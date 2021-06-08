package it.polimi.ingsw.network.server;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.observers.ObservableThread;
import it.polimi.ingsw.observers.ObserverThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerReceiver implements ObservableThread {

    private final ArrayList<ObserverThread> observerThreads;
    private final BufferedReader in;
    private boolean exit = false;


    public ServerReceiver (BufferedReader in){
        observerThreads = new ArrayList<>();
        this.in = in;
    /*
        try {

            String receivedMex = "";

            while ((receivedMex = in.readLine()) != null && !exit) {
                notifyThreadObserver(receivedMex);
            }

            in.close();

        } catch (IOException e) {
            System.out.println("A user logged out");
            //e.printStackTrace();

        } catch (JsonSyntaxException e){
            System.out.println("Wrong syntax, the message has been discarded");
            e.printStackTrace();
        }

     */
    }


    @Override
    public void addThreadObserver(ObserverThread observer) {
         /*
        if(observer!=null)
            observerThreads.add(observer);*/
    }


    @Override
    public void notifyThreadObserver(String message) {
        /*
        for (ObserverThread obs: observerThreads) {
            obs.somethingHasBeenReceived(message);
        }*/
    }

    /**
     * Kills the thread when called
     */
    public void exit(){
        exit = true;
    }

}
