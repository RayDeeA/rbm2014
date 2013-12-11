

package de.htw.iconn.rbm.enhancements;

import de.htw.cbir.CBIREvaluationModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author moritz
 */
public class XMLEndTrainingLogger implements IRBMEndTrainingEnhancement  {
    
    private final XMLWeightLogger logger;


    public XMLEndTrainingLogger() {
        this.logger = new XMLWeightLogger();
    }


    @Override
    public void action(RBMInfoPackage info) {
        try {
            logger.stepXmlLogTraining(info);
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException ex) {
            Logger.getLogger(XMLTrainingLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
