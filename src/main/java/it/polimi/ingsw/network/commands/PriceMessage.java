package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.HashMap;

public class PriceMessage extends Message{

    private final HashMap<ResourceType, ResourceContainer> price;

    public PriceMessage(Command command, HashMap<ResourceType, ResourceContainer> price, String senderNick) {
        super(new Message.MessageBuilder().setNickname(senderNick).setCommand(command));
        this.price = price;
    }

    public PriceMessage(HashMap<ResourceType, ResourceContainer> price, String senderNick) {
        super(new Message.MessageBuilder().setNickname(senderNick).setCommand(Command.PRODUCTION_PRICE));
        this.price = price;
    }

    public HashMap<ResourceType, ResourceContainer> getPrice() {
        return price;
    }
}
