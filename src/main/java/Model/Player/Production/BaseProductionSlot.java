package Model.Player.Production;

import Model.Cards.Colour;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;

public class BaseProductionSlot implements ProductionSlot {

    private ArrayList<ResourceContainer> input;
    private ArrayList<ResourceContainer> output;
    private int QMI;
    private int QMO;


    public BaseProductionSlot() {
        this.input = new ArrayList<ResourceContainer>();
        this.output = new ArrayList<ResourceContainer>();
        this.QMI = 2;
        this.QMO = 1;
    }

    public BaseProductionSlot(int defaultQtyIn , int defaultQtyOut) {
        this.input = new ArrayList<ResourceContainer>();
        this.output = new ArrayList<ResourceContainer>();
        this.QMI = defaultQtyIn;
        this.QMO = defaultQtyOut;
    }



    @Override
    public int countCardsWith(int level, Colour c) {
        return 0;
    }

    @Override
    public ArrayList<ResourceContainer> getProductionInput() {
        return null;
    }

    @Override
    public ArrayList<ResourceContainer> getProductionOutput() {
        return null;
    }

    @Override
    public boolean hasQuestionMarks() {
        return true;
    }

    @Override
    public boolean fillQuestionMarkInput(ResourceType resourceType) throws NullPointerException,IllegalArgumentException{
        if(resourceType!= null &&  input.add(new ResourceContainer(resourceType, 1)))
            return true;
        return false;
    }

    @Override
    public boolean fillQuestionMarkOutput(ResourceType resourceType) throws NullPointerException,IllegalArgumentException{
        if(resourceType!= null &&  output.add(new ResourceContainer(resourceType, 1)))
            return true;
        return false;
    }

    @Override
    public boolean clearCurrentBuffer() {
        input.clear();
        output.clear();
        return true;

    }
}

