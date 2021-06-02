package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceContainer;

import java.util.ArrayList;

public class ContainerArrayListMessage extends Message {
    private ArrayList<ResourceContainer> containers;

    public ContainerArrayListMessage(Command command,ArrayList<ResourceContainer> containers, String senderNick) {
        super(new Message.MessageBuilder().setCommand(command).setNickname(senderNick));
        this.containers = containers;
    }

    public ArrayList<ResourceContainer> getContainers() {
        return containers;
    }
}
