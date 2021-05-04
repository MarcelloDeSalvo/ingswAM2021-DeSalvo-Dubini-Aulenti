package it.polimi.ingsw.network.commands;

public class CreateLobbyMessage extends Message{
    String lobbyName;
    int numOfPlayers;

    public CreateLobbyMessage(String lobbyName, int numOfPlayers, boolean customParameters) {
        super(Command.CREATE_LOBBY);
        this.lobbyName = lobbyName;
        this.numOfPlayers = numOfPlayers;
    }

    public CreateLobbyMessage(String lobbyName, int numOfPlayers, String senderNick) {
        super(Command.CREATE_LOBBY);
        this.lobbyName = lobbyName;
        this.numOfPlayers = numOfPlayers;
        this.senderNickname = senderNick;
    }


    public CreateLobbyMessage(String info, Target target, String lobbyName, int numOfPlayers) {
        super(Command.CREATE_LOBBY, info, target);
        this.lobbyName = lobbyName;
        this.numOfPlayers = numOfPlayers;
    }

    public CreateLobbyMessage(String info, Target target, String senderNickname, String lobbyName, int numOfPlayers) {
        super(Command.CREATE_LOBBY, info, target, senderNickname);
        this.lobbyName = lobbyName;
        this.numOfPlayers = numOfPlayers;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    @Override
    public String toString() {
        return "CreateLobbyMessage{" +
                "lobbyName='" + lobbyName + '\'' +
                ", numOfPlayers=" + numOfPlayers +
                "} " + super.toString();
    }
}
