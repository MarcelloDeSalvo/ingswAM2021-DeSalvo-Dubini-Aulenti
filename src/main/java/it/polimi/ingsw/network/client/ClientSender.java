package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObserverViewIO;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSender extends Thread implements ObserverViewIO {
    private Socket serverSocket;
    private PrintWriter out;
    private View view;

    public ClientSender(Socket serverSocket, View view){
        this.serverSocket = serverSocket;
        this.out = null;
        this.view = view;
        view.addObserverIO(this);

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

    @Override
    public void update(Message mex){
        //da serializzare
        String stringToSend = mex.serialize();
        send(stringToSend);
    }

    public void send(String mex){
        //passa al server il comando
        out.println(mex);
        out.flush();
    }

}
