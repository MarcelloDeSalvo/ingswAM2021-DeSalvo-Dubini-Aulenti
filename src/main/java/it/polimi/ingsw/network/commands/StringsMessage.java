package it.polimi.ingsw.network.commands;

import java.util.List;

public class StringsMessage extends Message{
    private final List<String> data;

    /**
     * Message from the server. Used to send an ArrayList of strings through the network
     */
    public StringsMessage(MessageBuilder messageBuilder, List<String> data) {
        super(messageBuilder);
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }
}
