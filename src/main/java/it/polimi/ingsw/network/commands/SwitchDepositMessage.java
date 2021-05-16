package it.polimi.ingsw.network.commands;

public class SwitchDepositMessage extends Message {
    private final int sourceID;
    private final int destinationID;

    public SwitchDepositMessage( int sourceID, int destinationID, String senderNick) {
        super(new Message.MessageBuilder().setCommand(Command.SWITCH_DEPOSIT).setNickname(senderNick));
        this.sourceID = sourceID;
        this.destinationID = destinationID;
    }

    public int getSourceID() {
        return sourceID;
    }

    public int getDestinationID() {
        return destinationID;
    }
}


