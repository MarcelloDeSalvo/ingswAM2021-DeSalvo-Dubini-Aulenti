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
    ArrayList<String> nicksOfPlayersConnected;
    HashMap<String, User> connectedPlayers;
    HashMap<String, Lobby> lobbies;

    public LobbyManager() {
        nicksOfPlayersConnected = new ArrayList<>();
        connectedPlayers = new HashMap<>();
        lobbies = new HashMap<>();
    }

    @Override
    public void update(Message mex){
        Command command = mex.getCommand();
        String senderNick = mex.getSenderNickname();
        String original = mex.serialize();
        Gson gson = new Gson();

        User currentUser = connectedPlayers.get(senderNick);

        if(hasPermission(currentUser))
            return;

        switch (command){
            case QUIT:
                UserManager.notifyUsers(connectedPlayers, new Message(Command.REPLY, "Bye!", Target.UNICAST, senderNick));
                break;

            case HELLO:
                UserManager.notifyUsers(connectedPlayers, new Message(Command.HELLO, "Hello!", Target.UNICAST, senderNick));
                break;

            case HELLO_ALL:
                UserManager.notifyUsers(connectedPlayers, new Message(Command.REPLY, "Hello!", Target.EVERYONE_ELSE, senderNick));
                break;

            case LOBBY_LIST:
                ArrayList<String> lobbiesInfos = new ArrayList<>();
                for (String key: lobbies.keySet()) {
                   lobbiesInfos.add(lobbies.get(key).toString());
                }

                UserManager.notifyUsers(connectedPlayers, new LobbyListMessage(lobbiesInfos, senderNick));
                break;

            case JOIN_LOBBY:
                JoinLobbyMessage joinLobbyMessage = gson.fromJson(original, JoinLobbyMessage.class);
                Lobby lobbyToJoin = lobbies.get(joinLobbyMessage.getLobbyName());
                lobbyToJoin.addUser(currentUser);
                break;

            case CREATE_LOBBY:
                CreateLobbyMessage createLobbyMessage = gson.fromJson(original, CreateLobbyMessage.class);
                String newLobbyName = createLobbyMessage.getLobbyName();

                if(!Util.isPresent(newLobbyName, lobbies))
                {
                    Lobby newLobby = new Lobby(newLobbyName, createLobbyMessage.getNumOfPlayers());
                    newLobby.addUser(currentUser);
                    lobbies.put(newLobbyName, newLobby);

                    UserManager.notifyUsers(connectedPlayers, new Message(Command.REPLY, "The lobby " + newLobbyName + " has been created", Target.UNICAST, senderNick));
                }
                else
                    UserManager.notifyUsers(connectedPlayers, new Message(Command.REPLY, "The lobby " + newLobbyName + " already exists! Please insert a valid name", Target.UNICAST, senderNick));

                break;

            default:
                UserManager.notifyUsers(connectedPlayers, new Message(Command.REPLY, "Invalid command", Target.UNICAST, senderNick));
                break;
        }

    }

    public boolean hasPermission (User user) {
        return user.getStatus() == Status.IN_LOBBY_MANAGER;
    }

    public HashMap<String, User> getConnectedPlayers() {
        return connectedPlayers;
    }
}
