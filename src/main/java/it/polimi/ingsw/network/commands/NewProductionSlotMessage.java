package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.cards.ProductionAbility;

public class NewProductionSlotMessage extends Message {
    ProductionAbility productionAbility;

    public NewProductionSlotMessage(Message.MessageBuilder messageBuilder,  ProductionAbility productionAbility) {
        super(messageBuilder);
        this.productionAbility = productionAbility;
    }

    public ProductionAbility getProductionAbility() {
        return productionAbility;
    }
}
