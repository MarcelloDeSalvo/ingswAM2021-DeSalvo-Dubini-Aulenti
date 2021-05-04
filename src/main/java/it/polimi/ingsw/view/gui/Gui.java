package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.view.ClientView;

import java.util.ArrayList;

public class Gui extends ClientView {

    @Override
    public boolean readInput() {
        return false;
    }

    @Override
    public void readUpdates(Message message) {

    }

    @Override
    public void setSender(ClientSender sender) {

    }

    @Override
    public void printHello() {

    }

    @Override
    public void printQuit() {

    }

    @Override
    public void printHand(ArrayList<Integer> leaderIDs) {

    }

    @Override
    public void printReply(String payload) {

    }

    @Override
    public void printLobby(ArrayList<String> lobbiesInfos) {

    }
}
