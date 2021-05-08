package it.polimi.ingsw.network.client;

import it.polimi.ingsw.observers.ObserverController;
import it.polimi.ingsw.view.ClientView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSender extends Thread implements ObserverController {
    private Socket serverSocket;
    private PrintWriter out;
    private ClientView view;

    public ClientSender(Socket serverSocket, ClientView view){
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

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String mex) {
        out.println(mex);
        out.flush();
    }

}
