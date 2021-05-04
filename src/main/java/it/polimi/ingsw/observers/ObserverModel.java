package it.polimi.ingsw.observers;

import java.util.ArrayList;

public interface ObserverModel {
    void printHello();
    void printQuit();
    void printHand(ArrayList<Integer> leaderIDs);
    void printReply(String payload);
    void printLobby(ArrayList<String> lobbiesInfos);
}
