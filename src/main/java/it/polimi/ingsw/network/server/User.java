package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObservableViewIO;
import it.polimi.ingsw.observers.ObserverThread;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class User implements ObserverThread, ObservableViewIO {
    private final String nickname;
    private final ServerReceiver serverReceiver;
    private final ServerSender serverSender;
    private final List<ObserverViewIO> serverAreas;

    private Status status;

    public User(String nickname, ServerReceiver serverReceiver, ServerSender serverSender, Status status) {
        this.nickname = nickname;
        this.serverReceiver = serverReceiver;
        this.serverSender = serverSender;
        serverReceiver.addThreadObserver(this);
        this.status = status;
        serverAreas = new CopyOnWriteArrayList<>();
    }

    @Override
    public void somethingHasBeenReceived(String message){
        notifyServerAreas(message);
    }

    @Override
    public void notifyServerAreas(String message) {

        for (ObserverViewIO serverArea: serverAreas) {
            serverArea.update(message);
        }
    }

    @Override
    public void addServerArea(ObserverViewIO serverArea){
        serverAreas.add(serverArea);
    }

    public void removeServerArea(ObserverViewIO serverArea){
        serverAreas.remove(serverArea);
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
