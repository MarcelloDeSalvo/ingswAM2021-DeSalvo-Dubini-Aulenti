package it.polimi.ingsw.network.server;

import java.io.PrintWriter;

public class ServerSender{

    private final PrintWriter out;

    public ServerSender (PrintWriter out){
        this.out = out;
    }

    /**
     * Sends a message to the ClientReceiver
     */
    public synchronized void send(String mex){
        out.println(mex);
        out.flush();
    }

    public void exit(){
        out.close();
    }

}

