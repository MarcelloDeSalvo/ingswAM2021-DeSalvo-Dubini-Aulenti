package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class ArrayListIntegerMessage extends Message {
    private final ArrayList<Integer> cardsID;

    /**
     * Reply from the server
     * @param cardsID updated cardsID
     */
    public ArrayListIntegerMessage(Command command,ArrayList<Integer> cardsID, String senderNick) {
        super(new MessageBuilder().setCommand(command).setNickname(senderNick));
        this.cardsID = cardsID;
    }

    public ArrayList<Integer> getCardsID() {
        return cardsID;
    }
}
