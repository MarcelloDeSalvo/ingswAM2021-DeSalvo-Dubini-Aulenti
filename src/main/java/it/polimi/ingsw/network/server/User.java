package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObservableViewIO;
import it.polimi.ingsw.observers.ObserverThread;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class User implements ObserverThread, ObservableViewIO {
    private String nickname;
    private ServerReceiver serverReceiver;
    private ServerSender serverSender;
    private List<ObserverViewIO> lobbies;

    Status status;

    public User(String nickname, ServerReceiver serverReceiver, ServerSender serverSender, Status status) {
        this.nickname = nickname;
        this.serverReceiver = serverReceiver;
        this.serverSender = serverSender;
        serverReceiver.addThreadObserver(this);
        this.status = status;
        lobbies = new CopyOnWriteArrayList<>();
    }

    @Override
    public void userReceive(String message){
        notifyLobbyOrView(message);
    }

    @Override
    public void notifyLobbyOrView(String message) {

        for (ObserverViewIO lobby: lobbies) {
            lobby.update(message);
        }
    }

    @Override
    public void addLobbyOrView(ObserverViewIO lobby){
        lobbies.add(lobby);
    }

    public void removeLobbyOrView(ObserverViewIO lobby){
        lobbies.remove(lobby);
    }

    public void userSend(Message message){
        String stringToSend = message.serialize();
        serverSender.send(stringToSend);
    }


    public void killThreads(){
        serverReceiver.exit();
    }

    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public String getNickname() {
        return nickname;
    }

    public void setServerReceiver(ServerReceiver serverReceiver) {
        this.serverReceiver = serverReceiver;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
