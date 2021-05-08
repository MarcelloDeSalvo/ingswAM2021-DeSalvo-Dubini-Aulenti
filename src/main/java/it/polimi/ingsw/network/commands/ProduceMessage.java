package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class ProduceMessage extends Message {
    int id;

    public ProduceMessage(int id, String senderNick) {
        super(new MessageBuilder().setCommand(Command.PRODUCE).setNickname(senderNick));
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
