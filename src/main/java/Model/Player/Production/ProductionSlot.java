package Model.Player.Production;

import Model.Cards.Colour;
import Model.Exceptions.MaterialChoiceRequired;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public interface ProductionSlot {

    public int countCardsWith(int level, Colour c);
    public ArrayList<ResourceContainer> getProductionInput() throws MaterialChoiceRequired;
    public ArrayList<ResourceContainer> getProductionOutput() throws MaterialChoiceRequired;
    public boolean hasQuestionMarks();
}
