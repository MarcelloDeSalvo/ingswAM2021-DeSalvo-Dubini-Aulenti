package it.polimi.ingsw.network.commands;

public class ManageDepositMessage extends Message{
    private final int qty;
    private final int sourceID;
    private final int destinationID;

    /**
     * Message from a player. Used to inform the server that the player wants to manage the deposit
     * @param qty amount of resources to move
     * @param sourceID source Deposit ID
     * @param destinationID destination Deposit ID
     * @param senderNick player performing the action
     */
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
