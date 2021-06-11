package it.polimi.ingsw.network;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.server.Status;
import it.polimi.ingsw.network.server.User;

public interface CommandPermission {

    /**
     * Checks if the user has a specific level of permission
     * @param user user to check
     * @return true if this is the case, false otherwise
     */
    boolean hasPermission(User user, Command command);

    /**
     * Gets the Area Status in order to process a command
     */
    Status getAreaStatus();
}
