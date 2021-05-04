package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObservableViewIO;
import it.polimi.ingsw.observers.ObserverThread;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.util.ArrayList;

enum Status{
    IN_LOBBY_MANAGER, IN_LOBBY, IN_GAME, DISCONNECTED
}

public class User implements ObserverThread, ObservableViewIO {
    String nickname;
    ServerReceiver serverReceiver;
    ServerSender serverSender;
    ArrayList<ObserverViewIO> lobbies;

    Status status;

    public User(String nickname, ServerReceiver serverReceiver, ServerSender serverSender, Status status) {
        this.nickname = nickname;
        this.serverReceiver = serverReceiver;
        this.serverSender = serverSender;
        serverReceiver.addThreadObserver(this);
        this.status = status;
        lobbies = new ArrayList<>();
    }


    @Override
    public void userReceive(Message message){
        notifyLobbyOrView(message);
    }

    @Override
    public void notifyLobbyOrView(Message message) {
        for (ObserverViewIO lobby: lobbies) {
            lobby.update(message);
        }
    }

    @Override
    public void addLobbyOrView(ObserverViewIO lobby){
        lobbies.add(lobby);
    }

    public void userSend(Message message){
        serverSender.update(message);
    }


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public String getNickname() {
        return nickname;
    }

    public ServerReceiver getServerReceiver() {
        return serverReceiver;
    }

    public void setServerReceiver(ServerReceiver serverReceiver) {
        this.serverReceiver = serverReceiver;
    }

    public ServerSender getServerSender() {
        return serverSender;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
