package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.Util;
import it.polimi.ingsw.network.ServerArea;
import it.polimi.ingsw.network.commands.*;
import com.google.gson.Gson;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.network.UserManager;

import java.util.HashMap;


public class LobbyManager implements ServerArea {

    private static final HashMap<String, User> connectedPlayers = new HashMap<>();
    private static final HashMap<String, Lobby> lobbies = new HashMap<>();

    private final Gson gson;

    public LobbyManager() {
        gson = new Gson();
    }

    @Override
    public synchronized void update(String mex, Command command, String senderNick){

        User currentUser = connectedPlayers.get(senderNick);

        if(!hasPermission (currentUser, command))
            return;

        Message deserializedMex = gson.fromJson(mex, Message.class);

        switch (command) {

            case HELLO:
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.HELLO).
                                setInfo("Hello!").setNickname(senderNick).build());
                break;

            case HELLO_ALL:
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("Hello all!").setTarget(Target.EVERYONE_ELSE).setNickname(senderNick).build());
                break;

            case CHAT:
                ChatMessage chatMessage = gson.fromJson(mex, ChatMessage.class);
                String receiver = chatMessage.getReceiver();
                if(!UserManager.isNamePresent(connectedPlayers,receiver)){
                    UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("Sorry to inform you the user you want to contact isn't connected.").setNickname(senderNick).build());
                }
                UserManager.notifyUsers(connectedPlayers,
                        new ChatMessage(senderNick,deserializedMex.getInfo(),receiver));
                break;

            case CHAT_ALL:
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.CHAT_ALL).
                                setInfo(deserializedMex.getInfo()).setNickname(senderNick).setTarget(Target.EVERYONE_ELSE).build());
                break;

            case LOBBY_LIST:
                sendLobbyList(senderNick);
                break;

            case JOIN_LOBBY:
                JoinLobbyMessage joinLobbyMessage = gson.fromJson(mex, JoinLobbyMessage.class);
                String lobbyToJoinName = joinLobbyMessage.getLobbyName();

                if(Util.isPresent(lobbyToJoinName, lobbies)) {

                    if(joinLobby(lobbyToJoinName, currentUser))
                        lobbies.get(lobbyToJoinName).notifyNewJoin(currentUser);
                    else
                        UserManager.notifyUsers(connectedPlayers,
                                new Message.MessageBuilder().setCommand(Command.REPLY).
                                        setInfo("The lobby " + lobbyToJoinName + " is closed or is already full! Please select another lobby").setNickname(senderNick).build());
                }
                else
                    UserManager.notifyUsers(connectedPlayers,
                            new Message.MessageBuilder().setCommand(Command.REPLY).
                                    setInfo("The lobby " + lobbyToJoinName + " does not exist! Please select a valid Lobby").setNickname(senderNick).build());

                break;

            case CREATE_LOBBY:
                CreateLobbyMessage createLobbyMessage = gson.fromJson(mex, CreateLobbyMessage.class);
                String newLobbyName = createLobbyMessage.getLobbyName();

                if(!Util.isPresent(newLobbyName, lobbies)) {
                    createLobby(newLobbyName, createLobbyMessage.getNumOfPlayers(), currentUser);
                }
                else
                    UserManager.notifyUsers(connectedPlayers,
                            new Message.MessageBuilder().setCommand(Command.REPLY).
                                    setInfo("The lobby " + newLobbyName + " already exists! Please insert a valid name").setNickname(senderNick).build());
                break;
        }
    }


    //LOBBY MANAGEMENT -------------------------------------------------------------------------------------------------
    /**
     * Retrieves every lobby name and sends them over using a specific message
     */
    public void sendLobbyList(String senderNick){

        LobbyListMessage lobbyListMessage = new LobbyListMessage(senderNick);
        for (String key: lobbies.keySet()) {
            Lobby lobby = lobbies.get(key);
            lobbyListMessage.addLobbyInfos(lobby.getLobbyName(), lobby.getOwnerNick(), lobby.getNumOfPlayersConnected(), lobby.getMaxPlayer(), lobby.isFull(), lobby.isClosed());
        }

        UserManager.notifyUsers(connectedPlayers, lobbyListMessage);
    }

    /**
     * Allows an user to join a specific lobby and sets the user status to "IN_LOBBY"
     * @param lobbyToJoinName is the lobby that the user wants to join
     * @return true if everything goes right, false if the User cannot join the lobby
     */
    private boolean joinLobby (String lobbyToJoinName, User currentUser) {
        Lobby lobbyToJoin = lobbies.get(lobbyToJoinName);

        if(!lobbyToJoin.addUser(currentUser))
            return false;

        currentUser.addServerArea(lobbyToJoin);
        currentUser.setStatus(Status.IN_LOBBY);

        return true;
    }

    /**
     * Create a new lobby, adds the user to it and sets his status to "IN_LOBBY"
     * @param newLobbyName name of the lobby that i want to create
     * @param numOfPlayers max number of players that the new lobby can have
     * @param currentUser the user that will be owner of the lobby
     */
    private void createLobby (String newLobbyName, int numOfPlayers, User currentUser) {
        Lobby newLobby = new Lobby(newLobbyName, numOfPlayers, currentUser);

        newLobby.addUser(currentUser);
        lobbies.put(newLobbyName, newLobby);
        currentUser.addServerArea(newLobby);

        currentUser.setStatus(Status.IN_LOBBY);

        UserManager.notifyUsers(connectedPlayers,
                new Message.MessageBuilder().setCommand(Command.REPLY).
                        setInfo("The lobby " + newLobbyName + " has been created correctly!").setNickname(currentUser.getNickname()).build());

        newLobby.notifyNewJoin(currentUser);
    }

    @Override
    public boolean hasPermission (User user, Command command) {
        if(!Command.canUseCommand(user, command)){

            if(user.getStatus() == Status.IN_LOBBY_MANAGER) {
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("You can't use this command in the lobby manager!").setNickname(user.getNickname()).build());
            }

            return false;
        }

        return true;
    }

    @Override
    public Status getAreaStatus(){
        return Status.IN_LOBBY_MANAGER;
    }

    @Override
    public synchronized void onDisconnect(User user) {
        UserManager.removePlayer(connectedPlayers, user.getNickname());
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTERS AND SETTERS-----------------------------------------------------------------------------------------------
    public HashMap<String, User> getConnectedPlayers() {
        return connectedPlayers;
    }

    public HashMap<String, Lobby> getLobbies() {
        return lobbies;
    }
    //------------------------------------------------------------------------------------------------------------------
}
