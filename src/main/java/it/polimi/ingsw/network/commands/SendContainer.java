package it.polimi.ingsw.network.commands;


import it.polimi.ingsw.model.resources.ResourceContainer;

public class SendContainer extends Message{

    ResourceContainer container;

    String destination; //Vault or Deposit
    int destinationID;

    public SendContainer(Command command, ResourceContainer container, String destination, int destinationID, String senderNickname) {
        super( new MessageBuilder().setCommand(Command.SEND_CONTAINER).setNickname(senderNickname));
        this.container = container;
        this.destination = destination;
        this.destinationID = destinationID;
    }

    public SendContainer(ResourceContainer container, String destination, String senderNickname) {
        super( new MessageBuilder().setCommand(Command.SEND_CONTAINER).setNickname(senderNickname));
        this.container = container;
        this.destination = destination;
    }

    public ResourceContainer getContainer() {
        return container;
    }

    public String getDestination() {
        return destination;
    }

    public int getDestinationID() {
        return destinationID;
    }

    @Override
    public String toString() {
        return "SendContainer{" +
                "container=" + container +
                ", destination='" + destination + '\'' +
                ", destinationID=" + destinationID +
                '}';
    }
}
