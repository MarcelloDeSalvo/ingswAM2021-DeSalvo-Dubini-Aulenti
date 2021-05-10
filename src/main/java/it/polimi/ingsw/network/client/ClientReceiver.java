package it.polimi.ingsw.network.client;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiver extends Thread{
    private final Socket socket;
    private final ClientView view;
    private boolean exit;

    public ClientReceiver (Socket socket, ClientView view){
        this.socket = socket;
        this.view = view;
    }

    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String receivedMex = "";

            while ((receivedMex = in.readLine())!=null || !exit) {
                view.readUpdates(receivedMex);
            }

            in.close();

        } catch (IOException e) {
            System.out.println("Disconnected");

        } catch (JsonSyntaxException e){
            System.out.println("Wrong syntax, the message has been discarded");
            e.printStackTrace();

        }
    }

    /**
     * Kills the thread when called
     */
    public void exit(){
        exit= true;
    }

}
