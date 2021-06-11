package it.polimi.ingsw.network.commands;

import com.google.gson.Gson;

public class Message {
    private static final Gson gson = new Gson();
    private final Command command;
    private final String info;
    private final Target target;
    private final String senderNickname;

    /**
     * Generic type of message. Extended by every message we used.
     * @param messageBuilder used to build a message with the features desired
     */
    public Message(MessageBuilder messageBuilder) {
        this.command = messageBuilder.command;
        this.info = messageBuilder.info;
        this.target = messageBuilder.target;
        this.senderNickname = messageBuilder.senderNickname;
    }

    public static class MessageBuilder{
        private Command command;
        private String senderNickname;
        private String info;
        private Target target = Target.UNICAST;

        public MessageBuilder setCommand(Command command) {
            this.command = command;
            return this;
        }

        public MessageBuilder setNickname(String senderNickname) {
            this.senderNickname = senderNickname;
            return this;
        }

        public MessageBuilder setInfo(String info) {
            this.info = info;
            return this;
        }

        public MessageBuilder setTarget(Target target) {
            this.target = target;
            return this;
        }

        public Message build(){
            return new Message(this);
        }
    }

    public String serialize () {
        return gson.toJson(this);
    }

    public Command getCommand() {
        return command;
    }

    public String getInfo() {
        return info;
    }

    public Target getTarget() {
        return target;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    @Override
    public String toString() {
        return "Message{" +
                "command=" + command +
                ", info='" + info + '\'' +
                ", target=" + target +
                ", senderNickname='" + senderNickname + '\'' +
                '}';
    }
}
