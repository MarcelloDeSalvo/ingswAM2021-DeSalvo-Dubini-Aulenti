package it.polimi.ingsw.network.commands;

public class NotifyCardGrid extends Message{

    int oldID;
    int newID;

    public NotifyCardGrid(int oldID, int newID) {
        super(new MessageBuilder().setTarget(Target.BROADCAST).setCommand(Command.NOTIFY_CARDGRID));
        this.oldID = oldID;
        this.newID = newID;
    }

    public int getOldID() {
        return oldID;
    }

    public int getNewID() {
        return newID;
    }
}
