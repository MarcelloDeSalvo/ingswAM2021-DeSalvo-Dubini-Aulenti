package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.server.User;

public interface ObserverViewIO {

    /**
     * Updates the state of a lobby or a view
     * @param message is the message that has been received and needs to be processed
     */
    void update(String message, Command command, String senderNickname);

    void onDisconnect(User user);
}
