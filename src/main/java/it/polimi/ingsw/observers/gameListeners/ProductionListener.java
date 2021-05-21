package it.polimi.ingsw.observers.gameListeners;

import it.polimi.ingsw.model.cards.ProductionAbility;

public interface ProductionListener {
    void notifyNewProductionSlot(ProductionAbility productionAbility, String senderNick);
}
