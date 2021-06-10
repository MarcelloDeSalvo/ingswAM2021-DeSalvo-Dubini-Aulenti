package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;

public class AskConversionMessage extends Message{

    ResourceType convertedType;
    ArrayList<ResourceType> availableConversions;
    int numToConvert;

    public AskConversionMessage(MessageBuilder messageBuilder, ResourceType convertedType, ArrayList<ResourceType> availableConversions, int numToConvert) {
        super(messageBuilder);
        this.convertedType = convertedType;
        this.availableConversions = availableConversions;
        this.numToConvert = numToConvert;
    }

    public ResourceType getConvertedType() {
        return convertedType;
    }

    public ArrayList<ResourceType> getAvailableConversions() {
        return availableConversions;
    }

    public int getNumToConvert() {
        return numToConvert;
    }
}
