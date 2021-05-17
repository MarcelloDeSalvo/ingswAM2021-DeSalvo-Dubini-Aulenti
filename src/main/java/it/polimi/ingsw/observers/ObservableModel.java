package it.polimi.ingsw.observers;

public interface ObservableModel {

    /**
     * Adds a view that observes the Model
     */
    void addView(ObserverModel view);


}
