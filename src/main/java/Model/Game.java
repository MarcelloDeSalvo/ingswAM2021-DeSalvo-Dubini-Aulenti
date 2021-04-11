package Model;

import Model.Player.Deposit.DepositSlot;
import Model.Player.Player;

import java.util.ArrayList;
import java.util.Iterator;

public class Game {
    private int numOfPlayers;
    private ArrayList<Player> playerList;
    private Player currentPlayer;
    private Market market;
    private Cardgrid cardgrid;
    private Deck leaderDeck;


    public boolean createGame(ArrayList<String> players){
        Iterator<String> iter = players.iterator();
        String current;
        while(iter.hasNext()){
            current=iter.next();
            playerList.add(new Player(current));
        }
        return true;
    }

}
