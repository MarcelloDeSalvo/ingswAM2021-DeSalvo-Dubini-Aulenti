package it.polimi.ingsw.observers;

public interface ObservableThread {

     /**
      * Adds a thread to an observer list <br>
      * Used to manage the communication between the User and his ServerSender thread (server-side) <br>
      * @param observer
      */
     void addThreadObserver(ObserverThread observer);

     /**
      * Notifies all the listening threads
      */
     void notifyThreadObserver(String message);
}
