package Model;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Parser.DevelopmentCardParser;
import Model.Player.Player;

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

    /**
     * Function used to print the cardgrid and manually check if everything is correct.
     */
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

    /**
     * This method returns the current card on top of its respective deck with the desired parametres
     * @return null if the card isn't found
     */
    public DevelopmentCard getDevelopmentCardOnTop( Colour desiredColour, int desiredLevel) {
            for (int i = 0; i < columns; i++) {
                for (int j = 0; j < rows; j++) {
                    if (deckGrid[i][j]!=null && deckGrid[i][j].getDeck().peek().getColour()==desiredColour && deckGrid[i][j].getDeck().peek().getLevel()==desiredLevel)
                        return( deckGrid[i][j].getDeck().peekFirst());
                }
            }
            return null;
    }

    /**
     * Removes a chosen card from the top of a deck in the cardgrid.
     * @returns false if a deck with the chosen parameters isn't found or is empty
     */
    public boolean removeDevelopmentCard(Colour desiredColour, int desiredLevel){
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (deckGrid[i][j]!=null && deckGrid[i][j].getDeck().peek().getColour()==desiredColour && deckGrid[i][j].getDeck().peek().getLevel()==desiredLevel)
                    deckGrid[i][j].getDeck().remove();
                    return true;
            }
        }
        return false;
    }

}
