package it.polimi.ingsw.network.commands;

public class JoinLobbyMessage extends Message{
    String lobbyName;

    public JoinLobbyMessage(String lobbyName) {
        super(Command.JOIN_LOBBY);
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    @Override
    public String toString() {
        return "JoinLobby{" +
                "lobbyName='" + lobbyName + '\'' +
                "} " + super.toString();
    }
}
