package it.polimi.ingsw.network.commands;


import it.polimi.ingsw.model.resources.ResourceType;

public class ContainerMessage extends Message{
    //ResourceContainer container;
    //TESTARE CON CONTAINER-------------------------------
    int qty;
    ResourceType resourceType; //sending a whole container, instead of qty and resourceType, is also an option
    String source; //Vault or Deposit
    int depositID;

    public ContainerMessage(int qty, ResourceType resourceType, String source, int depositID) {
        super(Command.SEND_CONTAINER);
        this.qty = qty;
        this.resourceType = resourceType;
        this.source = source;
        this.depositID = depositID;
    }

    public ContainerMessage(Command command, String info, Target target, int qty, ResourceType resourceType, String source, int depositID) {
        super(Command.SEND_CONTAINER, info, target);
        this.qty = qty;
        this.resourceType = resourceType;
        this.source = source;
        this.depositID = depositID;
    }

    public ContainerMessage(Command command, String info, Target target, String senderNickname, int qty, ResourceType resourceType, String source, int depositID) {
        super(Command.SEND_CONTAINER, info, target, senderNickname);
        this.qty = qty;
        this.resourceType = resourceType;
        this.source = source;
        this.depositID = depositID;
    }

    public int getQty() {
        return qty;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public String getSource() {
        return source;
    }

    public int getDepositID() {
        return depositID;
    }

    @Override
    public String toString() {
        return "ContainerMessage{" +
                "qty=" + qty +
                ", resourceType=" + resourceType +
                ", source='" + source + '\'' +
                ", depositID=" + depositID +
                "} " + super.toString();
    }
}
