package it.polimi.ingsw.network.commands;

import java.util.ArrayList;

public class LobbyListMessage extends Message{
    private final ArrayList<LobbyInfo> lobbyInfos;

    public class LobbyInfo {
        private final String lobbyName;
        private final String owner ;

        private final int maxPlayers;
        private final int numOfPlayersConnected;

        private boolean isFull = false;
        private boolean isClosed = false;

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

    public LobbyListMessage(String senderNickname) {
        super(new MessageBuilder().setCommand(Command.LOBBY_LIST).setNickname(senderNickname));
        this.lobbyInfos = new ArrayList<>();
    }

    public void addLobbyInfos(String lobbyName, String  owner, int numOfPlayersConnected, int maxPlayers, boolean isFull, boolean isClosed){
        lobbyInfos.add(new LobbyInfo(lobbyName, owner, numOfPlayersConnected, maxPlayers, isFull, isClosed));
    }

    public ArrayList<LobbyInfo> getLobbiesInfos() {
        return lobbyInfos;
    }


    @Override
    public String toString() {
        return "LobbyListMessage{" +
                "lobbyList=" + lobbyInfos +
                "} " + super.toString();
    }
}
