package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.UserManager;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.observers.ObserverViewIO;
import it.polimi.ingsw.view.cli.Color;

import java.util.HashMap;

public class Lobby extends LobbyManager implements ObserverViewIO {
    private final String lobbyName;
    private final HashMap<String, User> players;
    private User owner;
    private Gson gson;

    private final int maxPlayers;
    private int numOfPlayersConnected;

    private boolean isFull = false;
    private boolean isClosed = false;

    public Lobby(String lobbyName, int maxPlayers, User owner) {
        this.lobbyName = lobbyName;
        this.maxPlayers = maxPlayers;
        this.owner = owner;
        players = new HashMap<>();
        gson = new Gson();
    }

    //LOBBY MANAGEMENT--------------------------------------------------------------------------------------------------
    @Override
    public synchronized void update(String mex) {

        Message deserializedMex = gson.fromJson(mex, Message.class);

        Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        if(!UserManager.isNamePresent(players, senderNick))
            return;

        User currentUser = players.get(senderNick);


        if(!hasPermission(currentUser, command))
            return;

        switch (command) {
            case EXIT_LOBBY:
                removeUser(currentUser);
                sendLobbyList(currentUser.getNickname());
                break;

            case PLAYER_LIST:
                notifyPlayerList(currentUser);
                break;

            case START_GAME:
                if(currentUser != owner)
                    UserManager.notifyUsers(players, new Message.MessageBuilder().setCommand(Command.REPLY).
                            setInfo("Only " + owner.getNickname() +" (the owner of the lobby) can start the game!").setNickname(senderNick).build());
                else
                    startGame();

                break;
        }
    }

    /**
     * Adds a new user to this lobby after checking if this can be done. <br>
     * Increments numOfPlayerConnected and if maxPlayers is reached the lobby is set to FULL
     * @param user the user that needs to be added
     * @return true if everything goes right and the user is actually added, false otherwise
     */
    public boolean addUser(User user){
        if(isFull||isClosed)
            return false;

        if(!UserManager.addPlayer(players, user.getNickname(), user))
            return false;

        numOfPlayersConnected++;
        notifyPlayerList(user);

        if(numOfPlayersConnected == maxPlayers)
            setFull(true);

        return true;
    }

    /**
     * Remove an user from this lobby after checking if the user is actually present. <br>
     * numOfPlayerConnected gets decremented and if the Lobby was full "isFull" is set to false. <br>
     * If the user leaving the lobby is the last one in it, the lobby gets deleted. <br>
     * If the user leaving the lobby is its owner, a new owner is selected between the users still in the lobby.
     * @param user the user that need to be removed
     */
    public void removeUser(User user){
        if(UserManager.removePlayer(players, user.getNickname())){

            numOfPlayersConnected--;
            notifySomeoneLeft(user);

            if(isFull)
                isFull = false;

            if(numOfPlayersConnected == 0) {
                deleteLobby(user);
            }
            else {
                if(user.getNickname().equals(owner.getNickname())){
                    String key = players.entrySet().stream().findFirst().get().getKey();
                    owner = players.get(key);

                    notifyNewOwner();
                }
            }

            user.removeServerArea(this);
            user.setStatus(Status.IN_LOBBY_MANAGER);
        }
    }

    /**
     * Delete this lobby and removes it from the user's list
     * @param user
     */
    private void deleteLobby (User user) {
        Lobby lobbyToDelete = getLobbies().get(lobbyName);

        getLobbies().remove(lobbyName, lobbyToDelete);
        user.removeServerArea(lobbyToDelete);
    }

    /**
     * Creates a new Controller, sets this Lobby to Closed and every player's status to "IN_GAME"
     */
    private void startGame() {
        notifyTheGameIsStarted();

        Controller controller = new Controller(players);

        for (String name: players.keySet()) {
            players.get(name).setStatus(Status.IN_GAME);
            players.get(name).addServerArea(controller.getView());
        }

        isClosed = true;

    }

    @Override
    public boolean hasPermission (User user, Command command) {
        if(!Command.canUseCommand(user, command)){

            if(user.getStatus() == Status.IN_LOBBY) {
                UserManager.notifyUsers(players,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("You can't use this command in the lobby!").setNickname(user.getNickname()).build());
            }

            return false;
        }

        return command.getWhereToProcess() == Status.IN_LOBBY;
    }

