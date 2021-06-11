package it.polimi.ingsw.network.commands;

import java.util.ArrayList;
import java.util.List;

public class ScoreMessage extends Message{
    private final List<Integer> playersTotalVictoryPoints;
    private final ArrayList<String> nicknames;

    /**
     * Message from the server. Used at the end of the game to notify every player about the final scores.
     */
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
