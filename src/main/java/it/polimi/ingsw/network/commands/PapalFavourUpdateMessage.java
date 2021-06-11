package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class PapalFavourUpdateMessage extends Message{
    private final ArrayList<Integer> playerFavours;

    /**
     * Message from the server. Used to notify every player that someone activated a "Papal Favour"
     * @param playerFavours amount of victory points that every player received
     */
    public PapalFavourUpdateMessage(ArrayList<Integer> playerFavours, String senderNickname){
        super(new MessageBuilder().setNickname(senderNickname).setTarget(Target.BROADCAST).setCommand(Command.NOTIFY_FAITHPATH_FAVOURS));
        this.playerFavours = playerFavours;
    }

    public ArrayList<Integer> getPlayerFavours() {
        return playerFavours;
    }
}
