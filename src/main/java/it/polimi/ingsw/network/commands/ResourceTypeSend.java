package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;

public class ResourceTypeSend extends Message{
    private ResourceType resourceType;

    private ArrayList<ResourceType> resourceTypeArrayList;

    public ResourceTypeSend(Command command, ResourceType resourceType, String senderNickname) {
        super(new MessageBuilder().setNickname(senderNickname).setCommand(command));
        this.resourceType = resourceType;
    }

    public ResourceTypeSend(Command command, ArrayList<ResourceType> resourceTypeArrayList, String senderNickname) {
        super(new MessageBuilder().setNickname(senderNickname).setCommand(command));
        this.resourceTypeArrayList = new ArrayList<>(resourceTypeArrayList);
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public ArrayList<ResourceType> getResourceTypeArrayList() {
        return resourceTypeArrayList;
    }
}
