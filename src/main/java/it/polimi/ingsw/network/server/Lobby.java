package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.UserManager;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.util.HashMap;

public class Lobby implements ObserverViewIO {
    String lobbyName;
    HashMap<String, User> players;

    int maxPlayers;
    int numOfPlayersConnected;

    boolean isFull;
    boolean isClosed;

    public Lobby(String lobbyName, int maxPlayers) {
        this.lobbyName = lobbyName;
        this.maxPlayers = maxPlayers;
        players = new HashMap<>();
    }

    //LOBBY MANAGEMENT--------------------------------------------------------------------------------------------------
    public boolean addUser(User user){
        if(isFull)
            return false;

        if(UserManager.addPlayer(players, user.getNickname(), user)) {
            numOfPlayersConnected++;
            if(numOfPlayersConnected == maxPlayers)
                setFull(true);
            return true;
        }
        else
            return false;

    }

    public boolean removeUser(User user){
        if(UserManager.removePlayer(players, user.getNickname())){
            numOfPlayersConnected--;
            return true;
        }
        else
            return false;
    }

    @Override
    public void update(String message) {

    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public String getLobbyName() {
        return lobbyName;
    }

    public int getMaxPlayer() {
        return maxPlayers;
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
        return "* lobbyName='" + lobbyName + '\'' +
                ", connected=" + numOfPlayersConnected + "/" + maxPlayers +
                ", isFull=" + isFull +
                ", isClosed=" + isClosed;
    }
    //------------------------------------------------------------------------------------------------------------------
}
