package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceType;

public class NewDepositMessage extends Message{
    int maxDim;
    ResourceType resourceType;

    public NewDepositMessage(MessageBuilder messageBuilder, int maxDim, ResourceType resourceType) {
        super(messageBuilder);
        this.maxDim = maxDim;
        this.resourceType = resourceType;
    }

    public int getMaxDim() {
        return maxDim;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}
