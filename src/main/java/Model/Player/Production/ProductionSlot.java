package Model.Player.Production;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;

public interface ProductionSlot {
    /**
     * Checks the amount and the type of development cards active
     * @return the number of cards with level "level" and colour "c"
     */
     int countCardsWith(int level, Colour c);

    /**
     * Returns to the caller the list of all the required resources
     */
     ArrayList<ResourceContainer> getProductionInput();

    /**
     * Returns to the caller the list of all the production outputs
     */
     ArrayList<ResourceContainer> getProductionOutput();

    /**
     * Checks if the ProductionSlot has question marks in its production pattern
     */
     boolean hasQuestionMarks();

    /**
     * This method is used by the controller to insert the user's requested resources instead of the question marks  (Production input only)
     * @param resourceType is the user's selected resourceType
     */
     boolean fillQuestionMarkInput(ResourceType resourceType);

    /**
     * This method is used by the controller to insert the user's requested resources instead of the question marks  (Production output only)
     * @param resourceType is the user's selected resourceType
     */
     boolean fillQuestionMarkOutput(ResourceType resourceType);

    /**
     * Replaces back the questionMarks in the depositSlot
     */
     boolean clearCurrentBuffer();

    /**
     * Used only for slots that have queues
     * if the queue is empty it simply sets the new card status to "ON_TOP" and adds the new card in the queue
     * if the queue already has elements in it and if level+1 of the element on top == new card level
     * the method sets the first element of the queue to "ACTIVE" and then adds the new one
     * @param newDevelopmentCard a new DevelopmentCard to add to the queue in Deck
     * @return true
     */
     boolean insertOnTop(DevelopmentCard newDevelopmentCard);

}
