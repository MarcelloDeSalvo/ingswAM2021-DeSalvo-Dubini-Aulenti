package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.commands.*;
import com.google.gson.Gson;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.observers.ObserverViewIO;
import it.polimi.ingsw.observers.UserManager;

import java.util.ArrayList;
import java.util.HashMap;

public class LobbyManager implements  ObserverViewIO {
    ArrayList<String> nicksOfPlayersConnected;
    HashMap<String,User> connectedPlayers;
    ArrayList<Lobby> lobbies;

    public LobbyManager() {
        nicksOfPlayersConnected = new ArrayList<>();
        connectedPlayers = new HashMap<>();
        lobbies= new ArrayList<>();
    }

    @Override
    public void update(Message mex){
        Command command = mex.getCommand();
        String senderNick = mex.getSenderNickname();
        String original = mex.serialize();
        Gson gson= new Gson();

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

            case JOIN_LOBBY:

            case CREATE_LOBBY:
                CreateLobbyMessage createLobbyMessage=gson.fromJson(original,CreateLobbyMessage.class);
                System.out.println(mex.toString());
                //Lobby newLobby=new Lobby(createLobbyMessage.getLobbyName(),createLobbyMessage.getNumOfPlayers(),createLobbyMessage.isCustomParameters());
                //newLobby.addNick(mex.getSenderNickname());
                //lobbies.add(newLobby);
                break;

            default:
                UserManager.notifyUsers(connectedPlayers, new Message(Command.REPLY, "Invalid command", Target.UNICAST, senderNick));
                break;
        }
    }

    public HashMap<String, User> getConnectedPlayers() {
        return connectedPlayers;
    }
}
