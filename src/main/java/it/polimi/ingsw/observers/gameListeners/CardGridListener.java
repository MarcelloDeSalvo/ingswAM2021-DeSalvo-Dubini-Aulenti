package it.polimi.ingsw.observers.gameListeners;

import it.polimi.ingsw.model.cards.Colour;

public interface CardGridListener {
    void notifyCardRemoved(int amount, Colour color, int level);
    void notifyCardGridChanges(int oldID, int newID);
}
