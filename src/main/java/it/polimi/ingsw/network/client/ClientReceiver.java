package it.polimi.ingsw.network.client;

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

public class ClientReceiver extends Thread implements ObservableViewIO {
    private final Socket socket;
    ArrayList<ObserverViewIO> observerViewIOS;

    public ClientReceiver (Socket socket){
        this.socket = socket;
        observerViewIOS = new ArrayList<>();
    }

    public void run() {
        Gson gson = new Gson();

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String receivedMex = "";
            while ((receivedMex = in.readLine())!=null) {
                Message deserializedMex = gson.fromJson(receivedMex, Message.class);
                notifyIO(deserializedMex);
            }

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
    public void notifyIO(Message message) {
        for (ObserverViewIO observerViewIO: observerViewIOS) {
            observerViewIO.update(message);
        }
    }

    @Override
    public boolean readInput() {
        return false;
    }
}
