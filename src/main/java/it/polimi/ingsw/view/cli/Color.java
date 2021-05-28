package it.polimi.ingsw.view.cli;

public enum Color {
    ANSI_RED ("\u001B[38;5;196m"),
    ANSI_YELLOW ("\u001B[38;5;220m"),
    ANSI_BLUE ("\u001B[38;5;27m"),
    ANSI_PURPLE ("\u001B[38;5;162m"),
    ANSI_GRAY ("\u001B[38;5;243m"),
    ANSI_WHITE ("\u001B[0;1m"),

    ANSI_GREEN ("\u001B[38;5;46m"),
    ANSI_CYAN ("\u001B[36m"),
    ANSI_BLACK ("\u001B[30m"),
    ANSI_WHITE_BOLD_FRAMED("\u001B[51;1m"),
    WHITE_BOLD_BRIGHT ("\u001B[1;97m"),

    ANSI_WHITE_FRAMED_BACKGROUND ("\u001B[51;97m"),
    ANSI_YELLOW_FRAMED_BACKGROUND ("\u001B[51;93m"),
    ANSI_RED_FRAMED_BACKGROUND ("\u001B[51;91m"),

    ANSI_RESET ("\u001B[0m");

    static final String RESET = "\u001B[0m";

    private final String escape;

    Color(String escape) {
        this.escape = escape;
    }

    public String escape(){
        return escape;
    }


}
