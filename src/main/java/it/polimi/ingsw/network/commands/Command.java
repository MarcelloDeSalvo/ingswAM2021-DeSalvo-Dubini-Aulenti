package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.network.server.Status;
import it.polimi.ingsw.network.server.User;

public enum Command {

    //RECEIVED COMMANDS (SERVER SIDE)-----------------------------------------------------------------------------------
    PONG                (true,true,true, Status.IN_LOBBY_MANAGER),

    QUIT                (true,true,true, Status.IN_LOBBY_MANAGER),
    HELLO               (true,true,true, Status.IN_LOBBY_MANAGER),
    HELLO_ALL           (true,false,false, Status.IN_LOBBY_MANAGER),

    CHAT                (true,true,true, Status.IN_LOBBY_MANAGER),
    CHAT_ALL            (true,true,false, Status.IN_LOBBY_MANAGER),

    LOGIN               (false,false,false,null),
    RECONNECTED         (false,false,false,null),

    LOBBY_LIST          (true,false,false, Status.IN_LOBBY_MANAGER),
    CREATE_LOBBY        (true,false,false, Status.IN_LOBBY_MANAGER),
    PLAYER_LIST         (false, true, false, Status.IN_LOBBY),
    JOIN_LOBBY          (true,false,false, Status.IN_LOBBY_MANAGER),
    EXIT_LOBBY          (false,true,false, Status.IN_LOBBY),
    START_GAME          (false,true,false, Status.IN_LOBBY),
    END_GAME            (false, true, false, Status.IN_LOBBY),

    CHEAT_VAULT         (false,false,true, Status.IN_GAME),

    DISCARD_LEADER      (false,false,true, Status.IN_GAME),
    ACTIVATE_LEADER     (false, false, true, Status.IN_GAME),

    BUY                 (false, false, true, Status.IN_GAME),
    PRODUCE             (false, false, true, Status.IN_GAME),
    PICK_FROM_MARKET    (false, false,true, Status.IN_GAME),
    MANAGE_DEPOSIT      (false, false, true, Status.IN_GAME),
    SWITCH_DEPOSIT      (false,false,true, Status.IN_GAME),

    FILL_QM             (false, false, true, Status.IN_GAME),
    CONVERSION          (false, false, true, Status.IN_GAME),

    SETUP_CONTAINER     (false, false, true, Status.IN_GAME),
    SEND_CONTAINER      (false,false,true, Status.IN_GAME),
    DONE                (false, false, true, Status.IN_GAME),

    SEND_DEPOSIT_ID     (false, false,true, Status.IN_GAME),

    END_TURN            (false, false, true, Status.IN_GAME),


    //SENT COMMANDS  (SERVER SIDE)--------------------------------------------------------------------------------------
    PING,
    REPLY,

    USER_JOINED_LOBBY,
    USER_LEFT_LOBBY,

    GAME_SETUP,

    DISCARD_OK,
    ACTIVATE_OK,

    BUY_OK,
    BUY_SLOT_OK,
    BUY_ERROR,

    PRODUCE_OK,
    PRODUCE_ERROR,

    MANAGE_DEPOSIT_OK,

    REMOVE_CONTAINER_OK,
    REMOVE_CONTAINER_ERROR,

    MARKET_OK,

    CONVERSION_ERROR,

    START_FILL,
    FILL_OK,
    PRODUCTION_PRICE,

    NOTIFY_TURN_CHANGE,
    NOTIFY_CARDGRID,
    NOTIFY_DEPOSIT_UPDATE,
    NOTIFY_NEW_DEPOSIT,
    NOTIFY_VAULT_UPDATE,
    NOTIFY_NEW_PROD_SLOT,
    NOTIFY_HAND,
    NOTIFY_FAITHPATH_CURRENT,
    NOTIFY_FAITHPATH_OTHERS,
    NOTIFY_FAITHPATH_FAVOURS,
    NOTIFY_GAME_STARTED,
    NOTIFY_LORENZO_ACTION,
    NOTIFY_WINNER,
    NOTIFY_SCORES,

    ASK_MARKET_DEST,
    ASK_SETUP_RESOURCES,
    ASK_MULTIPLE_CONVERSION,

    SHOW_TURN_HELP;

    private final boolean usableInLobbyManager;
    private final boolean usableInLobby;
    private final boolean usableInGame;
    private final Status whereToProcess;

    /**
     * Received Command
     */
    Command(boolean canBeUsedInLobbyManager,boolean canBeUsedInLobby, boolean canBeUsedInGame, Status status){
        this.usableInLobbyManager=canBeUsedInLobbyManager;
        this.usableInLobby=canBeUsedInLobby;
        this.usableInGame=canBeUsedInGame;
        this.whereToProcess=status;

    }

    /**
     * Reply Command
     */
    Command(){
        this.usableInLobbyManager = false;
        this.usableInLobby = false;
        this.usableInGame = false;
        whereToProcess = null;
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
