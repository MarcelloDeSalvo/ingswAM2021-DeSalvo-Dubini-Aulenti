package it.polimi.ingsw.network.commands;

public class NotifyCardGrid extends Message{
    private final int oldID;
    private final int newID;

    /**
     * Message from the server. Used to notify every player about changes in the Card Grid.
     * @param oldID removed Development Card's ID
     * @param newID new Development Card's ID
     */
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
