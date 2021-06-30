package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceContainer;


public interface Requirement {
    boolean checkRequirements (PlayerBoard playerBoard);

    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    Colour getColour();
    int getLevel();
    ResourceContainer getResourceRequirement();
    //------------------------------------------------------------------------------------------------------------------

}
