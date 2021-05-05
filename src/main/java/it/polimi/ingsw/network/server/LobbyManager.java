package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.Util;
import it.polimi.ingsw.network.commands.*;
import com.google.gson.Gson;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.observers.ObserverViewIO;
import it.polimi.ingsw.network.UserManager;

import java.util.ArrayList;
import java.util.HashMap;

public class LobbyManager implements  ObserverViewIO {

    private static final HashMap<String, User> connectedPlayers = new HashMap<>();;
    private static final HashMap<String, Lobby> lobbies = new HashMap<>();;


    @Override
    public void update(String mex){
        Gson gson = new Gson();
        Message deserializedMex = gson.fromJson(mex, Message.class);

        Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        User currentUser = connectedPlayers.get(senderNick);

        if(!hasPermission(currentUser))
            return;

        switch (command) {
            case QUIT:
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("Bye!").setNickname(senderNick).build());
                break;

            case HELLO:
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("Hello!").setNickname(senderNick).build());
                break;

            case HELLO_ALL:
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("Hello all!").setTarget(Target.EVERYONE_ELSE).setNickname(senderNick).build());
                break;

            case LOBBY_LIST:
                sendLobbyList(senderNick);
                break;

            case JOIN_LOBBY:
                JoinLobbyMessage joinLobbyMessage = gson.fromJson(mex, JoinLobbyMessage.class);
                String lobbyToJoinName = joinLobbyMessage.getLobbyName();

                if(Util.isPresent(lobbyToJoinName, lobbies)) {

                    if(joinLobby(lobbyToJoinName, currentUser))
                        UserManager.notifyUsers(connectedPlayers,
                                new Message.MessageBuilder().setCommand(Command.REPLY).
                                        setInfo("You joined " + lobbyToJoinName + " correctly!").setNickname(senderNick).build());
                    else
                        UserManager.notifyUsers(connectedPlayers,
                                new Message.MessageBuilder().setCommand(Command.REPLY).
                                        setInfo("The lobby " + lobbyToJoinName + " is already full! Please select another lobby").setNickname(senderNick).build());
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

                    UserManager.notifyUsers(connectedPlayers,
                            new Message.MessageBuilder().setCommand(Command.REPLY).
                                    setInfo("The lobby " + newLobbyName + " has been created correctly!").setNickname(senderNick).build());

                }
                else
                    UserManager.notifyUsers(connectedPlayers,
                            new Message.MessageBuilder().setCommand(Command.REPLY).
                                    setInfo("The lobby " + newLobbyName + " already exists! Please insert a valid name").setNickname(senderNick).build());

                break;

            default:
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Invalid Command").setNickname(senderNick).build());
                break;
        }
    }


    //LOBBY MANAGEMENT -------------------------------------------------------------------------------------------------
    public void sendLobbyList(String senderNick){

        ArrayList<String> lobbiesInfo = new ArrayList<>();
        for (String key: lobbies.keySet()) {
            lobbiesInfo.add(lobbies.get(key).toString());
        }
        UserManager.notifyUsers(connectedPlayers, new LobbyListMessage(lobbiesInfo, senderNick));
    }

    private boolean joinLobby (String lobbyToJoinName, User currentUser) {
        Lobby lobbyToJoin = lobbies.get(lobbyToJoinName);

        if(!lobbyToJoin.addUser(currentUser))
            return false;

        currentUser.addLobbyOrView(lobbyToJoin);
        currentUser.setStatus(Status.IN_LOBBY);

        return true;
    }

    private void createLobby (String newLobbyName, int numOfPlayers, User currentUser) {
        Lobby newLobby = new Lobby(newLobbyName, numOfPlayers, currentUser);

        newLobby.addUser(currentUser);
        lobbies.put(newLobbyName, newLobby);
        //System.out.println("create " + newLobbyName + newLobby);
        currentUser.addLobbyOrView(newLobby);

        currentUser.setStatus(Status.IN_LOBBY);
    }

    public boolean hasPermission (User user) {
        return user.getStatus() == Status.IN_LOBBY_MANAGER;
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
