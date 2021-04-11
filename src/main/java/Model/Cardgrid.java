package Model;

import Model.Cards.DevelopmentCard;
import Model.Parser.DevelopmentCardParser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Cardgrid {
    private Deck[][] deckGrid;
    static final int rows = 3;
    static final int columns = 4;


   /* public Cardgrid() {
        deckGrid = new Deck[columns][rows];
    }*/

    public Cardgrid() throws FileNotFoundException {
        deckGrid = new Deck[columns][rows];
        ArrayList<DevelopmentCard> fileCards;
        int c=0;
        fileCards= DevelopmentCardParser.deserializeDevelopmentList();
        for(int i=0;i<columns;i++){
            for(int j=0;j<rows;j++){
                Deck tempDeck=new Deck();
                for(int z=0;z<4;z++){
                    tempDeck.getDeck().push(fileCards.get(c));
                    c++;
                }
                Collections.shuffle(tempDeck.getDeck());
                deckGrid[i][j]=tempDeck;
            }
        }
    }

    public void printGrid(){
        for(int i=0;i<columns;i++){
            for(int j=0;j<rows;j++){
                for(int z=0;z<4;z++) {
                    System.out.println(deckGrid[i][j].getDeck().get(z).getColour());
                    System.out.println(deckGrid[i][j].getDeck().get(z).getLevel());
                    System.out.println(deckGrid[i][j].getDeck().get(z).getVictoryPoints());
                    System.out.println();

                }System.out.println('\n');
            }
        }
    }


}
