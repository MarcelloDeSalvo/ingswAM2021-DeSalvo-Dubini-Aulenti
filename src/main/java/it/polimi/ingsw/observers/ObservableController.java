package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Message;

public interface ObservableController {

    void addObserverController(ObserverController observerController);
    void notifyController(Message message);

}
