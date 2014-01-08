package de.htw.iconn.views;

import de.htw.iconn.enhancement.IVisualizeObserver;
import de.htw.iconn.enhancement.RBMInfoPackage;
import java.util.ArrayList;
import java.util.LinkedList;
public class ErrorViewModel implements IVisualizeObserver {

    private final ErrorViewController controller;

    private final LinkedList<Float> errors;
    private final LinkedList<Integer> epochs;

    public ErrorViewModel(ErrorViewController controller) {
        this.controller = controller;
        this.errors = new LinkedList<>();
        this.epochs = new LinkedList<>();
    }

    @Override
    public void update(RBMInfoPackage pack) {
        getErrors().add(pack.getError());
        getEpochs().add(pack.getEpochs());
        controller.update();
    }

    /**
     * @return the errors
     */
    public LinkedList<Float> getErrors() {
        return errors;
    }

    public void clear() {
        getEpochs().clear();
        errors.clear();
        controller.clear();
    }

    /**
     * @return the epochs
     */
    public LinkedList<Integer> getEpochs() {
        return epochs;
    }

}
