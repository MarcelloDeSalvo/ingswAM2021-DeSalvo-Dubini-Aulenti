package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class LobbyListMessage extends Message{
    ArrayList<String> lobbyInfos;


    public LobbyListMessage(ArrayList<String> lobbyNames, String senderNickname) {
        super( new MessageBuilder().setCommand(Command.LOBBY_LIST).setNickname(senderNickname));
        this.lobbyInfos = lobbyNames;
    }

    public ArrayList<String> getLobbiesInfos() {
        return lobbyInfos;
    }

    @Override
    public String toString() {
        return "LobbyListMessage{" +
                "lobbyList=" + lobbyInfos +
                "} " + super.toString();
    }
}
