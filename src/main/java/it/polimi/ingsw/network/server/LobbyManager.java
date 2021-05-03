package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.observers.ObservableViewIO;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.util.ArrayList;
import java.util.HashMap;

public class LobbyManager implements ObservableViewIO, ObserverViewIO {
    ArrayList<String> nicksOfPlayersConnected;
    HashMap<String,ObserverViewIO> connectedPlayers;

    public LobbyManager() {
        nicksOfPlayersConnected = new ArrayList<>();
        connectedPlayers = new HashMap<>();
    }

    @Override
    public void update(Message mex){
        Command command = mex.getCommand();
        String senderNick = mex.getSenderNickname();

        switch (command){
            case QUIT:
                notifyIO(new Message(Command.REPLY, "Bye!", Target.UNICAST, senderNick));
                break;

            case HELLO:
                notifyIO(new Message(Command.HELLO, "Hello!", Target.UNICAST, senderNick));
                break;

            case HELLO_ALL:
                notifyIO(new Message(Command.REPLY, "Hello!", Target.EVERYONE_ELSE, senderNick));
                break;

            case JOIN_LOBBY:

            case CREATE_LOBBY:

            case LOBBY_LIST:

            default:
                notifyIO(new Message(Command.REPLY, "Invalid command", Target.UNICAST, senderNick));
                break;
        }

    }


    @Override
    public void notifyIO(Message message) {
        switch (message.getTarget()){
            case UNICAST:
                if (isNamePresent(message.getSenderNickname()))
                    connectedPlayers.get(message.getSenderNickname()).update(message);
                break;

            case BROADCAST:
                for (String nick: connectedPlayers.keySet()) {
                    connectedPlayers.get(nick).update(message);
                }
                break;

            case EVERYONE_ELSE:
                for (String nick: connectedPlayers.keySet()) {
                    if (!nick.equals(message.getSenderNickname()))
                        connectedPlayers.get(nick).update(message);
                }
                break;
        }
    }

    //PLAYERSLIST MANAGEMENT -------------------------------------------------------------------------------------------
    @Override
    public boolean addPlayer(String nick, ObserverViewIO threadSender){
        if(!connectedPlayers.containsKey(nick)) {
            connectedPlayers.put(nick, threadSender);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean isNamePresent(String nick) {
        return connectedPlayers.containsKey(nick);
    }

    @Override
    public boolean removePlayer(String nick) {
        if (connectedPlayers.containsKey(nick)) {
            connectedPlayers.remove(nick);
            return true;
        }
        return false;
    }
}
