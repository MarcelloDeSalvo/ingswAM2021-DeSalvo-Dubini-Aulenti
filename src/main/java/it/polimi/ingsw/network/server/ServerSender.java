package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObserverViewIO;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSender extends Thread implements ObserverViewIO {

    Socket socket;
    VirtualView virtualView;
    PrintWriter out;

    public ServerSender (Socket socket, VirtualView virtualView){
        this.socket = socket;
        this.virtualView = virtualView;
        out = null;
        virtualView.addObserverIO(this);
    }

    public void run(){
        try{
            out = new PrintWriter(socket.getOutputStream(), true);

            while (true){
                if (!virtualView.readInput())
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

