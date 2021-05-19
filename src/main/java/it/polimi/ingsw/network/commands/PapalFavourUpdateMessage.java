package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class PapalFavourUpdateMessage extends Message{
    private ArrayList<Integer> playerFavours;


    public PapalFavourUpdateMessage(ArrayList<Integer> playerFavours, String senderNickname){
        super(new MessageBuilder().setNickname(senderNickname).setTarget(Target.BROADCAST).setCommand(Command.NOTIFY_FAITHPATH_FAVOURS));
        this.playerFavours=playerFavours;
    }

    public void setPlayerFavours(ArrayList<Integer> playerFavours) {
        this.playerFavours = playerFavours;
    }

    public ArrayList<Integer> getPlayerFavours() {

        return playerFavours;
    }
}