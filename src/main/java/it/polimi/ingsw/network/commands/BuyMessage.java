package it.polimi.ingsw.network.commands;

public class BuyMessage extends Message{
    private final int cardID;
    private final int productionSlotID;

    /**
     * Reply from the server. Used to notify that a card has been bought.
     */
    public BuyMessage(Command command, int cardID, int productionSlotID, String senderNick) {
        super(new Message.MessageBuilder().setNickname(senderNick).setCommand(command).setTarget(Target.BROADCAST));
        this.cardID = cardID;
        this.productionSlotID = productionSlotID;
    }

    /**
     * Message from a player. Used to inform the server that the player wants to buy a development card.
     * @param cardID id of the card that i want to buy
     * @param productionSlotID id of production slot where i want to put the card
     * @param senderNick player performing the action
     */
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
