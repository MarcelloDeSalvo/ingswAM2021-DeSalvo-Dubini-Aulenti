package it.polimi.ingsw.network.commands;

public class BuyMessage extends Message{
    private final int row;
    private final int column;

    private final int productionSlotID;

    public BuyMessage(int row, int column, int productionSlotID, String senderNick) {
        super(new Message.MessageBuilder().setNickname(senderNick).setCommand(Command.BUY));
        this.row = row;
        this.column = column;
        this.productionSlotID = productionSlotID;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getProductionSlotID() {
        return productionSlotID;
    }
}
