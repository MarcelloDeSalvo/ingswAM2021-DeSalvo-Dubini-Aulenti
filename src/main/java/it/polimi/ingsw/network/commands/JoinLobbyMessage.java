package it.polimi.ingsw.network.commands;

public class JoinLobbyMessage extends Message{
    private final String lobbyName;


    /**
     * Message from a player. Used to inform the server that a player wants to join a specific Lobby
     * @param lobbyName Lobby name
     * @param senderNickname player that wants to join the lobby
     */
    public JoinLobbyMessage(String lobbyName, String senderNickname) {
        super(new MessageBuilder().setCommand(Command.JOIN_LOBBY).setNickname(senderNickname));
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
