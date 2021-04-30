package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class LobbyListMessage extends Message{
    ArrayList<String> lobbyNames;

    public LobbyListMessage(Command command, ArrayList<String> lobbyList) {
        super(Command.LOBBY_LIST);
        this.lobbyNames = lobbyList;
    }

    public ArrayList<String> getLobbyNames() {
        return lobbyNames;
    }

    @Override
    public String toString() {
        return "LobbyListMessage{" +
                "lobbyList=" + lobbyNames +
                "} " + super.toString();
    }
}
