package it.polimi.ingsw.observers;


public interface ObserverController {

    /**
     * Receives offline data from the controller
     * @param mex is the message received
     */
    void update(String mex);

}
