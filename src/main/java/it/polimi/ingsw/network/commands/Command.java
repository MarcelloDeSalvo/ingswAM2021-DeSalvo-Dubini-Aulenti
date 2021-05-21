package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.network.server.Status;
import it.polimi.ingsw.network.server.User;

public enum Command {

    PING            (true,true,true, Status.IN_LOBBY_MANAGER),
    PONG            (true,true,true, Status.IN_LOBBY_MANAGER),

    QUIT            (true,true,true, Status.IN_LOBBY_MANAGER),
    HELLO           (true,true,true, Status.IN_LOBBY_MANAGER),
    HELLO_ALL       (true,false,false, Status.IN_LOBBY_MANAGER),
    REPLY           (true,true,true,null),
    CHAT            (true,true,true, Status.IN_LOBBY_MANAGER),
    CHAT_ALL        (true,true,false, Status.IN_LOBBY_MANAGER),

    LOGIN           (false,false,false,null),
    CREATE_LOBBY    (true,false,false, Status.IN_LOBBY_MANAGER),
    JOIN_LOBBY      (true,false,false, Status.IN_LOBBY_MANAGER),
    LOBBY_LIST      (true,false,false, Status.IN_LOBBY_MANAGER),

    EXIT_LOBBY      (false,true,false, Status.IN_LOBBY),
    START_GAME      (false,true,false, Status.IN_LOBBY),
    GAME_SETUP      (false,false,false, Status.IN_GAME),
    PLAYER_LIST     (false, true, false, Status.IN_LOBBY),

    CHEAT_VAULT     (false,false,true, Status.IN_GAME),

    DISCARD_LEADER  (false,false,true, Status.IN_GAME),
    DISCARD_OK      (false,false,true, Status.IN_GAME),
    ACTIVATE_LEADER (false, false, true, Status.IN_GAME),
    ACTIVATE_OK     (false,false,true, Status.IN_GAME),
    BUY             (false, false, true, Status.IN_GAME),
    BUY_OK          (false,false,true, Status.IN_GAME),
    PRODUCE         (false, false, true, Status.IN_GAME),
    PRODUCE_OK         (false, false, true, Status.IN_GAME),
    MANAGE_DEPOSIT  (false, false, true, Status.IN_GAME),
    MANAGE_DEPOSIT_OK  (false, false, true, Status.IN_GAME),
    DONE            (false, false, true, Status.IN_GAME),
    FILL_QM         (false, false, true, Status.IN_GAME),
    PICK_FROM_MARKET(false, false,true, Status.IN_GAME ),
    SEND_DEPOSIT_ID (false, false,true, Status.IN_GAME ),
    SEND_CONTAINER  (false,false,true,Status.IN_GAME),
    SWITCH_DEPOSIT  (false,false,true,Status.IN_GAME),
    SETUP_CONTAINER (false, false, true, Status.IN_GAME),
    CONVERSION      (false, false, true, Status.IN_GAME),
    END_TURN        (false, false, true, Status.IN_GAME),

    END_GAME        (false, true, false, Status.IN_LOBBY),

    NOTIFY_CARDGRID             (false, false, true, Status.IN_GAME),
    NOTIFY_DEPOSIT_UPDATE       (false, false, true, Status.IN_GAME),
    NOTIFY_NEW_DEPOSIT          (false, false, true, Status.IN_GAME),
    NOTIFY_VAULT_UPDATE         (false, false, true, Status.IN_GAME),
    NOTIFY_NEW_PRODSLOT         (false, false, true, Status.IN_GAME),
    NOTIFY_HAND                 (false,false,true,Status.IN_GAME),
    NOTIFY_FAITHPATH_CURRENT    (false,false,true,Status.IN_GAME),
    NOTIFY_FAITHPATH_OTHERS     (false,false,true,Status.IN_GAME),
    NOTIFY_FAITHPATH_FAVOURS    (false,false,true,Status.IN_GAME),

    ASK_FOR_RESOURCES (false, false, false, null),

    SHOW_PLAYER         (false, false, true, Status.IN_GAME),
    SHOW_ORDER          (false, false, true, Status.IN_GAME),

    SHOW_TURN_HELP  (false,false,true,Status.IN_GAME);


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
