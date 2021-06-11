package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class ProduceMessage extends Message {
    private final ArrayList<Integer> IDs;

    /**
     * Message from a player. Used to inform the server that a player wants to activate the production.
     * @param IDs Production Slot IDs that the player wants to activate
     * @param senderNick player that wants to perform the action
     */
    public ProduceMessage(ArrayList<Integer> IDs, String senderNick) {
        super(new MessageBuilder().setCommand(Command.PRODUCE).setNickname(senderNick));
        this.IDs = new ArrayList<>(IDs);
    }

    public ArrayList<Integer> getIDs() {
        return IDs;
    }
}
