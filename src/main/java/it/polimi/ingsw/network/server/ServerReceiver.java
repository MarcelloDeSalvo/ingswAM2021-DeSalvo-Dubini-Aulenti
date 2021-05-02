package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObservableViewIO;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ServerReceiver extends Thread implements ObservableViewIO {

    Socket socket;
    ArrayList<ObserverViewIO> observerViewIOS;

    public ServerReceiver (Socket socket){
        this.socket = socket;
        observerViewIOS = new ArrayList<>();
    }

    @Override
    public void run() {
        Gson gson = new Gson();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String receivedMex = "";

            while ((receivedMex = in.readLine()) != null) {
                Message deserializedMex = gson.fromJson(receivedMex, Message.class);
                notifyIO_broadcast(deserializedMex);
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
    public void addObserverIO(ObserverViewIO observer) {
        if(observer!=null)
            observerViewIOS.add(observer);
    }

    @Override
    public void notifyIO_unicast(Message message, Socket socket) {

    }

    @Override
    public void notifyIO_broadcast(Message message) {
        for (ObserverViewIO obs: observerViewIOS) {
            obs.update(message, socket);
        }

    }

    @Override
    public boolean readInput() {
        return false;
    }
}
