package it.polimi.ingsw.view;


import it.polimi.ingsw.observers.ObservableViewIO;
import it.polimi.ingsw.observers.ObserverViewIO;

public abstract class View implements ObservableViewIO, ObserverViewIO {

    public abstract void askMove();
    public abstract void increasePos();

}
