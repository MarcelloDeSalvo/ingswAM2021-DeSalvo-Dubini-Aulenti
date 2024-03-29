package it.polimi.ingsw.network.commands;

public class CreateLobbyMessage extends Message{
    private final String lobbyName;
    private final int numOfPlayers;

    /**
     * Message used to notify the server that a player wants to create a new Lobby
     * @param senderNick player performing the action
     */
    public CreateLobbyMessage(String lobbyName, int numOfPlayers, String senderNick) {
        super(new MessageBuilder().setNickname(senderNick).setCommand(Command.CREATE_LOBBY));
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
