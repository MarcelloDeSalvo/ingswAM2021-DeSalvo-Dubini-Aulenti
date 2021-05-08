package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObserverController;
import it.polimi.ingsw.view.ClientView;

import java.util.ArrayList;

public class Gui extends ClientView {

    @Override
    public boolean readInput() {
        return false;
    }

    @Override
    public void readUpdates(String message) {

    }

    @Override
    public void setSender(ClientSender sender) {

    }

    @Override
    public void printHello() {

    }

    @Override
    public void printQuit(String nickname) {

    }

    @Override
    public void printHand(ArrayList<Integer> leaderIDs, String nickname) {

    }

    @Override
    public void printReply(String payload) {

    }

    @Override
    public void printLobby(ArrayList<String> lobbiesInfos) {

    }

    @Override
    public void printOrder(ArrayList<String> randomOrder) {

    }

    @Override
    public void printDeposit(String depositInfo) {

    }

    @Override
    public void askForResources(String nickname, int qty) {

    }

    @Override
    public void notifyFaithPathProgression(String nickname, int qty) {

    }

    @Override
    public void printLeaderCardRequest(String nickname) {

    }
}
