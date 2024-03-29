package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceType;
import java.util.List;

public class AskConversionMessage extends Message{

    private final ResourceType convertedType;
    private final List<ResourceType> availableConversions;
    private final int numToConvert;

    /**
     * Reply from the server. Notifies a player that he have to choose between multiple resource types for a conversion
     * @param convertedType ResourceType to convert
     * @param availableConversions ArrayList of available conversions
     * @param numToConvert amount of resources that needs to be converted
     */
    public AskConversionMessage(MessageBuilder messageBuilder, ResourceType convertedType, List<ResourceType> availableConversions, int numToConvert) {
        super(messageBuilder);
        this.convertedType = convertedType;
        this.availableConversions = availableConversions;
        this.numToConvert = numToConvert;
    }

    public ResourceType getConvertedType() {
        return convertedType;
    }

    public List<ResourceType> getAvailableConversions() {
        return availableConversions;
    }

    public int getNumToConvert() {
        return numToConvert;
    }
}
