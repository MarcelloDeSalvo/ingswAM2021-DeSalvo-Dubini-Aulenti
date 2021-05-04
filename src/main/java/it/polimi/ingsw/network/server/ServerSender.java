package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSender extends Thread {

    Socket socket;
    PrintWriter out;

    public ServerSender (Socket socket, PrintWriter out){
        this.socket = socket;
        this.out = out;
    }

    public void run(){
        try{
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String mex){
        //passa al server il comando
        out.println(mex);
        out.flush();
    }

}

