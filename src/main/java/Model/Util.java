package Model;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Parser.DevelopmentCardParser;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Util {


    /**
     * Returns a specific Development Card (victoryPoints and Colour)
     */
    public static DevelopmentCard getCardWithVpColour (int victoryPointsRequested, Colour requestedColor){
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


    /**
     * Converts a list into a map
     */
    public static boolean arraylistToMap (ArrayList<ResourceContainer> list, HashMap<ResourceType, ResourceContainer> map){
        Iterator<ResourceContainer> iterator= list.iterator();
        ResourceContainer current;
        while(iterator.hasNext()){
            current=iterator.next();
            if(isPresent(current.getResourceType(), map)){
                map.get(current.getResourceType()).addQty(current.getQty());
            }
            else
                map.put(current.getResourceType(),new ResourceContainer(current.getResourceType(),current.getQty()));
        }
        return true;
    }

    /**
     * Converts a list to a Map
     * @return a Map containing the converted list
     */
    public static HashMap<ResourceType, ResourceContainer> arraylistToMap (ArrayList<ResourceContainer> list) {
        HashMap<ResourceType, ResourceContainer> map = new HashMap<>();
        if (arraylistToMap(list, map))
            return map;
        return null;
    }

    /**
     * checks if a specific ResourceType is present in the HashMap
     * @param type is the key that will be used to check in the HashMap
     * @return true if present, false otherwise
     */
    public static boolean isPresent(ResourceType type, HashMap<ResourceType, ResourceContainer> map){
        return map.containsKey(type);
    }
}
