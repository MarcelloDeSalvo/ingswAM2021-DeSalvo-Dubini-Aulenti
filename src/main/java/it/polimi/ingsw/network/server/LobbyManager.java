package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.observers.ObserverViewIO;
import it.polimi.ingsw.observers.UserManager;

import java.util.HashMap;

public class LobbyManager implements ObserverViewIO {

    HashMap<String,User> connectedPlayers;

    public LobbyManager() {
        connectedPlayers = new HashMap<>();
    }

    @Override
    public void update(Message mex){
        Command command = mex.getCommand();
        String senderNick = mex.getSenderNickname();

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

            case LOBBY_LIST:

            default:
                UserManager.notifyUsers(connectedPlayers, new Message(Command.REPLY, "Invalid command", Target.UNICAST, senderNick));
                break;
        }
    }

    public HashMap<String, User> getConnectedPlayers() {
        return connectedPlayers;
    }
}
