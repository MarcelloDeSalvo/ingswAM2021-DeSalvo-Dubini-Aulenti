package Model.Cards;

import Model.Player.PlayerBoard;

public class DevelopmentRequirement implements Requirement {
    private int number;
    private Colour colour;
    private int level;

    public DevelopmentRequirement(int number, Colour colour, int level) {
        this.number = number;
        this.colour = colour;
        this.level = level;
    }

    @Override
    public boolean checkRequirements(PlayerBoard playerBoard) {
        return false;
    }
}
