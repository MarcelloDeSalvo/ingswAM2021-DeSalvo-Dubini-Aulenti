package it.polimi.ingsw.view;


import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObserverController;

import java.util.ArrayList;

public abstract class ClientView implements View, UserInput {
    private String nickname;
    private final ArrayList<ObserverController> observerControllers;


    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ClientView(){
        observerControllers = new ArrayList<>();
    }

    @Override
    public void send(Message mex){
        String stringToSend = mex.serialize();
        notifyController(stringToSend);
    }


    //OBSERVERS --------------------------------------------------------------------------------------------------------
    @Override
    public void addObserverController(ObserverController observerController) {
        if(observerController!=null)
            observerControllers.add(observerController);
    }

    @Override
    public void notifyController(String message) {
        for (ObserverController obs: observerControllers) {
            obs.update(message);
        }
    }

    /**
     * Receives offline data from the controller
     * @param message is the message received
     */
    @Override
    public void update(String message) {
        //OFFLINE NOT YET IMPLEMENTED
        readUpdates(message);
    }
    //------------------------------------------------------------------------------------------------------------------
}
