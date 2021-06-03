package it.polimi.ingsw.observers.gameListeners;

public interface ProductionSubject {
    /**
     * Adds a new ProductionListener
     */
    void addListeners(ProductionListener productionListener);
}
