package it.polimi.ingsw.view;

import it.polimi.ingsw.network.client.ClientReceiver;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.commands.Message;

public abstract class View {

    public abstract void setSender(ClientSender sender);

    public abstract boolean readInput();
    public abstract void readUpdates(Message message);

    public abstract void printHello();
    public abstract void printQuit();
    public abstract void printReply(String payload);

}
