package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public class ProductionAbility implements Ability {
    private ArrayList<ResourceContainer> input;
    private ArrayList<ResourceContainer> output;

    public ProductionAbility(ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output) {
        this.input = new ArrayList<ResourceContainer>(input);
        this.output = new ArrayList<ResourceContainer>(output);
    }

    @Override
    public boolean useAbility(PlayerBoard playerBoard) {
        return false;
    }
}
