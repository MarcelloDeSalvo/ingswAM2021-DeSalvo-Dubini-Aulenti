package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiver extends Thread{
    private final Socket socket;
    ClientView view;

    public ClientReceiver (Socket socket, ClientView view){
        this.socket = socket;
        this.view = view;
    }

    public void run() {
        Gson gson = new Gson();

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String receivedMex = "";
            while ((receivedMex = in.readLine())!=null) {
                view.readUpdates(receivedMex);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e){
            System.out.println("Wrong syntax, the message has been discarded");
            e.printStackTrace();
        }
    }

}
