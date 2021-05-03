package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObserverViewIO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSender extends Thread implements ObserverViewIO {

    Socket socket;
    PrintWriter out;

    public ServerSender (Socket socket){
        this.socket = socket;
        out = null;
    }

    public void run(){
        try{
            out = new PrintWriter(socket.getOutputStream(), true);

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

