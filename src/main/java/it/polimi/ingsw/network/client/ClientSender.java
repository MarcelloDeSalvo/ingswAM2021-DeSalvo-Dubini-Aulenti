package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.observers.ObserverController;
import it.polimi.ingsw.view.ClientView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSender extends Thread implements ObserverController {
    private final Socket serverSocket;
    private PrintWriter out;
    private final ClientView view;

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
            System.out.println("Disconnected");
        }

    }

    @Override
    public synchronized void update(String mex, Command command, String senderNick) {
        out.println(mex);
        out.flush();
    }

}
