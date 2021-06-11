package it.polimi.ingsw.network.commands;


import it.polimi.ingsw.model.resources.ResourceContainer;

public class SendContainer extends Message{
    private final ResourceContainer container;
    private final String destination; //Vault or Deposit
    private int destinationID;
    private boolean added;

    /**
     * Notifies the clients that a container is added or removed inside a Vault
     */
    public SendContainer(Command command, ResourceContainer container, String sendNickname, boolean add) {
        super(new MessageBuilder().setTarget(Target.BROADCAST).setNickname(sendNickname).setCommand(command));
        this.container = container;
        this.added = add;
        this.destination = "VAULT";
    }

    /**
     * Notifies the clients that a container is added or removed inside a Deposit
     */
    public SendContainer(Command command, ResourceContainer container, int destinationID, String senderNickname, boolean added) {
        super(new MessageBuilder().setCommand(command).setTarget(Target.BROADCAST).setNickname(senderNickname));
        this.container = container;
        this.destination = "DEPOSIT";
        this.destinationID = destinationID;
        this.added = added;
    }

    /**
     * Notifies the server that a player sent a ResourceContainer to a Deposit
     */
    public SendContainer(Command command, ResourceContainer container, String destination, int destinationID, String senderNickname) {
        super(new MessageBuilder().setCommand(command).setNickname(senderNickname));
        this.container = container;
        this.destination = destination;
        this.destinationID = destinationID;
    }

    /**
     * Notifies the server that a player sent a ResourceContainer to a Vault
     */
    public SendContainer(ResourceContainer container, String destination, String senderNickname) {
        super(new MessageBuilder().setCommand(Command.SEND_CONTAINER).setNickname(senderNickname));
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

    public boolean isAdded() { return added; }

    @Override
    public String toString() {
        return "SendContainer{" +
                "container=" + container +
                ", destination='" + destination + '\'' +
                ", destinationID=" + destinationID +
                '}';
    }
}
