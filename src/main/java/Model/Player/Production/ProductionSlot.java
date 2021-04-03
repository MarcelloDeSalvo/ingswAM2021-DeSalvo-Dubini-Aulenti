package Model.Player.Production;

import Model.Cards.Colour;
import Model.Exceptions.MaterialChoiceRequired;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;

public interface ProductionSlot {
    /**
     * Checks the amount and the type of development cards active
     * @param level
     * @param c
     * @return the number of cards with level "level" and colour "c"
     */
    public int countCardsWith(int level, Colour c);

    /**
     * Returns to the caller the list of all the required resources
     * @return
     */
    public ArrayList<ResourceContainer> getProductionInput();

    /**
     * Returns to the caller the list of all the production outputs
     * @return
     */
    public ArrayList<ResourceContainer> getProductionOutput();

    /**
     * Checks if the ProductionSlot has question marks in its production pattern
     * @return
     */
    public boolean hasQuestionMarks();

    /**
     * This method is used by the controller to insert the user's requested resources instead of the question marks  (Production input only)
     * @param resourceType is the user's selected resourceType
     * @return
     */
    public boolean fillQuestionMarkInput(ResourceType resourceType);
    /**
     * This method is used by the controller to insert the user's requested resources instead of the question marks  (Production output only)
     * @param resourceType is the user's selected resourceType
     * @return
     */
    public boolean fillQuestionMarkOutput(ResourceType resourceType);

    /**
     * Replaces back the questionMarks in the depositSlot
     * @return
     */
    public boolean clearCurrentBuffer();

}
