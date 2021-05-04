package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.UserManager;

import java.util.HashMap;

public class Lobby {
    String lobbyName;
    HashMap<String, User> players;

    int maxPlayer;
    int numOfPlayersConnected;

    boolean isFull;
    boolean isClosed;

    public Lobby(String lobbyName, int maxPlayer) {
        this.lobbyName = lobbyName;
        this.maxPlayer = maxPlayer;
        players = new HashMap<>();
    }

    //LOBBY MANAGEMENT--------------------------------------------------------------------------------------------------
    public void addUser(User user){
        UserManager.addPlayer(players, user.getNickname(), user);
    }

    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public String getLobbyName() {
        return lobbyName;
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
                ", connected=" + numOfPlayersConnected + '\''+ numOfPlayersConnected +
                ", isFull=" + isFull +
                ", isClosed=" + isClosed +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------
}
