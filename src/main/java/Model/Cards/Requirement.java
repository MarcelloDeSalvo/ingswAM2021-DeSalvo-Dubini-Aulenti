package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;
import java.util.Collections;

public interface Requirement {
    boolean checkRequirements (PlayerBoard playerBoard);

    Colour getColour();
    int getLevel();
    int getDevelopmentCardNumber();
    ResourceContainer getResourceRequirement();
}
