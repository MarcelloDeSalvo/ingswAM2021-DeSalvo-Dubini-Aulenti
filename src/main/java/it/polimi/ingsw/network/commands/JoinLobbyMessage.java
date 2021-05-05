package it.polimi.ingsw.network.commands;

public class JoinLobbyMessage extends Message{
    String lobbyName;

    public JoinLobbyMessage(String lobbyName) {
        super(Command.JOIN_LOBBY);
        this.lobbyName = lobbyName;
    }

    public JoinLobbyMessage(String lobbyName, String senderNickname) {
        super(Command.JOIN_LOBBY, null, senderNickname);
        this.lobbyName = lobbyName;
    }

    public JoinLobbyMessage(String info, Target target, String lobbyName) {
        super(Command.JOIN_LOBBY, info, target);
        this.lobbyName = lobbyName;
    }

    public JoinLobbyMessage(String info, Target target, String senderNickname, String lobbyName) {
        super(Command.JOIN_LOBBY, info, target, senderNickname);
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
