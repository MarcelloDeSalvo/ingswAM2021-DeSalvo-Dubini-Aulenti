package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceContainer;

import java.util.List;

public class GameSetUp extends Message{
    private final List<Integer> cardGridIDs;
    private final List<String> nicknames;
    private final List<ResourceContainer> marketSetUp;

    /**
     * Message from the server. Used to notify the players that the game is started and to send them CardGrid and Market set up
     * @param cardGridIDs contains all the starting Development Cards' IDs
     * @param nicknames nicknames of every player in game
     * @param marketSetUp contains all the starting ResourceContainers in the Market
     */
    public GameSetUp(List<Integer> cardGridIDs, List<String> nicknames, List<ResourceContainer> marketSetUp){
        super(new MessageBuilder().setTarget(Target.BROADCAST).setCommand(Command.GAME_SETUP));
        this.cardGridIDs = cardGridIDs;
        this.nicknames = nicknames;
        this.marketSetUp = marketSetUp;
    }

    public List<Integer> getCardGridIDs() {
        return cardGridIDs;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public List<ResourceContainer> getMarketSetUp() { return marketSetUp; }
}
