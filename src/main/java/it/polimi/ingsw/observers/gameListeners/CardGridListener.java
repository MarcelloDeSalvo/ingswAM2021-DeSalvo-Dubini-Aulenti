package it.polimi.ingsw.observers.gameListeners;


public interface CardGridListener {
    /**
     * Notifies that a change happened in the CardGrid
     * @param oldID old DevelopmentCard Id
     * @param newID new DevelopmentCard Id (the one that substitute the old one)
     */
    void notifyCardGridChanges(int oldID, int newID);
}
