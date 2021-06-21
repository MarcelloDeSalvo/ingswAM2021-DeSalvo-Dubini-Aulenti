package it.polimi.ingsw.network.commands;

import java.util.ArrayList;
import java.util.List;

public class LobbyListMessage extends Message{
    private final List<LobbyInfo> lobbyInfos;

    public class LobbyInfo {
        private final String lobbyName;
        private final String owner ;

        private final int maxPlayers;
        private final int numOfPlayersConnected;

        private final boolean isFull;
        private final boolean isClosed;

        public LobbyInfo(String lobbyName, String  owner, int numOfPlayersConnected, int maxPlayers, boolean isFull, boolean isClosed) {
            this.lobbyName = lobbyName;
            this.owner = owner;
            this.maxPlayers = maxPlayers;
            this.numOfPlayersConnected = numOfPlayersConnected;
            this.isFull = isFull;
            this.isClosed = isClosed;
        }

        public String getLobbyName() {
            return lobbyName;
        }

        public String getOwner() {
            return owner;
        }

        public int getMaxPlayers() {
            return maxPlayers;
        }

        public int getNumOfPlayersConnected() {
            return numOfPlayersConnected;
        }

        public boolean isFull() {
            return isFull;
        }

        public boolean isClosed() {
            return isClosed;
        }
    }

    /**
     * Message from the server. Used to send the list of all Lobbies (and their infos) through the network.
     */
    public LobbyListMessage(String senderNickname) {
        super(new MessageBuilder().setCommand(Command.LOBBY_LIST).setNickname(senderNickname));
        this.lobbyInfos = new ArrayList<>();
    }

    /**
     * Adds a new element to lobbyInfos
     */
    public void addLobbyInfos(String lobbyName, String  owner, int numOfPlayersConnected, int maxPlayers, boolean isFull, boolean isClosed){
        lobbyInfos.add(new LobbyInfo(lobbyName, owner, numOfPlayersConnected, maxPlayers, isFull, isClosed));
    }

    public List<LobbyInfo> getLobbiesInfos() {
        return lobbyInfos;
    }

    @Override
    public String toString() {
        return "LobbyListMessage{" +
                "lobbyList=" + lobbyInfos +
                "} " + super.toString();
    }
}
