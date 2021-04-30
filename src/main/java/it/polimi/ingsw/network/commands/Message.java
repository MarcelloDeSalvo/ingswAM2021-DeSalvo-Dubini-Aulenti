package it.polimi.ingsw.network.commands;

import com.google.gson.Gson;

public class Message {
    static Gson gson = new Gson();
    Command command;
    String info;

    public Message(Command command) {
        this.command = command;
        this.info = null;
    }

    public Message(Command command, String message) {
        this.command = command;
        this.info = message;
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


    @Override
    public String toString() {
        return "Message{" +
                "command=" + command +
                ", info='" + info + '\'' +
                '}';
    }
}
