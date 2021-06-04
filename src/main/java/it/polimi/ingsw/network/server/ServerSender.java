package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSender{

    private final Socket socket;
    private PrintWriter out;

    public ServerSender (Socket socket, PrintWriter out){
        this.socket = socket;
        this.out = out;
    }

    /**
     * Sends a message to the ClientReceiver
     */
    public synchronized void send(String mex){
        //sends the message to the server
        out.println(mex);
        out.flush();
    }

    public void exit(){
        out.close();
    }

}

