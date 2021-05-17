package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class GameSetUp extends Message{
    private final ArrayList<Integer> cardGridIDs;
    private final ArrayList<String> nicknames;

    public GameSetUp(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames){
        super(new MessageBuilder().setTarget(Target.BROADCAST).setCommand(Command.GAME_SETUP));
        this.cardGridIDs=cardGridIDs;
        this.nicknames=nicknames;
    }

    public ArrayList<Integer> getCardGridIDs() {
        return cardGridIDs;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }
}
