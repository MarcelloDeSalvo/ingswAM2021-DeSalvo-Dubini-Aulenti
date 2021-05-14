package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSender extends Thread {

    private final Socket socket;
    private PrintWriter out;

    public ServerSender (Socket socket, PrintWriter out){
        this.socket = socket;
        this.out = out;
    }

    public void run(){
        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            //Muore subito?

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * Sends a message to the ClientReceiver
     */
    public void send(String mex){
        //passa al server il comando
        out.println(mex);
        out.flush();
    }

    public void exit(){
        out.close();
    }

}

