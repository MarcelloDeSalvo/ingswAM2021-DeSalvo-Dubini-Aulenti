package it.polimi.ingsw.network.commands;

public class MarketMessage extends Message{
    private final String selection;
    private final int num;

    /**
     * Message from a player. Used to inform the server that a player wants to select something from the Market.
     * @param selection "ROW" or "COLUMN"
     * @param num row/column ID
     * @param nickname player performing the action
     */
    public MarketMessage(String selection, int num, String nickname) {
        super(new MessageBuilder().setNickname(nickname).setCommand(Command.PICK_FROM_MARKET).setTarget(Target.BROADCAST));
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
