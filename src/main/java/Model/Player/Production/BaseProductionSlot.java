package Model.Player.Production;

import Model.Cards.Colour;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public class BaseProductionSlot implements ProductionSlot {
    private ArrayList<ResourceContainer> input;
    private ArrayList<ResourceContainer> output;


    public BaseProductionSlot(){
        this.input= new ArrayList<ResourceContainer>();
        input.add(new ResourceContainer(null, 1));
        input.add(new ResourceContainer(null, 1));
        this.output= new ArrayList<ResourceContainer>();
        output.add(new ResourceContainer(null,1));
    }

    @Override
    public int countCardsWith(int level, Colour c) {
        return 0;
    }
}

