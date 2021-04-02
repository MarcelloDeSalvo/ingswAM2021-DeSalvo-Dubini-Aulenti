package Model.Cards;

import Model.Player.PlayerBoard;

public class DevelopmentRequirement implements Requirement {
    private int number;
    private Colour colour;
    private int level;

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
        return playerBoard.getProductionSite().hasEnoughDevelopementCardsWith(this.number, this.level, this.colour);

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
