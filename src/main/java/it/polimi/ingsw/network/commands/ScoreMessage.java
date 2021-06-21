package it.polimi.ingsw.network.commands;

import java.util.List;

public class ScoreMessage extends Message{
    private final List<Integer> playersTotalVictoryPoints;
    private final List<String> nicknames;
    private final List<String> winners;

    /**
     * Message from the server. Used at the end of the game to notify every player about the final scores.
     */
    public ScoreMessage(List<Integer> playersTotalVictoryPoints, List<String> nicknames, List<String> winners) {
        super(new MessageBuilder().setCommand(Command.NOTIFY_SCORES).setTarget(Target.BROADCAST));
        this.playersTotalVictoryPoints = playersTotalVictoryPoints;
        this.nicknames = nicknames;
        this.winners = winners;
    }

    public List<Integer> getPlayersTotalVictoryPoints() {
        return playersTotalVictoryPoints;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public List<String> getWinners() { return winners; }
}
