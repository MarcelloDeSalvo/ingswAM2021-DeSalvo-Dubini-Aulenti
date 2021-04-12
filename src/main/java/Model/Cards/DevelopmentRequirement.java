package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

public class DevelopmentRequirement implements Requirement {
    private final int number;
    private final Colour colour;
    private final int level;

    public DevelopmentRequirement(int number, int level, Colour colour) {
        this.number = number;
        this.colour = colour;
        this.level = level;
    }

    public DevelopmentRequirement(int number, Colour colour) {
        this.number = number;
        this.colour = colour;
        this.level = 0;
    }



    /**
     * Check if the current player has enough cards with a specific Level (that can also be any level), a specific colour and a specific amount of this kind of card
     * @param playerBoard is the current player's PlayerBoard
     * @return
     */
    @Override
    public boolean checkRequirements(PlayerBoard playerBoard) {
        return playerBoard.getProductionSite().hasEnoughDevelopmentCardsWith(number, level, colour);
    }


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    @Override
    public int getDevelopmentCardNumber() {
        return number;
    }

    @Override
    public Colour getColour() {
        return colour;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public ResourceContainer getResourceRequirement() {
        return null;
    }
    //------------------------------------------------------------------------------------------------------------------


    //JAVA---------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "DevelopmentRequirement{" +
                "number=" + number +
                ", colour=" + colour +
                ", level=" + level +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------


}
