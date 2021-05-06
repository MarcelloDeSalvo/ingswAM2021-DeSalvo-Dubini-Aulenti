package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.UserManager;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.util.HashMap;

public class Lobby extends LobbyManager implements ObserverViewIO {
    private final String lobbyName;
    private final HashMap<String, User> players;
    private User owner;

    private final int maxPlayers;
    private int numOfPlayersConnected;

    private boolean isFull;
    private boolean isClosed;

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
               deleteLobby(user);
                System.out.println(user);
            }else{
                if(user.getNickname().equals(owner.getNickname())){
                    owner = players.get(0); //DA VEDERE--------------------------------||||||||||||||||||||||||||||||
                    System.out.println(owner);
                }
            }

            user.setStatus(Status.IN_LOBBY_MANAGER);
            return true;
        }
        else
            return false;
    }

    private void deleteLobby (User user) {
        System.out.println(getConnectedPlayers());
        Lobby lobbyToDelete = getLobbies().get(lobbyName);
        System.out.println(getLobbies());
        System.out.println(lobbyName + " " + lobbyToDelete);

        getLobbies().remove(lobbyName, lobbyToDelete);
        user.removeLobbyOrView(lobbyToDelete);
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

        if(!hasPermission(currentUser)){
            if(!hasPermission(currentUser)){
                return;
            }
        }


        switch (command) {
            case EXIT_LOBBY:
                if(removeUser(currentUser)) {
                    if(players.size() > 0) {
                        UserManager.notifyUsers(players, new Message.MessageBuilder().setCommand(Command.REPLY).
                                        setInfo("The user " + senderNick + " has left the lobby").setNickname(senderNick).
                                        setTarget(Target.EVERYONE_ELSE).build());
                    }

                    currentUser.userSend(new Message.MessageBuilder().setCommand(Command.REPLY).
                                    setInfo("You left: " + lobbyName + " correctly!").setNickname(senderNick).build());
                }

                break;


            case START_GAME:
                if(currentUser != owner)
                    UserManager.notifyUsers(players, new Message.MessageBuilder().setCommand(Command.REPLY).
                            setInfo("Only " + owner +" (the owner of the lobby) can start the game!").setNickname(senderNick).build());
                else
                    startGame();
                    //printare tutti i comandi a disposizione

                break;


            default:
                UserManager.notifyUsers(players, new Message.MessageBuilder()
                        .setCommand(Command.REPLY).setInfo("Invalid Command").setNickname(senderNick).build());
                break;
        }
    }

    @Override
    public boolean hasPermission (User user) {
        return user.getStatus() == Status.IN_LOBBY;
    }

    private void startGame() {
        Controller controller = new Controller(players);

        for (String name: players.keySet()) {
            players.get(name).setStatus(Status.IN_GAME);
            players.get(name).addLobbyOrView(controller.getVirtualView());
        }
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
