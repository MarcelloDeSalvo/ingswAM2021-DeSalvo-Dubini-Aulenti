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

    private final Message ping = new Message.MessageBuilder().setCommand(Command.PING).build();
    private final Message inactivity = new Message.MessageBuilder().setCommand(Command.INACTIVITY_KICK).build();

    /**
     * True if connected 
     */
    private boolean connected = true;

    /**
     * After this number of packets is lost the client will be considered disconnected
     */
    private final int maxLostPongs = 5;
    private int receivedPongsCounts = maxLostPongs;

    private int counter = 0;

    /**
     * After this amount of milliseconds the timer checks for a received pong and sends a ping <br>
     *     timerTaskDelta * maxLostPings = total time that has to elapse before disconnecting the player
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
                        userSend(ping);
                        System.out.println(nickname + " Ping sent: " + counter);
                        receivedPongsCounts--;
                        counter++;

                    } else {
                        connected = false;
                        System.out.println(nickname + " disconnected!");

                        for (ObserverViewIO obs:serverAreas) {
                            obs.onDisconnect(getThis());
                        }

                        userSend(inactivity);
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
        for (ServerArea serverArea: serverAreas) {
            if (serverArea.getAreaStatus() == command.getWhereToProcess())
                serverArea.update(message, command, nickname);
        }
    }

    /**
     * Updates the user when a Pong is successfully received.
     */
    public void pongReceived(){
        receivedPongsCounts = maxLostPongs;
        //System.out.println("PONG RECEIVED IN USER: " + nickname);
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

    public int getCounter() { return counter; }

    //------------------------------------------------------------------------------------------------------------------
}
