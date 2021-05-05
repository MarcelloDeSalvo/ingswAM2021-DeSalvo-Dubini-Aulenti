package it.polimi.ingsw.network.commands;

public class DiscardLeaderMessage extends Message{
    int leaderID;


    public DiscardLeaderMessage(int leaderID, String senderNickname) {
        super(new MessageBuilder().setNickname(senderNickname).setCommand(Command.DISCARD_LEADER));
        this.leaderID = leaderID;
    }

    public int getLeaderID() {
        return leaderID;
    }

    @Override
    public String toString() {
        return "DiscardLeaderMessage{" +
                "leaderID='" + leaderID + '\'' +
                "} " + super.toString();
    }
}
