package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObservableViewIO;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.net.Socket;
import java.util.ArrayList;

public class LobbyManager implements ObservableViewIO, ObserverViewIO {
    ArrayList<String> nicksOfPlayersConnected;
    ArrayList<ObserverViewIO> observerViewIOS;

    public LobbyManager() {
        nicksOfPlayersConnected = new ArrayList<>();
        observerViewIOS = new ArrayList<>();
    }

    @Override
    public void update(Message mex, Socket client){
        Command command = mex.getCommand();


        switch (command){
            case QUIT:
                notifyIO_unicast(new Message(Command.REPLY, "Bye!"), client);
                break;
            case HELLO:
                notifyIO_unicast(new Message(Command.HELLO, "Hello!"), client);
                System.out.println("server");
                break;

            case HELLO_ALL:
                notifyIO_broadcast(new Message(Command.REPLY, "Hello!"));
                break;

            case LOGIN:

            case JOIN_LOBBY:

            case CREATE_LOBBY:

            case LOBBY_LIST:

            default:
                notifyIO_unicast(new Message(Command.REPLY, "Invalid command"), client);;
                break;
        }

    }


    @Override
    public void addObserverIO(ObserverViewIO observer) {
        if(observer!=null) {
            observerViewIOS.add(observer);
        }
    }

    @Override
    public void notifyIO_unicast(Message message, Socket socket) {
        for (ObserverViewIO obs: observerViewIOS) {
            if(obs.getSocket().equals(socket)){
                obs.update(message);
            }
        }
    }

    @Override
    public void notifyIO_broadcast(Message message) {
        for (ObserverViewIO obs: observerViewIOS) {
                obs.update(message);
        }
    }


    @Override
    public boolean readInput() {
        return false;
    }

    @Override
    public void update(Message message) {

    }

    @Override
    public Socket getSocket() {
        return null;
    }
}
