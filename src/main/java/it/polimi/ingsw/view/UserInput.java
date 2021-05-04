package it.polimi.ingsw.view;

import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.commands.Message;

public interface UserInput {

    boolean readInput();
    void readUpdates(Message message);

    void setSender(ClientSender sender);
}