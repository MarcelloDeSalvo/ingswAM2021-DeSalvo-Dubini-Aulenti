package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.parser.DevelopmentCardParser;
import it.polimi.ingsw.view.cli.Color;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class LiteCardGrid {
    private ArrayList<Integer> cardIDs;
    private final ArrayList<DevelopmentCard> developmentCards;

    public LiteCardGrid(ArrayList<Integer> initialGrid, ArrayList<DevelopmentCard> developmentCards){
        this.developmentCards=developmentCards;
        cardIDs= new ArrayList<>();
        cardIDs.addAll(initialGrid);
    }

    /**
     * Removes from the gridIDs the passedID and replaces it with the appropriate DevelopmentCard
     */
    public boolean gridUpdated(Integer deletedID, Integer newID) {
            int pos=cardIDs.indexOf(deletedID);
            cardIDs.remove(deletedID);
            if(newID!=0)
                cardIDs.add(pos, newID);
            return true;
    }



    /* depends on how Gui works
    /**
     * @returns an arraylist of the current DevelopmentCards
     **/
    /* depends on how Gui works
    public ArrayList<DevelopmentCard> getCardGrid(){
        ArrayList <DevelopmentCard> currentGrid=new ArrayList<>();
        for (Integer id:cardIDs.keySet()) {
            currentGrid.add(cardIDs.get(id));
        }
        return currentGrid;
    }*/

    public ArrayList<Integer> getCardIDs() {
        return cardIDs;
    }

    @Override
    public String toString() {
        StringBuilder cardGrid = new StringBuilder("\n");
        for (Integer id : cardIDs) {
            if (id.equals(-1))
                cardGrid.append("\n").append(Color.ANSI_GREEN.escape()).append("EMPTY").append(Color.ANSI_RESET.escape()).append("\n");
            else{
                cardGrid.append(developmentCards.get(id-1));
                cardGrid.append("\n");
            }
        }
        return cardGrid.toString();
    }
}
