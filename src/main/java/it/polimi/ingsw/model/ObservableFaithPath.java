package it.polimi.ingsw.model;


public interface ObservableFaithPath {

    void addObserver(ObserverFaithPath observer);

    void removeObserver(ObserverFaithPath observer);

    /**
     * Notifies all the Observer that a certain amount of FAITHPOINTs need to be added to FaithPath
     * @param faithPoints qty of FAITHPOINTs
     */
    void notifyFaithPath(int faithPoints);
}
