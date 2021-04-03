package Model.Player.Production;

import Model.Cards.Colour;
import Model.Exceptions.MaterialChoiceRequired;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;

public interface ProductionSlot {

    public int countCardsWith(int level, Colour c);
    public ArrayList<ResourceContainer> getProductionInput() throws MaterialChoiceRequired;
    public ArrayList<ResourceContainer> getProductionOutput() throws MaterialChoiceRequired;
    public boolean hasQuestionMarks();
    public boolean fillQuestionMarkInput(ResourceType resourceType);
    public boolean fillQuestionMarkOutput(ResourceType resourceType);
    public boolean clearCurrentBuffer();

}
