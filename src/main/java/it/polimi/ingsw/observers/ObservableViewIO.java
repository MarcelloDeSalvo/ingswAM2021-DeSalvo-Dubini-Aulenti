package it.polimi.ingsw.observers;

import it.polimi.ingsw.network.ServerArea;
import it.polimi.ingsw.network.commands.Command;

public interface ObservableViewIO {

    /**
     * Notifies all the lobbies or the views (they are treated the same way) that are listening for an update
     */
    void notifyServerAreas(Command command, String message);

    /**
     * Adds a lobby or a view to the observers list
     */
    void addServerArea(ServerArea obs);

    /**
     * Removes a lobby or a view to the observers list
     */
    void removeServerArea(ServerArea serverArea);
}
