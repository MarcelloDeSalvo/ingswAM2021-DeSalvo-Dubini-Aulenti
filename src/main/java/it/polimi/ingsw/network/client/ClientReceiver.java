package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiver extends Thread{
    private final Socket socket;
    private final ClientView view;
    private boolean exit = false;
    private ClientSender clientSender;

    public ClientReceiver (Socket socket, ClientView view, ClientSender clientSender){
        this.socket = socket;
        this.view = view;
        this.clientSender=clientSender;
    }

    public void run() {

        try {
            ClientCommandQueue clientCommandQueue= new ClientCommandQueue(view);
            clientCommandQueue.start();
            Gson gson=new Gson();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String receivedMex = "";

            while (!exit) {
                if((receivedMex = in.readLine()) != null) {
                    Message deserializedMex = gson.fromJson(receivedMex, Message.class);
                    Command command = deserializedMex.getCommand();
                    if(command==Command.PING){
                        clientSender.update(new Message.MessageBuilder().setCommand(Command.PONG).build().serialize(),null, null);
                    }
                    else {
                        clientCommandQueue.addCommandToQueue(receivedMex);
                    }
                }
                else
                    throw new IOException();
            }

            in.close();

        } catch (IOException e) {
            //e.printStackTrace();
            view.onDisconnected();
            System.out.println("Disconnected from the server");

        } catch (JsonSyntaxException e) {
            System.out.println("Wrong syntax, the message has been discarded");

        }
    }

    /**
     * Kills the thread when called
     */
    public void exit(){
        exit= true;
    }

}
