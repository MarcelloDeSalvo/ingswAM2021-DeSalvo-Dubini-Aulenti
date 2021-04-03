package Model.Player.Production;

import Model.Cards.Colour;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public interface ProductionSlot {

    public int countCardsWith(int level, Colour c);
    public ArrayList<ResourceContainer> getProductionInput();
    public ArrayList<ResourceContainer> getProducionOutput();

}
