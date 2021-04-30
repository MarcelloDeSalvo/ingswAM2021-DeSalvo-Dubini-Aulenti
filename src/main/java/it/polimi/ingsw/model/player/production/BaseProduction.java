package it.polimi.ingsw.model.player.production;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;

public class BaseProduction implements ProductionSlot {

    private final ArrayList<ResourceContainer> input;
    private final ArrayList<ResourceContainer> output;
    private final int QMI;
    private final int QMO;


    public BaseProduction() {
        this.input = new ArrayList<>();
        this.output = new ArrayList<>();
        this.QMI = 2;
        this.QMO = 1;
    }

    public BaseProduction(int defaultQtyIn , int defaultQtyOut) {
        this.input = new ArrayList<>();
        this.output = new ArrayList<>();
        this.QMI = defaultQtyIn;
        this.QMO = defaultQtyOut;
    }


    //SLOT MANAGEMENT---------------------------------------------------------------------------------------------------
    @Override
    public boolean hasQuestionMarks() {
        return true;
    }

    @Override
    public boolean fillQuestionMarkInput(ResourceType resourceType) throws NullPointerException,IllegalArgumentException{
        return resourceType != null && input.add(new ResourceContainer(resourceType, 1));
    }

    @Override
    public boolean fillQuestionMarkOutput(ResourceType resourceType) throws NullPointerException,IllegalArgumentException{
        return resourceType != null && output.add(new ResourceContainer(resourceType, 1));
    }

    @Override
    public boolean clearCurrentBuffer() {
        input.clear();
        output.clear();
        return true;
    }

    @Override
    public boolean insertOnTop(DevelopmentCard newDevelopmentCard) {
        return false;
    }

    @Override
    public int countCardsWith(int level, Colour c) {
        return 0;
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    @Override
    public ArrayList<ResourceContainer> getProductionInput() {
        return input;
    }

    @Override
    public ArrayList<ResourceContainer> getProductionOutput() {
        return output;
    }

    public int getQMI() {
        return QMI;
    }

    public int getQMO() {
        return QMO;
    }

    @Override
    public int getVictoryPoints() {
        return 0;
    }
    //------------------------------------------------------------------------------------------------------------------

}
