package it.polimi.ingsw.network.commands;

public class JoinLobbyMessage extends Message{
    private final String lobbyName;



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
