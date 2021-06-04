package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Command;

public interface ObserverThread {

    /**
     * Updates all the thread listeners that the user on the client-side has sent some message
     */
    void somethingHasBeenReceived(Command command, String message);
}
