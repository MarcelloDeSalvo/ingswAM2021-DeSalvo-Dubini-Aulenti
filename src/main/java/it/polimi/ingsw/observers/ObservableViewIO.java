package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Message;

import java.net.Socket;

public interface ObservableViewIO {

    void addObserverIO(ObserverViewIO observer);
    void notifyIO_unicast(Message message, Socket socket);
    void notifyIO_broadcast(Message message);

    boolean readInput();

}
