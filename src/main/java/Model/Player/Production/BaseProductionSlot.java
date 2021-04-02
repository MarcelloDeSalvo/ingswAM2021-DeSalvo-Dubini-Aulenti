package Model.Player.Production;

import Model.Cards.Colour;

public class BaseProductionSlot implements ProductionSlot {

    @Override
    public int countCardsWith(int level, Colour c) {
        return 0;
    }
}

