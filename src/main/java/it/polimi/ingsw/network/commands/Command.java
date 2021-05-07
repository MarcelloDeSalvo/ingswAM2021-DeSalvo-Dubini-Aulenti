package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.network.server.Status;
import it.polimi.ingsw.network.server.User;

public enum Command {

    QUIT            (true,true,true),
    HELLO           (true,false,false),
    HELLO_ALL       (true,false,false),
    REPLY           (true,true,true),
    CHAT            (true,true,true),
    CHAT_ALL        (true,true,false),

    LOGIN           (false,false,false),
    CREATE_LOBBY    (true,false,false),
    JOIN_LOBBY      (true,false,false),
    LOBBY_LIST      (true,false,false),

    EXIT_LOBBY      (false,true,false),
    START_GAME      (false,true,false),

    DISCARD_LEADER  (false,false,true),
    SEND_CONTAINER  (false,false,true),

    SHOW_HAND       (false,false,true);


    private final boolean usableInLobbyManager;
    private final boolean usableInLobby;
    private final boolean usableInGame;

    Command(boolean canBeUsedInLobbyManager,boolean canBeUsedInLobby, boolean canBeUsedInGame){
        this.usableInLobbyManager=canBeUsedInLobbyManager;
        this.usableInLobby=canBeUsedInLobby;
        this.usableInGame=canBeUsedInGame;

    }

    //COMMAND PERMISSIONS-----------------------------------------------------------------------------------------------

    public boolean isUsableInLobbyManager() { return usableInLobbyManager; }

    public boolean isUsableInLobby() { return usableInLobby; }

    public boolean isUsableInGame() { return usableInGame; }

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
