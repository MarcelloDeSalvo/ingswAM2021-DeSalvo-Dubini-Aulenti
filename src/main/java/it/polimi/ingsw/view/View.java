package it.polimi.ingsw.view;


import it.polimi.ingsw.observers.FaithPathListener;
import it.polimi.ingsw.observers.ObservableController;
import it.polimi.ingsw.observers.ObserverModel;
import it.polimi.ingsw.observers.ObserverViewIO;

public interface View extends ObserverModel, ObserverViewIO, ObservableController, FaithPathListener {



}
