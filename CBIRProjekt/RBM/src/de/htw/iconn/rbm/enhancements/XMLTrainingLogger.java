

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
public class XMLTrainingLogger implements IRBMTrainingEnhancement {

    private final int updateIntervall;
    private final XMLWeightLogger logger;
    public XMLTrainingLogger(int updateIntervall) {
        this.updateIntervall = updateIntervall;
        this.logger = new XMLWeightLogger();
    }

      
    @Override
    public int getUpdateInterval() {
        return this.updateIntervall;
    }

    @Override
    public void action(CBIREvaluationModel evaluationModel) {
        try {
            logger.stepXmlLogTraining(evaluationModel);
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException ex) {
            Logger.getLogger(XMLTrainingLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
}
