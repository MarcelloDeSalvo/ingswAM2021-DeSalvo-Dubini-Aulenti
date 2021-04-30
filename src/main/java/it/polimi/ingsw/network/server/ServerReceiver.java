package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.view.VirtualView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReceiver extends Thread {

    Socket socket;
    VirtualView virtualView;

    public ServerReceiver (Socket socket, VirtualView virtualView){
        this.socket = socket;
        this.virtualView = virtualView;
    }

    @Override
    public void run() {
        Gson gson = new Gson();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String receivedMex = "";

            while ((receivedMex = in.readLine()) != null) {
                Message deserializedMex = gson.fromJson(receivedMex, Message.class);
                virtualView.update(deserializedMex);
            }

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e){
            System.out.println("Wrong syntax, the message has been discarded");
            e.printStackTrace();
        }

    }



}
