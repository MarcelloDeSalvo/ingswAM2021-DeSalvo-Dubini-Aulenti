package it.polimi.ingsw.view;


import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.*;

import java.net.Socket;
import java.util.ArrayList;

public class VirtualView implements ObservableViewIO, ObserverViewIO, ObservableController, ObserverModel {

    ArrayList<ObserverViewIO> observerViewIOS;
    ArrayList<ObserverController> observerControllers;

    public VirtualView() {
        observerViewIOS = new ArrayList<>();
        observerControllers = new ArrayList<>();
    }

    @Override
    public void update(Message mex, Socket socket){
        Command command = mex.getCommand();

        switch (command){
            case QUIT:
                notifyIO_unicast(new Message(Command.REPLY, "Bye!"), socket);
                break;
            case HELLO:
                notifyIO_unicast(new Message(Command.REPLY, "Hello!"), socket);
                break;

            default:
                notifyController(mex);
                break;
        }
    }


    @Override
    public void notifyIO_unicast(Message message, Socket socket) {

    }

    @Override
    public void notifyIO_broadcast(Message message) {

    }

    @Override
    public void addObserverIO(ObserverViewIO observer) {
        if(observer!=null)
            observerViewIOS.add(observer);
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

    @Override
    public void showBoard() {

    }

    @Override
    public void increaseReply(String path) {

    }

    @Override
    public void update(Message message) {

    }

    @Override
    public Socket getSocket() {
        return null;
    }
}