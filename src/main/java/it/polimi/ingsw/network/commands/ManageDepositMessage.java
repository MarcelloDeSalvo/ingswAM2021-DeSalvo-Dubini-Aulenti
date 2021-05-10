package it.polimi.ingsw.network.commands;

public class ManageDepositMessage extends Message{
    private final int qty;
    private final int sourceID;
    private final int destinationID;

    public ManageDepositMessage(int qty, int sourceID, int destinationID, String senderNick) {
        super(new MessageBuilder().setCommand(Command.MANAGE_DEPOSIT).setNickname(senderNick));
        this.qty = qty;
        this.sourceID = sourceID;
        this.destinationID = destinationID;
    }

    public int getQty() {
        return qty;
    }

    public int getSourceID() {
        return sourceID;
    }

    public int getDestinationID() {
        return destinationID;
    }
}
