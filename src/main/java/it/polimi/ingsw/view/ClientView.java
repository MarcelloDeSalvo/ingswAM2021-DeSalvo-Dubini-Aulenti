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
import java.util.HashMap;

public abstract class ClientView implements View, UserInput {
    private String nickname;
    private final ArrayList<ObserverController> observerControllers;

    private final ArrayList<LeaderCard> leaderCards;
    private final ArrayList<DevelopmentCard> developmentCards;

    private final LiteFaithPath liteFaithPath;
    private LiteCardGrid liteCardGrid;
    private LiteMarket liteMarket;

    private HashMap<String,LitePlayerBoard> liteBoards;



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

    public void litePlayerBoardsSetUp(ArrayList<String> nicknames){
        this.liteBoards=new HashMap<>();
        for (String nic:nicknames) {
            liteBoards.put(nic,new LitePlayerBoard(leaderCards, developmentCards));
        }
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


    //GETTER AND SETTER METHODS FOR LITE MODEL--------------------------------------------------------------------------
    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() { return developmentCards; }

    public void setMyHand(LiteHand hand) {
        liteBoards.get(nickname).setLiteHand(hand);
    }

   public LiteHand getMyHand() {
        return liteBoards.get(nickname).getLiteHand();
    }

    public LiteVault getMyLiteVault() {
        return liteBoards.get(nickname).getLiteVault();
    }

    public LiteDeposit getMyLiteDeposit() {
        return  liteBoards.get(nickname).getLiteDeposit();
    }

    public LiteHand getSomeonesHand(String nickname){ return liteBoards.get(nickname).getLiteHand();}

    public LiteVault getSomeonesLiteVault(String nickname) { return liteBoards.get(nickname).getLiteVault();  }

    public LiteDeposit getSomeonesLiteDeposit(String nickname) {return  liteBoards.get(nickname).getLiteDeposit(); }

    public LiteProduction getSomeonesLiteProduction(String nickname) { return liteBoards.get(nickname).getLiteProduction(); }

    public LiteProduction getMyLiteProduction() {  return liteBoards.get(nickname).getLiteProduction(); }

    public LitePlayerBoard getLitePlayerBoard(String nickname){   return liteBoards.get(nickname); }

    public LiteMarket getLiteMarket(){return liteMarket;}

    public void setLiteMarket(LiteMarket liteMarket) { this.liteMarket = liteMarket; }

    public LiteCardGrid getLiteCardGrid() { return liteCardGrid; }

    public void setLiteCardGrid(LiteCardGrid liteCardGrid) { this.liteCardGrid = liteCardGrid; }

    public LiteFaithPath getLiteFaithPath() { return liteFaithPath;  }


}
