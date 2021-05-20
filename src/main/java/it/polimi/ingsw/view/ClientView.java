package it.polimi.ingsw.view;


import it.polimi.ingsw.liteModel.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.parser.DevelopmentCardParser;
import it.polimi.ingsw.model.parser.FaithPathSetUpParser;
import it.polimi.ingsw.model.parser.LeaderCardParser;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObserverController;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public abstract class ClientView implements View, UserInput {
    private String nickname;
    private final ArrayList<ObserverController> observerControllers;

    private final ArrayList<LeaderCard> leaderCards;
    private final ArrayList<DevelopmentCard> developmentCards;

    private final LiteFaithPath liteFaithPath;
    private LiteHand hand;
    private LiteCardGrid liteCardGrid;
    private LiteVault liteVault;
    private LiteMarket liteMarket;
    private LiteDeposit liteDeposit;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ClientView() throws FileNotFoundException {
        leaderCards = LeaderCardParser.deserializeLeaderList();
        developmentCards = DevelopmentCardParser.deserializeDevelopmentList();
        liteFaithPath = FaithPathSetUpParser.deserializeLiteFaithPathSetUp();
        observerControllers = new ArrayList<>();
    }

    @Override
    public void send(Message mex){
        String stringToSend = mex.serialize();
        notifyController(stringToSend, null, null);
    }


    //OBSERVERS --------------------------------------------------------------------------------------------------------
    @Override
    public void addObserverController(ObserverController observerController) {
        if(observerController!=null)
            observerControllers.add(observerController);
    }

    @Override
    public void notifyController(String mex, Command command, String senderNick) {
        for (ObserverController obs: observerControllers) {
            obs.update(mex,command, senderNick);
        }
    }

    @Override
    public void update(String mex, Command command, String senderNick) {
        //OFFLINE NOT YET IMPLEMENTED
        readUpdates(mex);
    }
    //------------------------------------------------------------------------------------------------------------------


    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() { return developmentCards; }

    public void setHand(LiteHand hand) {
        this.hand = hand;
    }

    public LiteHand getHand() {
        return hand;
    }

    public LiteCardGrid getLiteCardGrid() { return liteCardGrid; }

    public void setLiteCardGrid(LiteCardGrid liteCardGrid) { this.liteCardGrid = liteCardGrid; }

    public LiteFaithPath getLiteFaithPath() { return liteFaithPath;  }

    public LiteVault getLiteVault() {
        return liteVault;
    }

    public LiteDeposit getLiteDeposit() {
        return liteDeposit;
    }

    public void setLiteDeposit(LiteDeposit liteDeposit) {
        this.liteDeposit = liteDeposit;
    }

    public void setLiteVault(LiteVault liteVault) {
        this.liteVault = liteVault;
    }

    public LiteMarket getLiteMarket(){return liteMarket;}

    public void setLiteMarket(LiteMarket liteMarket) { this.liteMarket = liteMarket; }
}
