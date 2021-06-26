package it.polimi.ingsw.view;

import it.polimi.ingsw.network.commands.Message;

public interface UserInput {

    /**
     * Reads the user's inputs from the View
     */
    void readInput();

    /**
     * Receives the updates from the Controller or the ClientReceiver (which is also considered a Controller)
     */
    void readUpdates(String message);

    /**
     * Sends a message to the server or the controller (offline-mode)
     * @param message is the message that comes from the Gui or the Cli
     */
    void send(Message message);
}
