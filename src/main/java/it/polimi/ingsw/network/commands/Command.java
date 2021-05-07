package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.network.server.Status;
import it.polimi.ingsw.network.server.User;

public enum Command {

    QUIT            (true,true,true,Status.IN_LOBBY_MANAGER),
    HELLO           (true,false,false,Status.IN_LOBBY_MANAGER),
    HELLO_ALL       (true,false,false,Status.IN_LOBBY_MANAGER),
    REPLY           (true,true,true,null),
    CHAT            (true,true,true,Status.IN_LOBBY_MANAGER),
    CHAT_ALL        (true,true,false,Status.IN_LOBBY_MANAGER),

    LOGIN           (false,false,false,null),
    CREATE_LOBBY    (true,false,false,Status.IN_LOBBY_MANAGER),
    JOIN_LOBBY      (true,false,false,Status.IN_LOBBY_MANAGER),
    LOBBY_LIST      (true,false,false,Status.IN_LOBBY_MANAGER),

    EXIT_LOBBY      (false,true,false,Status.IN_LOBBY),
    START_GAME      (false,true,false,Status.IN_LOBBY),

    DISCARD_LEADER  (false,false,true,Status.IN_GAME),
    SEND_CONTAINER  (false,false,true,Status.IN_GAME),

    SHOW_HAND       (false,false,true,null);


    private final boolean usableInLobbyManager;
    private final boolean usableInLobby;
    private final boolean usableInGame;
    private final Status whereToProcess;

    Command(boolean canBeUsedInLobbyManager,boolean canBeUsedInLobby, boolean canBeUsedInGame, Status status){
        this.usableInLobbyManager=canBeUsedInLobbyManager;
        this.usableInLobby=canBeUsedInLobby;
        this.usableInGame=canBeUsedInGame;
        this.whereToProcess=status;

    }

    //COMMAND PERMISSIONS-----------------------------------------------------------------------------------------------

    public boolean isUsableInLobbyManager() { return usableInLobbyManager; }

    public boolean isUsableInLobby() { return usableInLobby; }

    public boolean isUsableInGame() { return usableInGame; }

    public Status getWhereToProcess() { return whereToProcess; }

    public static boolean canUseCommand(User user, Command command){
        switch (user.getStatus()) {
            case IN_GAME:
                return (command.isUsableInGame());
            case IN_LOBBY:
                return (command.isUsableInLobby());
            case IN_LOBBY_MANAGER:
                return (command.isUsableInLobbyManager());
        }

        return false;
    }


}
