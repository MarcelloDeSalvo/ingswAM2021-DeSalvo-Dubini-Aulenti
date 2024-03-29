package it.polimi.ingsw.network.commands;

public class IdMessage extends Message{
    private final int id;

    /**
     * Message used by both Server and Players. Used to send an ID (int) through the network
     */
    public IdMessage(Command command, int id, String senderNickname) {
        super(new MessageBuilder().setNickname(senderNickname).setCommand(command));
        this.id = id;
    }

    public IdMessage(MessageBuilder messageBuilder, int id){
        super(messageBuilder);
        this.id=id;
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
