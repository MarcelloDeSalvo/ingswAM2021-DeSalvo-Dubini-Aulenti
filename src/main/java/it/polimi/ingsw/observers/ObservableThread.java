package it.polimi.ingsw.observers;

public interface ObservableThread {
     void addThreadObserver(ObserverThread observer);
     void notifyThreadObserver(String message);
}
