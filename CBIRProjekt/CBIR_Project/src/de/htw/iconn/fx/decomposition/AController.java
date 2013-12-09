/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

/**
 *
 * @author Moritz
 */
public abstract class AController implements Initializable, IFXController {
   
    protected Object loadController(String url) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        loader.load();
        return loader.getController();
    }
    
    
}
