package Model;


import Model.Parser.MarketSetUpParser;
import Model.Player.Player;
import Model.Resources.ResourceContainer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

public class Game implements ObserverEndGame{

    public static Player currentPlayer;

    private int numOfPlayers;
    private ArrayList<Player> playerList;
    private Market market;
    private Cardgrid cardgrid;
    private Deck leaderDeck;
    private boolean endGame;


    public void Game() throws  FileNotFoundException {
        playerList = new ArrayList<>();
        for (int i= 0; i<4; i++){
            playerList.add(new Player("default",3,3, i ));
        }

        try {
            ArrayList<ResourceContainer> marketMarbles = MarketSetUpParser.deserializeMarketElements();
            market = new Market(marketMarbles);
            cardgrid = new Cardgrid();


        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        endGame = false;
    }


    //GAME CREATION----------------------------------------------------------------------------------------------------
    public boolean createStandardRules(ArrayList<String> playersNicknames){

        playerList = new ArrayList<>();
        int i = 0;
        Iterator<String> iter = playersNicknames.iterator();
        String current;
        while(iter.hasNext()){
            current=iter.next();
            playerList.add(new Player(current,i));
            i++;
        }
        return true;
    }

    public boolean createCustomRules(ArrayList<String> playersNicknames, String confFilePath ){
        playerList = new ArrayList<>();
        int i = 0;
        Iterator<String> iter = playersNicknames.iterator();
        String current;
        while(iter.hasNext()){
            current=iter.next();
            playerList.add(new Player(current,i));
            i++;
        }
        return true;
    }
    //-----------------------------------------------------------------------------------------------------------------


    //OBSERVER METHODS---(end game notify)-----------------------------------------------------------------------------
    @Override
    public void update() {
        endGame = true;
    }
    //-----------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER------------------------------------------------------------------------------------------------
    public boolean isEndGame() {
        return endGame;
    }
    //-----------------------------------------------------------------------------------------------------------------

}
