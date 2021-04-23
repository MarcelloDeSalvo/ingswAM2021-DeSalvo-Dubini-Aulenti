package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;

interface Action{
    void doAction(Game_TokensAccess g);
}

public class ActionToken {
    private Colour colour;
    private int amountOfCards;
    private int faithPoints;

    private ArrayList<Action> actions = new ArrayList<>();

    public ActionToken() {
        Action removeCards = (g) -> {
            if(colour != null) {
                g.getCardgrid().removeAmountOfDevelopmentCardWithColour(amountOfCards, colour);
                if(!g.getCardgrid().getIfAColourIsPresent(colour))
                    g.getLorenzo().notifyEndGame();
            }
        };

        Action addFaithPoints = (g) -> {
            if(faithPoints != 0) {
                if(faithPoints == 1)
                    g.getLorenzo().shuffleActionTokens();

                ResourceContainer container = new ResourceContainer(ResourceType.FAITHPOINT, faithPoints);
                container.addToFaithPath();
            }
        };

        actions.add(removeCards);
        actions.add(addFaithPoints);
    }


    //GETTERS-----------------------------------------------------------------------------------------------------------
    public Colour getColour() {
        return colour;
    }

    public int getAmountOfCards() {
        return amountOfCards;
    }

    public int getFaithPoints() {
        return faithPoints;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
    //------------------------------------------------------------------------------------------------------------------


    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "ActionToken{" +
                "colour=" + colour +
                ", amountOfCards=" + amountOfCards +
                ", faithPoints=" + faithPoints +
                '}' + '\n';
    }
    //------------------------------------------------------------------------------------------------------------------
}
