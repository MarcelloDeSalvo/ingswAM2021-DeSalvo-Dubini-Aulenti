package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceContainer;

public class DevelopmentRequirement implements Requirement {
    private final int number;
    private final Colour colour;
    private final int level;

    public DevelopmentRequirement(int number, int level, Colour colour) {
        this.number = number;
        this.colour = colour;
        this.level = level;
    }


    /**
     * Check if the current player has enough cards with a specific Level (that can also be any level), a specific colour and a specific amount of this kind of card
     * @param playerBoard is the current player's PlayerBoard
     */
    @Override
    public boolean checkRequirements(PlayerBoard playerBoard) {
        return playerBoard.getProductionSite().hasEnoughDevelopmentCardsWith(number, level, colour);
    }


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
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


    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n - Development Requirement: ");

        if(number != 0)
            stringBuilder.append(number).append(" ");

        stringBuilder.append(colour).append(" Development Card");

        if(number > 1)
            stringBuilder.append("s");

        if(level != 0)
            stringBuilder.append(" with Level = ").append(level);

        return stringBuilder.toString();
    }
    //------------------------------------------------------------------------------------------------------------------


}
