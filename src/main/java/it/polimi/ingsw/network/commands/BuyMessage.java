package it.polimi.ingsw.network.commands;

public class BuyMessage extends Message{
    private final int cardID;
    private final int productionSlotID;

    public BuyMessage(Command command, int cardID, int productionSlotID, String senderNick) {
        super(new Message.MessageBuilder().setNickname(senderNick).setCommand(command).setTarget(Target.BROADCAST));
        this.cardID = cardID;
        this.productionSlotID = productionSlotID;
    }

    public BuyMessage(int cardID, int productionSlotID, String senderNick) {
        super(new Message.MessageBuilder().setNickname(senderNick).setCommand(Command.BUY));
        this.cardID = cardID;
        this.productionSlotID = productionSlotID;
    }

    public int getCardID() {
        return cardID;
    }

    public int getProductionSlotID() {
        return productionSlotID;
    }
}
