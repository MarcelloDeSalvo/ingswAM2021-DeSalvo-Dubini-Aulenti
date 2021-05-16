package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class ProduceMessage extends Message {
    private final ArrayList<Integer> IDs;

    public ProduceMessage(ArrayList<Integer> IDs, String senderNick) {
        super(new MessageBuilder().setCommand(Command.PRODUCE).setNickname(senderNick));
        this.IDs = new ArrayList<>(IDs);
    }

    public ArrayList<Integer> getIDs() {
        return IDs;
    }
}
