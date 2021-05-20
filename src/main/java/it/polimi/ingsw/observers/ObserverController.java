package it.polimi.ingsw.observers;


import it.polimi.ingsw.network.commands.Command;

public interface ObserverController {

    /**
     * Receives offline data from the controller
     * @param mex is the message received
     */
    void update(String mex, Command command, String senderNick);

}