    @Override
    public void onDisconnect(User user) {
        if(user.getStatus()==Status.IN_LOBBY){
            removeUser(user);
        }
    }
    //------------------------------------------------------------------------------------------------------------------


    //NOTIFICATIONS-----------------------------------------------------------------------------------------------------

    /**
     * Notifies all the player in the lobby that a new player has joined
     */
    public void notifyNewJoin(User newJoined){
        UserManager.notifyUsers(players, new Message.MessageBuilder().setCommand(Command.REPLY)
                .setInfo("# The user: "+ newJoined.getNickname() + " has joined the lobby").setTarget(Target.EVERYONE_ELSE).setNickname(newJoined.getNickname()).build());

        UserManager.notifyUsers(players,
                new Message.MessageBuilder().setCommand(Command.REPLY).
                        setInfo("You joined " + lobbyName + " correctly!").setNickname(newJoined.getNickname()).build());

        System.out.println("# " + newJoined.getNickname() + " has joined " + lobbyName);
    }

    /**
     * Notifies all the player in the lobby that the owner has changed
     */
    public void notifyNewOwner(){
        UserManager.notifyUsers(players,
                new Message.MessageBuilder().setCommand(Command.REPLY)
                        .setNickname(owner.getNickname()).setInfo("The Owner has left, you are now the new Owner").build());

        UserManager.notifyUsers(players,
                new Message.MessageBuilder().setCommand(Command.REPLY)
                        .setTarget(Target.EVERYONE_ELSE).setNickname(owner.getNickname()).setInfo("The Owner has left, the new Owner is: " + owner.getNickname()).build());
    }


    /**
     * Notifies all the players in the lobby the nickname of everyone
     */
    public void notifyPlayerList(User user){
        UserManager.notifyUsers(players, new Message.MessageBuilder().setCommand(Command.REPLY)
                .setInfo("Players connected in " + lobbyName + ":\n"+ players.keySet().toString()).setNickname(user.getNickname()).build());
    }

    /**
     * Notifies all the players in the lobby that someone has left
     */
    public void notifySomeoneLeft(User userThatHasLeft){
        String senderNick = userThatHasLeft.getNickname();
        if(players.size() > 0) {
            UserManager.notifyUsers(players,
                    new Message.MessageBuilder().setCommand(Command.REPLY).
                            setInfo("# The user " + senderNick + " has left the lobby").setNickname(senderNick).
                            setTarget(Target.EVERYONE_ELSE).build());
        }

        userThatHasLeft.userSend(
                new Message.MessageBuilder().setCommand(Command.REPLY).
                        setInfo("You left: " + lobbyName + " correctly!").setNickname(senderNick).build()
        );
    }

    /**
     * Notifies all the players in the lobby that the game is started
     */
    public void notifyTheGameIsStarted(){
        UserManager.notifyUsers(players, new Message.MessageBuilder().setCommand(Command.REPLY)
                .setInfo("The Game is started! Have fun!").setTarget(Target.BROADCAST).build());
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
        String coloredFull, coloredClosed, available = Color.ANSI_GREEN.escape();;
        if(isFull ) {
            coloredFull = Color.ANSI_RED.escape();
            available = Color.ANSI_RED.escape();
        }
        else
            coloredFull = Color.ANSI_GREEN.escape();
        if(isClosed ) {
            coloredClosed = Color.ANSI_RED.escape();
            available = Color.ANSI_RED.escape();
        }
        else
            coloredClosed = Color.ANSI_GREEN.escape();



        return available+"\u06DD Lobby " + Color.ANSI_RESET.escape() + lobbyName +
                ", connected=" + numOfPlayersConnected + "/" + maxPlayers +
                ", Owner=" + owner.getNickname() +
                ","+coloredFull+" isFull=" + isFull +Color.ANSI_RESET.escape()+
                ","+coloredClosed+" isClosed=" + isClosed+Color.ANSI_RESET.escape();
    }
    //------------------------------------------------------------------------------------------------------------------
}
