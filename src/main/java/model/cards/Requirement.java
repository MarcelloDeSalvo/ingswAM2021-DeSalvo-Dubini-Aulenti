package model.cards;

import model.player.PlayerBoard;
import model.resources.ResourceContainer;


public interface Requirement {
    boolean checkRequirements (PlayerBoard playerBoard);

    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    Colour getColour();
    int getLevel();
    int getDevelopmentCardNumber();
    ResourceContainer getResourceRequirement();
    //------------------------------------------------------------------------------------------------------------------

}
