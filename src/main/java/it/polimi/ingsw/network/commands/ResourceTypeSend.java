package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceType;

public class ResourceTypeSend extends Message{
    private ResourceType resourceType;

    public ResourceTypeSend(Command command, ResourceType resourceType, String senderNickname) {
        super(new MessageBuilder().setNickname(senderNickname).setCommand(command));
        this.resourceType = resourceType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}
