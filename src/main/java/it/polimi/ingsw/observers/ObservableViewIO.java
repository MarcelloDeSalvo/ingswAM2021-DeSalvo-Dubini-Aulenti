package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Message;

public interface ObservableViewIO {

    void addObserverIO(ObserverViewIO observer);
    void notifyIO(Message message);
    boolean readInput();

}
