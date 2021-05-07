package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObservableThread;
import it.polimi.ingsw.observers.ObserverThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerReceiver extends Thread implements ObservableThread {

    private Socket socket;
    private ArrayList<ObserverThread> observerThreads;
    private BufferedReader in;
    private boolean exit = false;


    public ServerReceiver (Socket socket, BufferedReader in){
        this.socket = socket;
        observerThreads = new ArrayList<>();
        this.in = in;
    }

    @Override
    public void run() {

        try {

            String receivedMex = "";

            while ((receivedMex = in.readLine()) != null || !exit) {
                notifyThreadObserver(receivedMex);
            }

            in.close();

        } catch (IOException e) {
            //System.out.println("A user logged out");
            //e.printStackTrace();

        } catch (JsonSyntaxException e){
            System.out.println("Wrong syntax, the message has been discarded");
            e.printStackTrace();
        }

    }

    @Override
    public void addThreadObserver(ObserverThread observer) {
        if(observer!=null)
            observerThreads.add(observer);
    }


    @Override
    public void notifyThreadObserver(String message) {
        for (ObserverThread obs: observerThreads) {
            obs.userReceive(message);
        }

    }

    public void exit(){
        exit = true;
    }

}
