package it.polimi.ingsw.observers.gameListeners;

import it.polimi.ingsw.model.cards.Colour;

public interface ActionTokenListener {
    /**
     * Notifies that Lorenzo did an action
     * @param actionID Action's ID
     * @param colour colour of the card removed by Lorenzo or null if the action was an increment position
     */
    void notifyLorenzoAction(int actionID, Colour colour);
}
