package Model.Player.Production;

import Model.Cards.*;
import Model.Deck;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class DevelopmentCardProduction implements ProductionSlot {
    private Deck dev;

    public DevelopmentCardProduction() {
        dev = new Deck();
    }

    @Override
    public int countCardsWith(int level, Colour c){
        int i = 0;
        for (DevelopmentCard d: dev.getDeck()) {
            if (d.isSameLevelandColour(level, c));
                i++;
        }


        return i;
    }
}
