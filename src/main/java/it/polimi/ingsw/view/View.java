package it.polimi.ingsw.view;

import it.polimi.ingsw.observers.ObservableController;
import it.polimi.ingsw.observers.ObserverModel;

public interface View extends ObserverModel, ObservableController {

    /**
     * Wait the view to be fully loaded
     * @return true when it is loaded
     */
    boolean isViewReady();

}
