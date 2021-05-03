package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Message;

public interface ObserverViewIO {

    void update(Message message);
}
