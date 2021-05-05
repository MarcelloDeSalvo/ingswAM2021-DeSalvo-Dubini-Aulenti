package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class ShowHandMessage extends Message {
    ArrayList<Integer> cardsID;

    /**
     * reply from the server
     * @param command
     * @param cardsID
     */
    public ShowHandMessage(Command command, ArrayList<Integer> cardsID) {
        super(new MessageBuilder().setCommand(Command.SHOW_HAND));
        this.cardsID = cardsID;
    }

    public ArrayList<Integer> getCardsID() {
        return cardsID;
    }
}
