package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObservableViewIO;
import it.polimi.ingsw.observers.ObserverViewIO;
import it.polimi.ingsw.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiver extends Thread{
    private final Socket socket;
    View view;

    public ClientReceiver (Socket socket, View view){
        this.socket = socket;
        this.view = view;
    }

    public void run() {
        Gson gson = new Gson();

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String receivedMex = "";
            while ((receivedMex = in.readLine())!=null) {
                Message deserializedMex = gson.fromJson(receivedMex, Message.class);
                view.readUpdates(deserializedMex);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e){
            System.out.println("Wrong syntax, the message has been discarded");
            e.printStackTrace();
        }
    }

}
