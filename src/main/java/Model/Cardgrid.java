package Model;

import Model.Cards.DevelopmentCard;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Cardgrid {
    private Deck[][] deckGrid;
    static final int rows = 3;
    static final int columns = 4;


    public Cardgrid() {
        deckGrid = new Deck[columns][rows];
    }



    public boolean gridInsert(ArrayList<DevelopmentCard> cardList, int row_num, int column_num){
           if(row_num<=rows && column_num <= columns && row_num>=0 && column_num >=0) {
                Deck d = new Deck();
                d.randomInsert(cardList);
                deckGrid[column_num][row_num] = d;
                return true;
           }
           return false;
    }



}
