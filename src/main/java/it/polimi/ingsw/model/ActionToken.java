package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.observers.gameListeners.ActionTokenListener;
import it.polimi.ingsw.observers.gameListeners.ActionTokenSubject;

import java.util.ArrayList;

interface Action{
    void doAction(Game_TokensAccess g);
}

public class ActionToken implements ActionTokenSubject {
    private Colour colour;
    private int amountOfCards;
    private int faithPoints;

    private int  actionID;

    private ActionTokenListener actionTokenListener;

    private final ArrayList<Action> actions = new ArrayList<>();

    public ActionToken() {
        Action removeCards = (game) -> {
            if(colour != null) {
                game.getCardgrid().removeAmountOfDevelopmentCardWithColour(amountOfCards, colour);

                switch (colour) {
                    case GREEN:
                        actionID = 1;
                        break;

                    case BLUE:
                        actionID = 2;
                        break;

                    case YELLOW:
                        actionID = 3;
                        break;

                    case PURPLE:
                        actionID = 4;
                        break;
                }

                actionTokenListener.notifyLorenzoAction(actionID, colour);

                if(!game.getCardgrid().getIfAColourIsPresent(colour))
                    game.getLorenzo().notifyEndGame();
            }
        };

        Action addFaithPoints = (game) -> {
            if(faithPoints != 0) {
                if(faithPoints == 1) {
                    game.getLorenzo().shuffleActionTokens();
                    actionID = 6;
                }
                else
                    actionID = 5;


                ResourceContainer container = new ResourceContainer(ResourceType.FAITH, faithPoints);
                container.addToFaithPath(game.getFaithPath());

                actionTokenListener.notifyLorenzoAction(actionID, null);
            }
        };

        actions.add(removeCards);
        actions.add(addFaithPoints);
    }


    @Override
    public void addActionTokenListener(ActionTokenListener actionTokenListener) {
        this.actionTokenListener = actionTokenListener;
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
