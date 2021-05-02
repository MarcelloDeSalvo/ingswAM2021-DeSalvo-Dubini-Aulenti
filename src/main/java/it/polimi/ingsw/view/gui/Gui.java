package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.view.View;

public class Gui extends View {

    @Override
    public boolean readInput() {
        return false;
    }

    @Override
    public void readUpdates(Message message) {

    }

    @Override
    public void printHello() {

    }

    @Override
    public void printQuit() {

    }

    @Override
    public void printReply(String payload) {

    }

    @Override
    public void setSender(ClientSender sender) {

    }
}
