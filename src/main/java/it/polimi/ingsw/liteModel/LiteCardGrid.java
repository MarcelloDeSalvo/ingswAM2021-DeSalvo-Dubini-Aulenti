package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.view.cli.Color;
import java.util.ArrayList;
import java.util.List;

public class LiteCardGrid {
    private final List<Integer> cardIDs;
    private final List<DevelopmentCard> developmentCards;

    public LiteCardGrid(List<Integer> initialGrid, List<DevelopmentCard> developmentCards){
        this.developmentCards=developmentCards;
        cardIDs = new ArrayList<>();
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

    public void printGridIDs(){
        StringBuilder IDGrid = new StringBuilder();
        IDGrid.append(Color.ANSI_CYAN.escape()).append("CARD IDs:").append(Color.ANSI_RESET.escape()).append("\n");
        IDGrid.append("\t");
        int c=-1;
        for(int i=3; i>=0; i--) {
            for (int j = 0; j < 4; j++) {
                if(i == 3) {
                    IDGrid.append("\t").append("\t").append("\t").append(developmentCards.get((j * 12) + 1).getColour().toString()).append("\t");
                }
                else{
                    if(j == 0)
                        IDGrid.append("LEVEL: ").append(i+1).append("\t").append("|").append("\t").append("\t");
                    if(cardIDs.get(c+j*3)!= -1)
                        IDGrid.append(cardIDs.get(c+j*3)).append("\t").append("\t").append("|").append("\t").append("\t");
                    else
                        IDGrid.append(Color.ANSI_RED.escape()).append("X").append(Color.ANSI_RESET.escape()).append("\t").append("\t").append("|").append("\t").append("\t");
                }
            }
            c++;
            IDGrid.append("\n");
        }
        System.out.println(IDGrid.toString());
    }

    public ArrayList<Integer> getGUICardIDs(){
        ArrayList<Integer> shuffledIDs=new ArrayList<>();

        for(int i=0; i<3; i++) {
            for (int j = 0; j < 4; j++) {
                shuffledIDs.add(cardIDs.get(i + (3 * j)));
            }
        }
        return shuffledIDs;
    }

    @Override
    public String toString() {
        StringBuilder cardGrid = new StringBuilder("\n");
        cardGrid.append(Color.ANSI_CYAN.escape()).append("CARDS:").append(Color.ANSI_RESET.escape()).append("\n");

        for (Integer id : cardIDs) {
            if (id.equals(-1))
                cardGrid.append("\n").append(Color.ANSI_RED.escape()).append("EMPTY").append(Color.ANSI_RESET.escape()).append("\n");
            else{
                cardGrid.append(developmentCards.get(id-1));
                cardGrid.append("\n");
            }
        }
        return cardGrid.toString();
    }
}
