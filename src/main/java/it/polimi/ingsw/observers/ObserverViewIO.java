package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Message;

import java.net.Socket;

public interface ObserverViewIO {
    void update(Message message, Socket socket);
    void update(Message message);
    Socket getSocket();
}
