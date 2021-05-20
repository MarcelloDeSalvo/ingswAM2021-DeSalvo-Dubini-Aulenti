package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import javax.imageio.ImageReader;
import java.util.ArrayList;

public class GameSetUp extends Message{
    private final ArrayList<Integer> cardGridIDs;
    private final ArrayList<String> nicknames;
    private final ArrayList<ResourceContainer> marketSetUp;

    public GameSetUp(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames, ArrayList<ResourceContainer> marketSetUp){
        super(new MessageBuilder().setTarget(Target.BROADCAST).setCommand(Command.GAME_SETUP));
        this.cardGridIDs=cardGridIDs;
        this.nicknames=nicknames;
        this.marketSetUp= marketSetUp;
    }

    public ArrayList<Integer> getCardGridIDs() {
        return cardGridIDs;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    public ArrayList<ResourceContainer> getMarketSetUp() { return marketSetUp; }
}
