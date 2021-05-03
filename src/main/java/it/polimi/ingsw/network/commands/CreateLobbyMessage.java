package it.polimi.ingsw.network.commands;

public class CreateLobbyMessage extends Message{
    String lobbyName;
    int numOfPlayers;
    boolean customParameters;

    public CreateLobbyMessage(String lobbyName, int numOfPlayers, boolean customParameters) {
        super(Command.CREATE_LOBBY);
        this.lobbyName = lobbyName;
        this.numOfPlayers = numOfPlayers;
        this.customParameters = customParameters;
    }

    public CreateLobbyMessage(String info, Target target, String lobbyName, int numOfPlayers, boolean customParameters) {
        super(Command.CREATE_LOBBY, info, target);
        this.lobbyName = lobbyName;
        this.numOfPlayers = numOfPlayers;
        this.customParameters = customParameters;
    }

    public CreateLobbyMessage(String info, Target target, String senderNickname, String lobbyName, int numOfPlayers, boolean customParameters) {
        super(Command.CREATE_LOBBY, info, target, senderNickname);
        this.lobbyName = lobbyName;
        this.numOfPlayers = numOfPlayers;
        this.customParameters = customParameters;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public boolean isCustomParameters() {
        return customParameters;
    }

    @Override
    public String toString() {
        return "LobbySettings{" +
                "lobbyName='" + lobbyName + '\'' +
                ", numOfPlayers=" + numOfPlayers +
                ", customParameters=" + customParameters +
                "} " + super.toString();
    }
}
