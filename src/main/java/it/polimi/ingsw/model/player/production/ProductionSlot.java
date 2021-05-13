package it.polimi.ingsw.model.player.production;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;

public interface ProductionSlot {


    //SLOT MANAGEMENT---------------------------------------------------------------------------------------------------
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
     * @return true if everything goes right, false if you can't add the newDevelopmentCard for some reasons
     */
     boolean insertOnTop(DevelopmentCard newDevelopmentCard);

    /**
     * Used to check if a specific Development Card can be put "ON_TOP" of a Production Slot
     * @param newDevelopmentCard the DevelopmentCard to checj
     * @return true if everything goes right, false if you can't add the newDevelopmentCard for some reasons
     */
     boolean canInsertOnTop(DevelopmentCard newDevelopmentCard);

    /**
     * Checks the amount and the type of development cards active
     * @return the number of cards with level "level" and colour "c"
     */
    int countCardsWith(int level, Colour c);

    /**
     *
     * @return
     */
    boolean isEmpty();
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    /**
     * Returns to the caller the list of all the required resources
     */
    ArrayList<ResourceContainer> getProductionInput();

    /**
     * Returns to the caller the list of all the production outputs
     */
    ArrayList<ResourceContainer> getProductionOutput();

    /**
     * gets the number of question marks inside the input
     * @return the number of question marks
     */
     int getQMI();

    /**
     * gets the number of question marks inside the output
     * @return the number of question marks
     */
     int getQMO();

    /**
     * Gets the amount of victoryPoints
     * @return the sum of the victoryPoints
     */
    int getVictoryPoints();
    //------------------------------------------------------------------------------------------------------------------



}
