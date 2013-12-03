/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author christoph
 */
public class ControlCenterModel {
    private List<Object> childrenControllers;
    
    public ControlCenterModel(List<Object> controllers){
        this.childrenControllers = controllers;
    }
    
    public ControlCenterModel(){
        this(new LinkedList<>());
    }
    
    public void addChildController(Object controller){
        this.childrenControllers.add(controller);
    }
    
    public List<Object> getChildrenControllers(){
        return this.childrenControllers;
    }
}
