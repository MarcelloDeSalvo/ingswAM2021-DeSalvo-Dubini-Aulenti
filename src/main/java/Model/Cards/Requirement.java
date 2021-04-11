package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;


public interface Requirement {
    boolean checkRequirements (PlayerBoard playerBoard);

    Colour getColour();
    int getLevel();
    int getDevelopmentCardNumber();
    ResourceContainer getResourceRequirement();
}
