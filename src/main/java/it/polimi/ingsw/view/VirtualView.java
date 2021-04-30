package it.polimi.ingsw.view;


import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.*;

import java.util.ArrayList;

public class VirtualView implements ObservableViewIO, ObserverViewIO, ObservableController, ObserverModel {

    ArrayList<ObserverViewIO> observerViewIOS;
    ArrayList<ObserverController> observerControllers;

    public VirtualView() {
        observerViewIOS = new ArrayList<>();
        observerControllers = new ArrayList<>();
    }

    @Override
    public void update(Message mex){
        Command command = mex.getCommand();

        switch (command){
            case QUIT:
                notifyIO(new Message(Command.REPLY, "Bye!"));
                break;
            case HELLO:
                notifyIO(new Message(Command.REPLY, "Hello!"));
                break;

            default:
                notifyController(mex);
                break;
        }

    }

    @Override
    public void showBoard(){
        notifyIO(new Message(Command.SHOW, "show"));
    }
    @Override
    public void increaseReply(String path){
        notifyIO(new Message(Command.REPLY, path));
    }


    @Override
    public void addObserverIO(ObserverViewIO observer) {
        if(observer!=null)
            observerViewIOS.add(observer);
    }

    @Override
    public void notifyIO(Message message) {
        for (ObserverViewIO obs: observerViewIOS) {
            obs.update(message);
        }
    }

    @Override
    public void addObserverController(ObserverController obs) {
        if(obs!=null)
            observerControllers.add(obs);
    }

    @Override
    public void notifyController(Message message) {
        for (ObserverController obs: observerControllers) {
            obs.update(message);
        }
    }

    @Override
    public boolean readInput() {
        return true;
    }

}