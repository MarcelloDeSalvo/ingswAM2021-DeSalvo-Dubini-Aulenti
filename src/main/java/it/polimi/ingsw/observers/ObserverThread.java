package it.polimi.ingsw.observers;

public interface ObserverThread {

    /**
     * Updates all the thread listeners that the user on the client-side has sent some message
     */
    void somethingHasBeenReceived(String message);
}
