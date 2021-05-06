package it.polimi.ingsw.network;

import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.server.User;

import java.util.HashMap;

public class UserManager {


    //PLAYERSLIST MANAGEMENT -------------------------------------------------------------------------------------------
    public static void notifyUsers(HashMap<String, User> connectedPlayers, Message message) {
        switch (message.getTarget()){
            case UNICAST:
                if (isNamePresent(connectedPlayers, message.getSenderNickname())){
                    connectedPlayers.get(message.getSenderNickname()).userSend(message);
                }

                break;

            case BROADCAST:
                for (String nick: connectedPlayers.keySet()) {
                    connectedPlayers.get(nick).userSend(message);
                }
                break;

            case EVERYONE_ELSE:
                for (String nick: connectedPlayers.keySet()) {
                    if (!nick.equals(message.getSenderNickname()))
                        connectedPlayers.get(nick).userSend(message);
                }
                break;
        }
    }
    /**
     * Adds an active player to a map of players players
     * @return true if the player is successfully added, false if it fails if there's another player connected <br>
     * with his name.
     */
    public static boolean addPlayer(HashMap<String, User> players, String nick, User user){
        if(!players.containsKey(nick)) {
            players.put(nick, user);
            return true;
        }
        else
            return false;
    }

    /**
     * @return true if nick is present
     */
    public static <T> boolean isNamePresent(HashMap<String, T> connectedPlayers, String nick) {
        return connectedPlayers.containsKey(nick);
    }

    /**
     * Removes a player from the list of connected players
     * @return true if the player is present and is successfully removed, false if a Player is not present <br>
     * and can't be removed.
     */
    public static boolean removePlayer(HashMap<String, User> connectedPlayers, String nick) throws NullPointerException{
        if (connectedPlayers.containsKey(nick)) {
            connectedPlayers.remove(nick);
            return true;
        }
        return false;
    }
}
