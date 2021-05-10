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

    private static final HashMap<String, User> connectedPlayers = new HashMap<>();
    private static final HashMap<String, Lobby> lobbies = new HashMap<>();


    @Override
    public void update(String mex){
        Gson gson = new Gson();
        Message deserializedMex = gson.fromJson(mex, Message.class);

        Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        User currentUser = connectedPlayers.get(senderNick);

        if(!Command.canUseCommand(currentUser,command)){
            if(hasPermission(currentUser)) {
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("You can't use this command in the lobby manager!").setNickname(senderNick).build());
            }
            return;
        }

        if(command.getWhereToProcess()!=Status.IN_LOBBY_MANAGER)
            return;


        switch (command) {
            case QUIT:
                UserManager.notifyUsers(connectedPlayers,
                        new Message.MessageBuilder().setCommand(Command.REPLY).
                                setInfo("Bye!").setNickname(senderNick).build());
                currentUser.killThreads();
                UserManager.removePlayer(connectedPlayers, senderNick);
                System.out.println("# " + senderNick + " has disconnected");
                break;


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
                System.out.println(chatMessage.toString());
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
                    UserManager.notifyUsers(connectedPlayers,
                            new Message.MessageBuilder().setCommand(Command.REPLY).
                                    setInfo("The lobby " + newLobbyName + " has been created correctly!").setNickname(senderNick).build());

                    createLobby(newLobbyName, createLobbyMessage.getNumOfPlayers(), currentUser);
                }
                else
                    UserManager.notifyUsers(connectedPlayers,
                            new Message.MessageBuilder().setCommand(Command.REPLY).
                                    setInfo("The lobby " + newLobbyName + " already exists! Please insert a valid name").setNickname(senderNick).build());

                break;


            default:
                //UserManager.notifyUsers(connectedPlayers,new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Invalid Command, you are in lobby manager").setNickname(senderNick).build());
                break;
        }
    }


    //LOBBY MANAGEMENT -------------------------------------------------------------------------------------------------
    /**
     * Retrieves every lobby name and sends them over using a specific message
     */
    public void sendLobbyList(String senderNick){

        ArrayList<String> lobbiesInfo = new ArrayList<>();
        for (String key: lobbies.keySet()) {
            lobbiesInfo.add(lobbies.get(key).toString());
        }
        UserManager.notifyUsers(connectedPlayers, new LobbyListMessage(lobbiesInfo, senderNick));
    }

    /**
     * Allows an user to join a specific lobby and sets the user status to "IN_LOBBY"
     * @param lobbyToJoinName is the lobby that the user wants to join
     * @return true if everything goes right, false if the User cannot join the lobby
     */
    private boolean joinLobby (String lobbyToJoinName, User currentUser) {
        Lobby lobbyToJoin = lobbies.get(lobbyToJoinName);
        System.out.println(currentUser.getStatus());

        if(!lobbyToJoin.addUser(currentUser))
            return false;

        currentUser.addLobbyOrView(lobbyToJoin);
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
        currentUser.addLobbyOrView(newLobby);

        currentUser.setStatus(Status.IN_LOBBY);
    }

    /**
     * Checks if the user has a specific level of permission
     * @param user user to check
     * @return true if this is the case, false otherwise
     */
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
