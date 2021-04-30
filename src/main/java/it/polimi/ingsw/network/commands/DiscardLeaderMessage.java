package it.polimi.ingsw.network.commands;

public class DiscardLeaderMessage extends Message{
    String leaderID;

    public DiscardLeaderMessage(String leaderID) {
        super(Command.DISCARD_LEADER);
        this.leaderID = leaderID;
    }

    public String getLeaderID() {
        return leaderID;
    }

    @Override
    public String toString() {
        return "DiscardLeaderMessage{" +
                "leaderID='" + leaderID + '\'' +
                "} " + super.toString();
    }
}
