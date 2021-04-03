package Model.Player.Production;

import Model.Cards.*;
import Model.Deck;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public class DevelopmentCardProduction implements ProductionSlot {
    private Deck dev;

    public DevelopmentCardProduction() {
        dev = new Deck();
    }

    public boolean insertOnTop (DevelopmentCard developmentCard) {
        if(dev.getDeck().peek() != null)
            dev.getDeck().element().changeStatus(Status.ACTIVE);

        developmentCard.changeStatus(Status.ON_TOP);
        dev.getDeck().add(developmentCard);

        return true;
    }

    @Override
    public ArrayList<ResourceContainer> getProductionInput() {
        if(dev.getDeck().peek() != null)
            return dev.getDeck().element().getInput();
        else
            return null;
    }

    @Override
    public ArrayList<ResourceContainer> getProductionOutput() {
        if(dev.getDeck().peek() != null)
            return dev.getDeck().element().getOutput();
        else
            return null;
    }

    @Override
    public int countCardsWith(int level, Colour c) {
        int i = 0;
        for (DevelopmentCard d: dev.getDeck()) {
            if (d.isSameLevelandColour(level, c))
                i++;
        }
        return i;
    }

    @Override
    public boolean hasQuestionMarks() {
        return false;
    }
}
