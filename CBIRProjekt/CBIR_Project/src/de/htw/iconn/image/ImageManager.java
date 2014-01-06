package de.htw.iconn.image;

import de.htw.iconn.tools.ShuffleArrayHelper;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

public class ImageManager {

    private static String startDirectory = "CBIR_Project/images";

    private Pic[] images;
    private File imageDirectory;
    private HashMap<String, List<Pic>> imageGroups;

    public ImageManager() {
        this.images = new Pic[0];
        this.imageGroups = new HashMap<String, List<Pic>>();
    }

    public ImageManager(File imageDirectory) {
        this.imageGroups = new HashMap<String, List<Pic>>();
        this.loadImages(imageDirectory);
    }

    public int getImageCount() {
        return images.length;
    }

    public Pic[] getImages(boolean sorted) {
        if (sorted) {
            return this.images;
        } else {
            Pic[] picsCopied = new Pic[this.images.length];
            System.arraycopy(this.images, 0, picsCopied, 0, this.images.length);
            ShuffleArrayHelper<Pic> picsShuffler = new ShuffleArrayHelper<>();
            picsShuffler.shuffleArray(picsCopied);
            return picsCopied;
        }
    }

    public Set<String> getGroupNames() {
        return imageGroups.keySet();
    }

    public List<Pic> getImageInGroup(String groupName) {
        return imageGroups.get(groupName);
    }

    /**
     * Lade alles Bilder in ihrer verkleinerten Version von der Festplatte
     *
     * @param imageDirectory
     */
    public void loadImages(final File imageDirectory) {
        this.imageDirectory = imageDirectory;

        // besorge alle gültigen Bilddateien aus dem Verzeichnis
        final File[] imageFiles = imageDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.endsWith("jpg") || name.endsWith("png") || name.endsWith("gif"));
            }
        });

        // erlaube nur ca. 65536 Bilder
        final int maxImages = 65536;
        if (imageFiles.length > maxImages) {
            System.out.println("Too many images, restricting to " + maxImages);
        }
        final int imageCount = Math.min(maxImages, imageFiles.length);

        // lade alle Bilder
        List<Pic> list = new ArrayList<Pic>();
        for (int i = 0; i < imageCount; i++) {
            Pic image = loadImage(imageFiles[i]);
            if (image != null) {
                image.setId(i);
                image.setRank(i);
                list.add(image);
            }
        }

        // packe die Bilder in ihre Endg��ltige Form
        images = new Pic[list.size()];
        for (int i = 0; i < list.size(); i++) {
            addImage(list.get(i), i);
        }

        // wieviele Bilder gibt es pro Gruppe
        for (List<Pic> groupImages : imageGroups.values()) {
            for (Pic pic : groupImages) {
                pic.setTypeOccurrence(groupImages.size());
            }
        }
    }

    private void addImage(Pic image, int index) {

        // das Bild wird zur allgemeinen Liste hinzugef��gt
        images[index] = image;

        // und in eine Gruppenliste einsortiert
        String key = image.getCategory();
        if (imageGroups.containsKey(key)) {
            imageGroups.get(key).add(image);
        } else {
            List<Pic> imageList = new ArrayList<Pic>();
            imageList.add(image);
            imageGroups.put(key, imageList);
        }
    }

    public Pic get(int index) {
        if (index < 0 || index > images.length - 1) {
            return null;
        }

        return images[index];
    }

    /**
     * Lade das Bild von der Festplatte und verkleinere es
     *
     * @param imageFile
     * @return
     */
    public Pic loadImage(File imageFile) {
        BufferedImage image = null;
        try {

            image = ImageIO.read(imageFile);
        } catch (Exception e) {
            System.out.println("Could not load: " + imageFile.getAbsolutePath());
            e.printStackTrace();
            return null;
        }

        int iw = image.getWidth(null);
        int ih = image.getHeight(null);

        int maxOrigImgSize = Math.max(iw, ih);

        // maximale Kantenl��nge
        float thumbSize = 128;

        //skalierungsfaktor bestimmen:
        float scale = (maxOrigImgSize > thumbSize) ? thumbSize / maxOrigImgSize : 1;

        int nw = (int) (iw * scale);
        int nh = (int) (ih * scale);

        // Bild verkleinern
        BufferedImage currBi = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D big = currBi.createGraphics();
        big.drawImage(image, 0, 0, nw, nh, null);

        String filename = imageFile.getName().toLowerCase();
        Pic currPic = new Pic();
        currPic.setName(filename);
        currPic.setType(filename.split("[_]")[0]);
        currPic.setDisplayImage(currBi);
        currPic.setOrigWidth(iw);
        currPic.setOrigHeight(ih);

        return currPic;
    }

    public String getImageSetName() {
        return imageDirectory.toPath().getFileName().toString();
    }

    public String getNameFromIndex(int index) {
        LinkedList<String> groupList = new LinkedList<>(getGroupNames());
        groupList.add(0, "All");
        return groupList.get(index);
    }
}
