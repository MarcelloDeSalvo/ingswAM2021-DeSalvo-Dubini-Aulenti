package it.polimi.ingsw.liteModel;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.parser.DevelopmentCardParser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class liteCardGrid {
    private HashMap<Integer, DevelopmentCard> cardIDs;
    private final ArrayList<DevelopmentCard> developmentCards;

    public liteCardGrid(ArrayList<Integer> initialGrid)throws FileNotFoundException {
        developmentCards= DevelopmentCardParser.deserializeDevelopmentList();
        for (Integer id:initialGrid) {
            cardIDs.put(id, developmentCards.get(id-1));
        }
    }

    /**
     * Removes from the gridIDs the passedID and replaces it with the appropriate DevelopmentCard
     */
    public boolean gridUpdated(int deletedID, int newID) {
        if (cardIDs.containsKey(deletedID)) {
            cardIDs.remove(deletedID);
            if(newID!=0)
                cardIDs.put(newID, developmentCards.get(newID - 1));
            return true;
        }
        System.out.println("An error occured with the card grid update");
        return false;
    }


    /**
     * @returns an arraylist of the current DevelopmentCards
     */
    public ArrayList<DevelopmentCard> getCardGrid(){
        ArrayList <DevelopmentCard> currentGrid=new ArrayList<>();
        for (Integer id:cardIDs.keySet()) {
            currentGrid.add(cardIDs.get(id));
        }
        return currentGrid;
    }


}
