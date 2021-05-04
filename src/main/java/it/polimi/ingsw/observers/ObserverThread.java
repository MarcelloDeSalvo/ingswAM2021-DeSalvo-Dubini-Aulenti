package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Message;

public interface ObserverThread {
    void userReceive(Message message);
}
