package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.parser.DevelopmentCardParser;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Util {


    //MAP AND LIST CONVERSION-------------------------------------------------------------------------------------------
    /**
     * Converts a list into a map <br>
     * It can also be used to add a list to a non empty map
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
    //------------------------------------------------------------------------------------------------------------------


    //CHECKS------------------------------------------------------------------------------------------------------------
    /**
     * Checks if a specific ResourceType is present in the HashMap
     * @param type is the key that will be used to check in the HashMap
     * @return true if present, false otherwise
     */
    public static <E,T> boolean isPresent(E type, HashMap<E, T> map){
        return map.containsKey(type);
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER------------------------------------------------------------------------------------------------------------
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
    //------------------------------------------------------------------------------------------------------------------


    //UTILITY ----------------------------------------------------------------------------------------------------------
    public static String mapToString (HashMap<ResourceType, ResourceContainer> inputMap) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;

        for(ResourceType key : inputMap.keySet()){
            stringBuilder.append(inputMap.get(key).getQty()).append(" ").append(key);

            i++;

            if(i < inputMap.size())
                stringBuilder.append(" + ");
        }

        return stringBuilder.toString();
    }
    //------------------------------------------------------------------------------------------------------------------

}
