package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObservableViewIO;
import it.polimi.ingsw.observers.ObserverThread;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class User implements ObserverThread, ObservableViewIO {
    private final String nickname;
    private final ServerReceiver serverReceiver;
    private final ServerSender serverSender;
    private final List<ObserverViewIO> serverAreas;
    private boolean active=true;
    private boolean received;


    private Status status;

    public User(String nickname, ServerReceiver serverReceiver, ServerSender serverSender, Status status) {
        this.nickname = nickname;
        this.serverReceiver = serverReceiver;
        this.serverSender = serverSender;
        serverReceiver.addThreadObserver(this);
        this.status = status;
        serverAreas = new CopyOnWriteArrayList<>();
        ServerConnectionCheck serverConnectionCheck=new ServerConnectionCheck();
        serverConnectionCheck.start();
    }

    //USER CONNECTION STABILITY-----------------------------------------------------------------------------------------

    public class ServerConnectionCheck extends Thread{
        public void run(){
            Timer timer = new Timer();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (received) {
                        received = false;
                        userSend(new Message.MessageBuilder().setCommand(Command.PING).build());

                    } else {
                        active = false;
                        System.out.println(nickname+" disconnected!");
                        this.cancel();
                        //Vari metodi sulla disconessione
                    }
                }
            };

            received=true;
            int initialDelay = 1000;
            int delta = 15000;
            timer.scheduleAtFixedRate(task,initialDelay,delta);

        }

    }

    public void pongReceived(){ received=true; }




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

    public synchronized void userSend(Message message){
        String stringToSend = message.serialize();
        serverSender.send(stringToSend);
    }
    //@override on disconnected

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

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }
}
