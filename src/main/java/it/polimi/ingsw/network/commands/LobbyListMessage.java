package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class LobbyListMessage extends Message{
    ArrayList<String> lobbyInfos;


    public LobbyListMessage(ArrayList<String> lobbyNames, String senderNickname) {
        super(Command.LOBBY_LIST, "", Target.UNICAST, senderNickname);
        this.lobbyInfos = lobbyNames;
    }

    public LobbyListMessage(String senderNickname) {
        super(Command.LOBBY_LIST, "", Target.UNICAST, senderNickname);
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
