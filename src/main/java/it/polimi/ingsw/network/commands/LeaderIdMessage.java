package it.polimi.ingsw.network.commands;

public class LeaderIdMessage extends Message{
    int leaderID;


    public LeaderIdMessage(Command command, int leaderID, String senderNickname) {
        super(new MessageBuilder().setNickname(senderNickname).setCommand(command));
        this.leaderID = leaderID;
    }

    public int getLeaderID() {
        return leaderID;
    }

    @Override
    public String toString() {
        return "LeaderIdMessage{" +
                "leaderID='" + leaderID + '\'' +
                "} " + super.toString();
    }
}
