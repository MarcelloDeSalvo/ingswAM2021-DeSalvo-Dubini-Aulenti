package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.view.cli.Color;

public enum Colour {
    GREEN(Color.ANSI_GREEN, "GREEN"),
    PURPLE(Color.ANSI_PURPLE, "PURPLE"),
    YELLOW(Color.ANSI_YELLOW, "YELLOW"),
    BLUE(Color.ANSI_BLUE, "BLUE");

    private final Color color;
    private final String name;

    Colour(Color color, String name){
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return this.color.escape() + super.toString() + Color.ANSI_RESET.escape();
    }

    public String noColorToString() {
        return name;
    }
}
