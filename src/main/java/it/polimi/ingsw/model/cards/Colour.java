package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.view.cli.Color;

public enum Colour {
    GREEN(Color.ANSI_GREEN),
    PURPLE(Color.ANSI_PURPLE),
    YELLOW(Color.ANSI_YELLOW),
    BLUE(Color.ANSI_BLUE);

    private final Color color;

    Colour(Color color){
        this.color = color;
    }


    @Override
    public String toString() {
        return this.color.escape() + super.toString() + Color.ANSI_RESET.escape();
    }
}
