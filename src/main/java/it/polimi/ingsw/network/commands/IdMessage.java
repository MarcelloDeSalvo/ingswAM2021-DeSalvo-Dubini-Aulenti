package it.polimi.ingsw.network.commands;

public class IdMessage extends Message{
    private final int id;

    public IdMessage(Command command, int id, String senderNickname) {
        super(new MessageBuilder().setNickname(senderNickname).setCommand(command));
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "IdMessage{" +
                "leaderID='" + id + '\'' +
                "} " + super.toString();
    }
}
