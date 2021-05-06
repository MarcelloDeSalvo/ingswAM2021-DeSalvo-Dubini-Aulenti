package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.view.cli.Color;

public class ChatMessage extends Message {
    String receiver;

    public ChatMessage(String receiver, String message, String senderNickname) {
        super(new MessageBuilder().setCommand(Command.CHAT).setNickname(senderNickname).setInfo(message));
        this.receiver=receiver;
    }
//Costructor for CHAT_ALL, i ignore the receiver field
    public ChatMessage(String message, String senderNickname) {
        super(new MessageBuilder().setCommand(Command.CHAT_ALL).setNickname(senderNickname).setInfo(message));
        this.receiver="Everyone";

    }


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "Chat message to" +
                receiver + '\'' +
                "} " + super.toString();
    }
}