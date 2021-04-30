package it.polimi.ingsw.network.server;

import it.polimi.ingsw.view.VirtualView;

import java.net.Socket;


public class EchoServerClientHandler implements Runnable {
    private Socket socket;
    private VirtualView virtualView;

    public EchoServerClientHandler(Socket socket, VirtualView virtualView) {
        this.socket = socket;
        this.virtualView=virtualView;
    }

    public void run() {
        try {

            ServerReceiver serverReceiver = new ServerReceiver(socket, virtualView);
            serverReceiver.start();

            ServerSender serverSender = new ServerSender(socket, virtualView);
            serverSender.start();

        }catch (IllegalThreadStateException e){
            e.printStackTrace();
        }


    }
}
