package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObservableThread;
import it.polimi.ingsw.observers.ObservableViewIO;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ServerReceiver extends Thread implements ObservableThread {

    Socket socket;
    ArrayList<ObserverViewIO> observerViewIOS;
    BufferedReader in;

    public ServerReceiver (Socket socket, BufferedReader in){
        this.socket = socket;
        observerViewIOS = new ArrayList<>();
        this.in=in;
    }

    @Override
    public void run() {
        Gson gson = new Gson();

        try {
            //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String receivedMex = "";

            while ((receivedMex = in.readLine()) != null) {
                Message deserializedMex = gson.fromJson(receivedMex, Message.class);
                notifyThreadObserver(deserializedMex);
            }

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e){
            System.out.println("Wrong syntax, the message has been discarded");
            e.printStackTrace();
        }

    }

    @Override
    public void addThreadObserver(ObserverViewIO observer) {
        if(observer!=null)
            observerViewIOS.add(observer);
    }


    @Override
    public void notifyThreadObserver(Message message) {
        for (ObserverViewIO obs: observerViewIOS) {
            obs.update(message);
        }

    }

}
