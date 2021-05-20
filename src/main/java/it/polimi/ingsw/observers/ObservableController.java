package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Command;

public interface ObservableController {

    /**
     * Adds a Controller to the observer list
     */
    void addObserverController(ObserverController observerController);

    /**
     * Notifies all the controllers
     */
    void notifyController(String message, Command command, String senderNick);

}
