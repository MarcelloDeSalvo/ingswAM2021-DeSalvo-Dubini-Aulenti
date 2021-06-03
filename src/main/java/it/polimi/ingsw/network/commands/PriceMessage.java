package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;

public class PriceMessage extends Message{

    private final ArrayList<ResourceContainer> price;

    public PriceMessage(Command command, ArrayList<ResourceContainer> price, String senderNick) {
        super(new Message.MessageBuilder().setNickname(senderNick).setCommand(command));
        this.price = price;
    }

    public PriceMessage(ArrayList<ResourceContainer> price, String senderNick) {
        super(new Message.MessageBuilder().setNickname(senderNick).setCommand(Command.PRODUCTION_PRICE));
        this.price = price;
    }

    public ArrayList<ResourceContainer> getPrice() {
        return price;
    }
}
