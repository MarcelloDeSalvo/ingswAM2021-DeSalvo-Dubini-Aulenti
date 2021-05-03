package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Message;

public interface ObservableThread {
     void addThreadObserver(ObserverViewIO observer);
     void notifyThreadObserver(Message message);
}
