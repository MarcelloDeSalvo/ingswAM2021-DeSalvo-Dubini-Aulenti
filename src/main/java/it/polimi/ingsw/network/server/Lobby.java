package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.network.UserManager;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.util.HashMap;

public class Lobby extends LobbyManager implements ObserverViewIO {
    String lobbyName;
    HashMap<String, User> players;
    User owner;

    int maxPlayers;
    int numOfPlayersConnected;

    boolean isFull;
    boolean isClosed;

    public Lobby(String lobbyName, int maxPlayers, User owner) {
        this.lobbyName = lobbyName;
        this.maxPlayers = maxPlayers;
        this.owner = owner;
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
            if(numOfPlayersConnected == 0) {
                deleteLobby(lobbyName, user);
            }

            user.setStatus(Status.IN_LOBBY_MANAGER);
            return true;
        }
        else
            return false;
    }

    @Override
    public void update(String mex) {
        Gson gson = new Gson();
        Message deserializedMex = gson.fromJson(mex, Message.class);

        Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        if(!UserManager.isNamePresent(players, senderNick))
            return;

        User currentUser = players.get(senderNick);

        if(!hasPermission(currentUser))
            return;

        switch (command) {
            case EXIT_LOBBY:

                if(removeUser(currentUser)) {
                    if(players.size() > 0) {
                        UserManager.notifyUsers(players, new Message(Command.REPLY,
                                "The user " + senderNick + " has left the lobby", Target.EVERYONE_ELSE, senderNick));
                    }

                    currentUser.userSend(new Message(Command.REPLY,
                            "You left: " + lobbyName + " correctly!", Target.UNICAST, senderNick));
                }

                break;

            case START_GAME:
                break;

        }
    }

    public boolean hasPermission (User user) {
        return user.getStatus() == Status.IN_LOBBY;
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
