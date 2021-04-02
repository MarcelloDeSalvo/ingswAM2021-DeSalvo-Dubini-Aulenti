package Model.Player.Production;

import Model.Cards.Colour;
import Model.Player.Production.ProductionSlot;

public class LeaderCardProduction implements ProductionSlot {

    @Override
    public int countCardsWith(int level, Colour c) {
        return 0;
    }
}
