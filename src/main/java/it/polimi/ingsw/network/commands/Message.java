package it.polimi.ingsw.network.commands;

import com.google.gson.Gson;

public class Message {
    static Gson gson = new Gson();
    Command command;
    String info;
    Target target;
    String senderNickname;

    public Message(Command command) {
        this.command = command;
        this.info = null;
    }

    public Message(Command command, String message) {
        this.command = command;
        this.info = message;
    }

    public Message(Command command, String info, Target target) {
        this.command = command;
        this.info = info;
        this.target = target;
    }

    public Message(Command command, String info, Target target, String senderNickname) {
        this.command = command;
        this.info = info;
        this.target = target;
        this.senderNickname = senderNickname;
    }

    public void deserialize(){
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
                '}';
    }
}
