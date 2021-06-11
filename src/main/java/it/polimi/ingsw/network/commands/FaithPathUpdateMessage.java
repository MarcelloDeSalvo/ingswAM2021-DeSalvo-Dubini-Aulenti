package it.polimi.ingsw.network.commands;

public class FaithPathUpdateMessage extends Message{
    private final int qty;

    /**
     * Message from the server. Used to notify every player about changes in the faithpath
     */
    public FaithPathUpdateMessage(Command command, int qty, String senderNickname) {
        super(new MessageBuilder().setNickname(senderNickname).setTarget(Target.BROADCAST).setCommand(command));
        this.qty = qty;
    }

    public int getId() {
        return qty;
    }

}
