package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Message;

public interface ObservableViewIO {

    /**
     * Notifies all the lobbies or the views (they are treated the same way) that are listening for an update
     */
    void notifyLobbyOrView(String message);

    /**
     * adds a lobby or a view to the observers list
     */
    void addLobbyOrView(ObserverViewIO obs);
}
