package Model;

import Model.Cards.Colour;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;

interface Action{
    void doAction(Game g);
}

public class ActionToken {
    private Colour colour;
    private int amountOfCards;
    private int faithPoints;

    private ArrayList<Action> actions = new ArrayList<>();

    public ActionToken(Colour colour, int amountOfCards, int faithPoints) {
        this.colour = colour;
        this.amountOfCards = amountOfCards;
        this.faithPoints = faithPoints;

        Action removeCards = (g) -> {
            if(colour != null) {
                g.getCardgrid().removeAmountOfDevelopmentCardWithColour(amountOfCards, colour);
                //if(!g.getCardgrid().getIfAColourIsPresent(colour))
                //notify observer
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
}
