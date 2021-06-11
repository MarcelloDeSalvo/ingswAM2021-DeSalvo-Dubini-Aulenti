package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceType;

public class NewDepositMessage extends Message{
    private final int maxDim;
    private final ResourceType resourceType;

    /**
     * Message from the server. Used to inform every player that someone added a new Deposit Slot.
     * @param maxDim deposit's max dimension
     * @param resourceType Resource Type that the deposit can store
     */
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
