package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.commands.Message;

public interface ObservableViewIO {

    /**
     * Adds an active player to the list of connected players
     * @return true if the player is successfully added, false if it fails if there's another player connected <br>
     * with his name.
     */
    boolean addPlayer(String nick, ObserverViewIO threadSender);

    /**
     * @return true if nick is present
     */
     boolean isNamePresent(String nick);

    /**
     * Removes a player from the list of connected players
     * @return true if the player is present and is successfully removed, false if a Player is not present <br>
     * and can't be removed.
     */
    boolean removePlayer(String nick);

    void notifyIO(Message message);

}
