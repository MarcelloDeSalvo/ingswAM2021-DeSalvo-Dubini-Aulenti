package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.cards.ProductionAbility;

public class NewProductionSlotMessage extends Message {
    private final ProductionAbility productionAbility;

    /**
     * Message from the server. Used to inform every player that someone added a new Production Slot.
     * @param productionAbility Production Ability of the new Production Slot
     */
    public NewProductionSlotMessage(Message.MessageBuilder messageBuilder,  ProductionAbility productionAbility) {
        super(messageBuilder);
        this.productionAbility = productionAbility;
    }

    public ProductionAbility getProductionAbility() {
        return productionAbility;
    }
}
