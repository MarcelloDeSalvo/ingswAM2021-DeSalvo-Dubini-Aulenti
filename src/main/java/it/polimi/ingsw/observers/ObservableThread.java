package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Message;

public interface ObservableThread {
     void addThreadObserver(ObserverThread observer);
     void notifyThreadObserver(Message message);
}
