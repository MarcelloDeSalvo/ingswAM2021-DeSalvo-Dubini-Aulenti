package it.polimi.ingsw.observers;

public interface ObserverViewIO {

    /**
     * Updates the state of a lobby or a view
     * @param message is the message that has been received and needs to be processed
     */
    void update(String message);
}
