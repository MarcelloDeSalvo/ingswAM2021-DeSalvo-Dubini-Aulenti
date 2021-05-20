package it.polimi.ingsw.network.commands;

public class FaithPathUpdateMessage extends Message{
    private final int qty;

    public FaithPathUpdateMessage(Command command, int qty, String senderNickname) {
        super(new MessageBuilder().setNickname(senderNickname).setTarget(Target.BROADCAST).setCommand(command));
        this.qty = qty;
    }

    public int getId() {
        return qty;
    }

}
