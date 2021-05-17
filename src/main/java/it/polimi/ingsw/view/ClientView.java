package it.polimi.ingsw.view;


import it.polimi.ingsw.liteModel.LiteHand;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.parser.LeaderCardParser;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObserverController;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public abstract class ClientView implements View, UserInput {
    private String nickname;
    private final ArrayList<ObserverController> observerControllers;

    private final ArrayList<LeaderCard> leaderCards;
    private LiteHand hand;

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ClientView() throws FileNotFoundException {
        leaderCards = LeaderCardParser.deserializeLeaderList();
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

    @Override
    public void update(String message) {
        //OFFLINE NOT YET IMPLEMENTED
        readUpdates(message);
    }
    //------------------------------------------------------------------------------------------------------------------


    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setHand(LiteHand hand) {
        this.hand = hand;
    }

    public LiteHand getHand() {
        return hand;
    }
}
