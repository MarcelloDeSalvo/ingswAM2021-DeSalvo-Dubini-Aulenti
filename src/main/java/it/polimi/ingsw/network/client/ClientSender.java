package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObserverViewIO;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSender extends Thread {
    private Socket serverSocket;
    private PrintWriter out;
    private View view;

    public ClientSender(Socket serverSocket, View view){
        this.serverSocket = serverSocket;
        this.out = null;
        this.view = view;
    }

    public void run(){
        try{
            out = new PrintWriter(serverSocket.getOutputStream(), true);

            while (true){
                if (!view.readInput())
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //passa al server il comando
    public void send(Message mex){
        String stringToSend = mex.serialize();
        out.println(stringToSend);
        out.flush();
    }

}
