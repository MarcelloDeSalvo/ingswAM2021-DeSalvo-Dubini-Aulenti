package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceContainer;

import java.util.List;

public class ContainerArrayListMessage extends Message {
    private final List<ResourceContainer> containers;

    /**
     * Message used for sending an ArrayList of ResourceContainers through the network
     */
    public ContainerArrayListMessage(Command command, List<ResourceContainer> containers, String senderNick) {
        super(new Message.MessageBuilder().setCommand(command).setNickname(senderNick));
        this.containers = containers;
    }

    public List<ResourceContainer> getContainers() {
        return containers;
    }
}
