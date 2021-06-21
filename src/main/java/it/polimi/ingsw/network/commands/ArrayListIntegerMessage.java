package it.polimi.ingsw.network.commands;

import java.util.List;

public class ArrayListIntegerMessage extends Message {
    private final List<Integer> cardsID;

    /**
     * Message from the server. Used for sending an ArrayList of integers thru the network
     * @param cardsID updated cardsID
     */
    public ArrayListIntegerMessage(Command command, List<Integer> cardsID, String senderNick) {
        super(new MessageBuilder().setCommand(command).setNickname(senderNick));
        this.cardsID = cardsID;
    }

    public List<Integer> getCardsID() {
        return cardsID;
    }
}
