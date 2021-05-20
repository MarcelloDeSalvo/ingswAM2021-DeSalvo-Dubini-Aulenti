package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class ShowHandMessage extends Message {
    private final ArrayList<Integer> cardsID;

    /**
     * Reply from the server
     * @param cardsID updated cardsID
     */
    public ShowHandMessage(ArrayList<Integer> cardsID, String senderNick) {
        super(new MessageBuilder().setCommand(Command.NOTIFY_HAND).setNickname(senderNick));
        this.cardsID = cardsID;
    }

    public ArrayList<Integer> getCardsID() {
        return cardsID;
    }
}
