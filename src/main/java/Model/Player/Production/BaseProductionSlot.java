package Model.Player.Production;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;

public class BaseProductionSlot implements ProductionSlot {

    private final ArrayList<ResourceContainer> input;
    private final ArrayList<ResourceContainer> output;
    private final int QMI;
    private final int QMO;


    public BaseProductionSlot() {
        this.input = new ArrayList<>();
        this.output = new ArrayList<>();
        this.QMI = 2;
        this.QMO = 1;
    }

    public BaseProductionSlot(int defaultQtyIn , int defaultQtyOut) {
        this.input = new ArrayList<>();
        this.output = new ArrayList<>();
        this.QMI = defaultQtyIn;
        this.QMO = defaultQtyOut;
    }



    @Override
    public int countCardsWith(int level, Colour c) {
        return 0;
    }

    @Override
    public ArrayList<ResourceContainer> getProductionInput() {
        return input;
    }

    @Override
    public ArrayList<ResourceContainer> getProductionOutput() {
        return output;
    }

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
}

