package it.polimi.ingsw.network.commands;

public class MarketMessage extends Message{
    private final String selection;
    private final int num;

    public MarketMessage(String selection, int num, String nickName) {
        super(new MessageBuilder().setNickname(nickName).setCommand(Command.PICK_FROM_MARKET));
        this.selection = selection;
        this.num = num;
    }

    public String getSelection() {
        return selection;
    }

    public int getNum() {
        return num;
    }
}
