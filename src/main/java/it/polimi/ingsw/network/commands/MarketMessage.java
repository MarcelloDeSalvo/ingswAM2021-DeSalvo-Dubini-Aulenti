package it.polimi.ingsw.network.commands;

public class MarketMessage extends Message{
    private int row;
    private int column;

    public MarketMessage( int row, int column, String nickName) {
        super(new MessageBuilder().setNickname(nickName).setCommand(Command.PICK_FROM_MARKET));
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
