package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Message;

public interface ObservableViewIO {

    void notifyLobbyOrView(String message);
    void addLobbyOrView(ObserverViewIO obs);
}
