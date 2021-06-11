package it.polimi.ingsw.network.commands;

public class ChatMessage extends Message {
    private final String receiver;

    /**
     * Message used for sending chat messages to a specific player
     */
    public ChatMessage(String receiver, String message, String senderNickname) {
        super(new MessageBuilder().setCommand(Command.CHAT).setNickname(senderNickname).setInfo(message));
        this.receiver=receiver;
    }

    /**
     * Message used for sending chat messages to every player
     */
    //Constructor for CHAT_ALL, i ignore the receiver field
    public ChatMessage(String message, String senderNickname) {
        super(new MessageBuilder().setCommand(Command.CHAT_ALL).setNickname(senderNickname).setInfo(message));
        this.receiver = "Everyone";

    }

    public String getReceiver() {
        return receiver;
    }

    @Override
    public String toString() {
        return "Chat message to" +
                receiver + '\'' +
                "} " + super.toString();
    }
}