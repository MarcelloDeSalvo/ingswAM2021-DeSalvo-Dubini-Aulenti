package Model;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Cards.LeaderCard;
import Model.Parser.DevelopmentCardParser;
import Model.Player.Player;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

public class Util {

    static DevelopmentCard getCardWithVpColour (int victoryPointsRequested, Colour requestedColor){
        ArrayList<DevelopmentCard> cards ;
        try{
            cards = DevelopmentCardParser.deserializeDevelopmentList();
            Iterator<DevelopmentCard> iter = cards.iterator();
            DevelopmentCard current;
            while(iter.hasNext()){
                current=iter.next();
                if(current.getColour()==requestedColor && current.getVictoryPoints()==victoryPointsRequested)
                    return current;
            }
            System.out.println("Card not found");
            return null;

        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
}
