package it.polimi.ingsw.observers;

public interface ObservableModel {
    void notifyView();
    void addView(ObserverModel view);
}
