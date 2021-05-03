package it.polimi.ingsw.network.commands;

public class DiscardLeaderMessage extends Message{
    int leaderID;

    public DiscardLeaderMessage(int leaderID) {
        super(Command.DISCARD_LEADER);
        this.leaderID = leaderID;
    }

    public DiscardLeaderMessage(String info, Target target, int leaderID) {
        super(Command.DISCARD_LEADER, info, target);
        this.leaderID = leaderID;
    }

    public DiscardLeaderMessage(String info, Target target, String senderNickname, int leaderID) {
        super(Command.DISCARD_LEADER, info, target, senderNickname);
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
