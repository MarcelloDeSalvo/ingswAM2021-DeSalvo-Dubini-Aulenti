package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Target;

import java.util.ArrayList;

public interface ObserverModel {
    void printHello();
    void printQuit(String nickname);

    void printReply(String payload);
    void printReply_uni(String payload, String nickname);
    void printReply_everyOneElse(String payload, String nickname);

    void printLobby(ArrayList<String> lobbiesInfos);
    void printOrder(ArrayList<String> randomOrder);
    void printDeposit(String depositInfo);
    void printHand(ArrayList<Integer> leaderIDs, String nickname);

    void askForResources(String nickname, int qty);
    void notifyFaithPathProgression(int qty, String nickname);
    void printLeaderCardRequest(String nickname);

}
