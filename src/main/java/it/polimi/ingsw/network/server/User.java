package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObservableViewIO;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.io.PrintWriter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class User implements ObservableViewIO {

    private final String nickname;
    //private ServerReceiver serverReceiver;
    //private ServerSender serverSender;
    private final List<ObserverViewIO> serverAreas;
    private final PrintWriter out;

    private boolean active = true;
    private int received = 3;

    private Status status;

    public User(String nickname, PrintWriter out, Status status) {
        this.nickname = nickname;
        this.out = out;
        this.status = status;

        serverAreas = new CopyOnWriteArrayList<>();
        ServerConnectionCheck serverConnectionCheck = new ServerConnectionCheck();
        serverConnectionCheck.start();
    }

    /*
    public User(String nickname, ServerReceiver serverReceiver, ServerSender serverSender, Status status) {
        this.nickname = nickname;
        this.serverReceiver = serverReceiver;
        this.serverSender = serverSender;
        this.status = status;

        //serverReceiver.addThreadObserver(this);

        serverAreas = new CopyOnWriteArrayList<>();
        ServerConnectionCheck serverConnectionCheck = new ServerConnectionCheck();
        serverConnectionCheck.start();
    } */

    //USER CONNECTION STABILITY-----------------------------------------------------------------------------------------
    public void reconnect(ServerReceiver serverReceiver, ServerSender serverSender){
        //this.serverReceiver=serverReceiver;
        //this.serverSender=serverSender;
        active=true;
        ServerConnectionCheck serverConnectionCheck=new ServerConnectionCheck();
        serverConnectionCheck.start();
    }

    /**
     * ServerConnectionCheck is a thread with a timer that pings the user back and forth to see if he's still connected.
     */
    public class ServerConnectionCheck extends Thread{
        public void run(){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (received>0) {
                        if(received==2)
                            System.out.println("\nNe ho perso uno! "+nickname+"\n");
                        if(received==1)
                            System.out.println("\nPerso un altro! "+nickname+"\n");
                        received --;
                        userSend(new Message.MessageBuilder().setCommand(Command.PING).build());
                        System.out.println(nickname + " Ping sent");

                    } else {
                        active = false;
                        System.out.println(nickname + " disconnected!");
                        //serverReceiver.exit();
                        //serverSender.exit();
                        for (ObserverViewIO obs:serverAreas) {
                            obs.onDisconnect(getThis());
                        }
                        this.cancel();
                    }
                }
            };

            int initialDelay = 100;
            int delta = 10000;
            timer.scheduleAtFixedRate(task,initialDelay,delta);
        }

    }

    /**
     * Updates the user when a Pong is successfully received.
     */
    public void pongReceived(){
        System.out.println("PONG RECEIVED IN USER: " + nickname);
        received = 3;
    }

    @Override
    public void notifyServerAreas(Command command, String message) {

        if (command==Command.PONG){
            pongReceived();
            return;
        }

        for (ObserverViewIO serverArea: serverAreas) {
            serverArea.update(message, command, nickname);
        }
    }

    @Override
    public void addServerArea(ObserverViewIO serverArea){
        serverAreas.add(serverArea);
    }

    @Override
    public void removeServerArea(ObserverViewIO serverArea){
        serverAreas.remove(serverArea);
    }

    public synchronized void userSend(Message message){
        String stringToSend = message.serialize();
        send(stringToSend);
    }

    /**
     * Sends a message to the ClientReceiver
     */
    public synchronized void send(String mex){
        out.println(mex);
        out.flush();
    }


    /**
     * This method is used to notify when the game ends so that the Lobby can set itself to 'isClosed = false'
     */
    public void notifyEndGame(Message message) {
        for (ObserverViewIO serverArea: serverAreas) {
            serverArea.update(" ", message.getCommand(), nickname);
        }
    }

    //@override on disconnected

    /*
    public void killThreads(){
        serverReceiver.exit();
    } */

    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public User getThis(){
        return this;
    }

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

   /* public void setReceived(boolean received) {
        this.received = received;
    }*/
    //------------------------------------------------------------------------------------------------------------------
}
