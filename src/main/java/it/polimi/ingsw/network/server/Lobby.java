package it.polimi.ingsw.network.server;

import java.util.ArrayList;

public class Lobby {
    String lobbyName;
    ArrayList<String> nicknames;

    int maxPlayer;
    int numOfPlayersConnected;

    boolean isFull;
    boolean isClosed;

    boolean customDecks;

    public Lobby(String lobbyName, int maxPlayer, boolean customDecks) {
        this.lobbyName = lobbyName;
        this.maxPlayer = maxPlayer;
        this.customDecks = customDecks;
        nicknames = new ArrayList<>();
    }

    //LOBBY MANAGEMENT--------------------------------------------------------------------------------------------------
    public boolean addNick(String nick){
        return nick!=null && nicknames.add(nick);
    }

    public boolean removeNick(String nick){
        return nick!=null && nicknames.remove(nick);
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public String getLobbyName() {
        return lobbyName;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public int getNumOfPlayersConnected() {
        return numOfPlayersConnected;
    }

    public boolean isFull() {
        return isFull;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public boolean isCustomDecks() {
        return customDecks;
    }

    public void setNicknames(ArrayList<String> nicknames) {
        this.nicknames = nicknames;
    }

    public void setNumOfPlayersConnected(int numOfPlayersConnected) {
        this.numOfPlayersConnected = numOfPlayersConnected;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
    //------------------------------------------------------------------------------------------------------------------

    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Lobby{" +
                "lobbyName='" + lobbyName + '\'' +
                ", nicknames=" + nicknames + '\n' +
                ", connected=" + numOfPlayersConnected + '\''+ numOfPlayersConnected +
                ", isFull=" + isFull +
                ", isClosed=" + isClosed +
                ", customDecks=" + customDecks +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------
}
