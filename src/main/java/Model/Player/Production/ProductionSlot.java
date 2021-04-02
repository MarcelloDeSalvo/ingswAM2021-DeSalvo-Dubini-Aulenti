package Model.Player.Production;

import Model.Cards.Colour;

public interface ProductionSlot {

    public int countCardsWith(int level, Colour c);
}
