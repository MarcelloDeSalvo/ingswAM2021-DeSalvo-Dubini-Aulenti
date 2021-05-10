package it.polimi.ingsw.observers;

public interface ObservableController {

    /**
     * Adds a Controller to the observer list
     */
    void addObserverController(ObserverController observerController);

    /**
     * Notifies all the controllers
     */
    void notifyController(String message);

}
