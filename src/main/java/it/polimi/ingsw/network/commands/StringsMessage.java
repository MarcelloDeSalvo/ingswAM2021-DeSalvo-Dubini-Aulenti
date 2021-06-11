package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class StringsMessage extends Message{
    private final ArrayList<String> data;

    /**
     * Message from the server. Used to send an ArrayList of strings through the network
     */
    public StringsMessage(MessageBuilder messageBuilder, ArrayList<String> data) {
        super(messageBuilder);
        this.data = data;
    }

    public ArrayList<String> getData() {
        return data;
    }
}
