package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.ServerArea;
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
    private final List<ServerArea> serverAreas;

    private final PrintWriter out;

    /**
     * True if connected 
     */
    private boolean connected = true;

    /**
     * After this number of packets is lost the client will be considered disconnected
     */
    private final int maxLostPongs = 3;
    private int receivedPongsCounts = maxLostPongs;

    /**
     * After this amount of milliseconds the timer checks for a received pong and sends a ping
     */
    private final int timerTaskDelta = 10000;

    private Status status;

    public User(String nickname, PrintWriter out, Status status) {
        this.nickname = nickname;
        this.out = out;
        this.status = status;

        serverAreas = new CopyOnWriteArrayList<>();
        ServerConnectionCheck serverConnectionCheck = new ServerConnectionCheck();
        serverConnectionCheck.start();
    }

    //USER CONNECTION STABILITY-----------------------------------------------------------------------------------------
    public void reconnect(){
        connected =true;
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
                    if (receivedPongsCounts >0) {
                        receivedPongsCounts--;
                        userSend(new Message.MessageBuilder().setCommand(Command.PING).build());
                        //System.out.println(nickname + " Ping sent");

                    } else {
                        connected = false;
                        System.out.println(nickname + " disconnected!");

                        for (ObserverViewIO obs:serverAreas) {
                            obs.onDisconnect(getThis());
                        }

                        this.cancel();
                    }
                }
            };

            int initialDelay = 100;
            timer.scheduleAtFixedRate(task,initialDelay, timerTaskDelta);
        }
    }

    /**
     * Sends a message through the socket
     * @param message the message to send
     */
    public synchronized void userSend(Message message){
        String stringToSend = message.serialize();
        out.println(stringToSend);
        out.flush();
    }

    @Override
    public void notifyServerAreas(Command command, String message) {

        if (command==Command.PONG){
            pongReceived();
            return;
        }

        for (ServerArea serverArea: serverAreas) {
            if (serverArea.getAreaStatus() == command.getWhereToProcess())
                serverArea.update(message, command, nickname);
        }
    }

    /**
     * This method is used to notify when the game ends so that the Lobby can set itself to 'isClosed = false'
     */
    public void notifyEndGame(Message message) {
        for (ObserverViewIO serverArea: serverAreas) {
            serverArea.update(" ", message.getCommand(), nickname);
        }
    }

    /**
     * Updates the user when a Pong is successfully received.
     */
    public void pongReceived(){
        System.out.println("PONG RECEIVED IN USER: " + nickname);
        receivedPongsCounts = maxLostPongs;
    }

    @Override
    public void addServerArea(ServerArea serverArea){
        serverAreas.add(serverArea);
    }

    @Override
    public void removeServerArea(ServerArea serverArea){
        serverAreas.remove(serverArea);
    }


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

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
    //------------------------------------------------------------------------------------------------------------------
}
