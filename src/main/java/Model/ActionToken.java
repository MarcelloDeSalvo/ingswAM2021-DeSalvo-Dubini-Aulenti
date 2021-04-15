package Model;

import Model.Cards.Colour;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

interface Action{
    void doAction(Game g);
}

public class ActionToken {
    private Colour colour;
    private int amountOfCards;
    private int faithPoints;

    public ActionToken(Colour colour, int amountOfCards, int faithPoints) {
        this.colour = colour;
        this.amountOfCards = amountOfCards;
        this.faithPoints = faithPoints;
    }

    Action removeCards = (g) -> {
        g.getCardgrid().removeAmountOfDevelopmentCardWithColour(amountOfCards, colour);
        //if(!g.getCardgrid().getIfAColourIsPresent(colour))
            //notify observer
    };

    Action addFaithPoints = (g) -> {
        if(faithPoints == 1)
            g.getLorenzo().shuffleActionTokens();

        ResourceContainer container = new ResourceContainer(ResourceType.FAITHPOINT, faithPoints);
        container.addToFaithPath();
    };

}
