package it.polimi.ingsw.observers.gameListeners;

import it.polimi.ingsw.model.cards.ProductionAbility;

public interface ProductionListener {
    /**
     * Notifies that a new Production Slot has been added
     * @param productionAbility the new Production Slot's production ability
     */
    void notifyNewProductionSlot(ProductionAbility productionAbility, String senderNick);
}
