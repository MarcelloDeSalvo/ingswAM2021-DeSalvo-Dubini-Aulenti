package it.polimi.ingsw.network.commands;

import java.util.ArrayList;
import java.util.List;

public class ScoreMessage extends Message{
    List<Integer> playersTotalVictoryPoints;
    ArrayList<String> nicknames;

    public ScoreMessage(List<Integer> playersTotalVictoryPoints, ArrayList<String> nicknames) {
        super(new MessageBuilder().setCommand(Command.NOTIFY_SCORES).setTarget(Target.BROADCAST));
        this.playersTotalVictoryPoints = playersTotalVictoryPoints;
        this.nicknames = nicknames;
    }

    public List<Integer> getPlayersTotalVictoryPoints() {
        return playersTotalVictoryPoints;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }
}
