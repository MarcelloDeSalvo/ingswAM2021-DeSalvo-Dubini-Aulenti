package it.polimi.ingsw.observers;

import java.util.ArrayList;

public interface ObserverModel {
    void printHello();
    void printQuit(String nickname);
    void printHand(ArrayList<Integer> leaderIDs, String nickname);
    void printReply(String payload);
    void printLobby(ArrayList<String> lobbiesInfos);
    void printOrder(ArrayList<String> randomOrder);
    void printDeposit(String depositInfo);

}
