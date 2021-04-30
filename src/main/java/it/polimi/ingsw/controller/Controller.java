package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.MoveMessage;
import it.polimi.ingsw.observers.ObserverController;
import it.polimi.ingsw.view.VirtualView;

public class Controller implements ObserverController {

    VirtualView virtualView;

    public Controller(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    @Override
    public void update(Message mex) {
        Command command = mex.getCommand();
        Gson gson = new Gson();
        String originale = mex.serialize();

        switch (command){

            case MOVE:
                MoveMessage move = gson.fromJson(originale, MoveMessage.class);
                moveController(move);
                break;

            default:
                System.out.println("comando invalido");
                break;
        }
    }

    void moveController(MoveMessage moveMessage){
        if (moveMessage.getX()<0){
            System.out.println("comando invalido");
        }else{
            //cambia stato del model
        }

    }
}