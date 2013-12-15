

package de.htw.iconn.fx.decomposition.enhancement;

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
public class XMLTrainingLogger implements IRBMTrainingEnhancement {

    private final int updateIntervall;
    private final XMLWeightLogger logger;
    public XMLTrainingLogger(int updateIntervall) {
        this.updateIntervall = updateIntervall;
        this.logger = new XMLWeightLogger();
    }

    public XMLTrainingLogger() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

      
    @Override
    public int getUpdateInterval() {
        return this.updateIntervall;
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
