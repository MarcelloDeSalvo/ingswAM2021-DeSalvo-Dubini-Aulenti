package Model;

import Model.Cards.DevelopmentCard;
import Model.Parser.DevelopmentCardParser;
import Model.Parser.MarketSetUpParser;
import Model.Player.Deposit.DepositSlot;
import Model.Player.Player;
import Model.Resources.ResourceContainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

public class Game {
    private int numOfPlayers;
    private ArrayList<Player> playerList;
    private Player currentPlayer;
    private Market market;
    private Cardgrid cardgrid;
    private Deck leaderDeck;


    public void Game() throws  FileNotFoundException {
        playerList = new ArrayList<>();
        for (int i= 0; i<4; i++){
            playerList.add(new Player(("default")));
        }

        try {
            ArrayList<ResourceContainer> marketMarbles = MarketSetUpParser.deserializeMarketElements();
            market = new Market(marketMarbles);
            cardgrid = new Cardgrid();


        }catch(FileNotFoundException e){
            e.printStackTrace();
        }


    }



    public boolean createStandardRules(ArrayList<String> playersNicknames){

        playerList = new ArrayList<>();

        Iterator<String> iter = playersNicknames.iterator();
        String current;
        while(iter.hasNext()){
            current=iter.next();
            playerList.add(new Player(current));
        }
        return true;
    }

    public boolean createCustomRules(ArrayList<String> playersNicknames, String confFilePath ){
        playerList = new ArrayList<>();

        Iterator<String> iter = playersNicknames.iterator();
        String current;
        while(iter.hasNext()){
            current=iter.next();
            playerList.add(new Player(current));
        }
        return true;
    }


}
