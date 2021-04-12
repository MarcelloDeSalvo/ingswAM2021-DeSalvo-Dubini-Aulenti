package Model;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Exceptions.InvalidColumnNumber;
import Model.Exceptions.InvalidRowNumber;
import Model.Parser.DevelopmentCardParser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;


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
                if(!deckGrid[i][j].getDeck().isEmpty()) {
                    System.out.println(deckGrid[i][j].getDeck().peek().getColour());
                    System.out.println("Level: " + deckGrid[i][j].getDeck().peek().getLevel());
                    System.out.println("Victory points: " + deckGrid[i][j].getDeck().peek().getVictoryPoints());
                    System.out.println();
                }
            }
        }
    }

    /**
     * This method returns the current card on top of its respective deck. The deck is chosen by the parameters: Colour and level
     * @return null if the card isn't found
     */
    public DevelopmentCard getDevelopmentCardOnTop( Colour desiredColour, int desiredLevel) {
            for (int i = 0; i < columns; i++) {
                for (int j = 0; j < rows; j++) {
                    if (!deckGrid[i][j].getDeck().isEmpty() && deckGrid[i][j].getDeck().peek().getColour()==desiredColour && deckGrid[i][j].getDeck().peek().getLevel()==desiredLevel)
                        return( deckGrid[i][j].getDeck().peekFirst());
                }
            }
            return null;
    }

    /**
     * Removes a chosen card from the top of a deck in the cardgrid.
     * @return false if a deck with the chosen parameters isn't found or is empty
     */
    public boolean removeDevelopmentCard(Colour desiredColour, int desiredLevel){
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (!deckGrid[i][j].getDeck().isEmpty()   && deckGrid[i][j].getDeck().peek().getColour()==desiredColour && deckGrid[i][j].getDeck().peek().getLevel()==desiredLevel) {
                    deckGrid[i][j].getDeck().remove();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns an entire deck from the card grid. The deck is selected with the parameters:Colour and Level
     * @return null if the deck isn't present
     */
    public Deck getDeckFromGrid(Colour desiredColour, int desiredLevel){
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (deckGrid[i][j]!=null && deckGrid[i][j].getDeck().peek().getColour()==desiredColour && deckGrid[i][j].getDeck().peek().getLevel()==desiredLevel)
                    return deckGrid[i][j];
            }
        }
        return null;
    }

    /**
     * This method returns the current card on top of its respective deck. The deck is chosen by the position on the grid defined by the two parameters
     * @return null if the card isn't found
     */
    public DevelopmentCard getDevelopmentCardOnTop( int rowNumber, int columnNumber) throws InvalidColumnNumber, InvalidRowNumber {
        if(rowNumber<=0)
            throw new InvalidRowNumber ("Selected row isn't valid");
        if(columnNumber<=0)
            throw new InvalidColumnNumber ("Selected column isn't valid");

        if (!deckGrid[columnNumber-1][rowNumber-1].getDeck().isEmpty() && deckGrid[columnNumber-1][rowNumber-1].getDeck()!=null)
            return( deckGrid[columnNumber-1][rowNumber-1].getDeck().peekFirst());
        return null;
    }

    /**
     * This method deletes the current card on top of its respective deck. The deck is chosen by the position on the grid defined by the two parameters
     */
    public boolean removeDevelopmentCard( int rowNumber, int columnNumber) throws InvalidColumnNumber, InvalidRowNumber {
        if(rowNumber<=0)
            throw new InvalidRowNumber ("Selected row isn't valid");
        if(columnNumber<=0)
            throw new InvalidColumnNumber ("Selected column isn't valid");

            if (!deckGrid[columnNumber-1][rowNumber-1].getDeck().isEmpty() && deckGrid[columnNumber-1][rowNumber-1].getDeck()!=null) {
            deckGrid[columnNumber-1][rowNumber-1].getDeck().remove();
            return true;
        }
        return false;
    }


     public void printGreenCardsLevel1(){
         deckGrid[0][0].getDeck().forEach(System.out::println);
     }

}
