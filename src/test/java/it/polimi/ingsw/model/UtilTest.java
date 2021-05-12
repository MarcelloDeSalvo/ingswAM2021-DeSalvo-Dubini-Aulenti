package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    @Test
    void getCardWithVpTest1(){
        assertNotNull(Util.getCardWithVpColour(9, Colour.YELLOW));
        DevelopmentCard myCard= Util.getCardWithVpColour(9, Colour.YELLOW);

        assertEquals(myCard.getPrice().get(0).getResourceType(), ResourceType.STONE);
        assertEquals(myCard.getPrice().get(0).getQty(), 6);
        assertEquals(myCard.getInput().get(0).getResourceType(),ResourceType.SHIELD);
        assertEquals(myCard.getInput().get(0).getQty(),2);
        assertEquals(myCard.getOutput().get(0).getResourceType(),ResourceType.MINION);
        assertEquals(myCard.getOutput().get(0).getQty(),3);
        assertEquals(myCard.getOutput().get(1).getResourceType(),ResourceType.FAITH);
        assertEquals(myCard.getOutput().get(1).getQty(),2);
    }
    @Test
    void getCardWithVpTest2(){
        assertNotNull(Util.getCardWithVpColour(1, Colour.PURPLE));
        DevelopmentCard myCard= Util.getCardWithVpColour(1, Colour.PURPLE);

        assertEquals(myCard.getPrice().get(0).getResourceType(), ResourceType.MINION);
        assertEquals(myCard.getPrice().get(0).getQty(), 2);
        assertEquals(myCard.getInput().get(0).getResourceType(),ResourceType.STONE);
        assertEquals(myCard.getInput().get(0).getQty(),1);
        assertEquals(myCard.getOutput().get(0).getResourceType(),ResourceType.FAITH);
        assertEquals(myCard.getOutput().get(0).getQty(),1);

    }

    @Test
    void arraylistToMap() {

        ArrayList<ResourceContainer> list = new ArrayList<>();

        list.add(new ResourceContainer(ResourceType.STONE, 2));
        list.add(new ResourceContainer(ResourceType.GOLD, 1));
        list.add(new ResourceContainer(ResourceType.MINION, 3));
        list.add(new ResourceContainer(ResourceType.GOLD, 2));
        list.add(new ResourceContainer(ResourceType.STONE, 3));


        HashMap<ResourceType, ResourceContainer> controlMap = new HashMap<>();

        controlMap.put(ResourceType.STONE, new ResourceContainer(ResourceType.STONE, 5));
        controlMap.put(ResourceType.GOLD, new ResourceContainer(ResourceType.GOLD, 3));
        controlMap.put(ResourceType.MINION, new ResourceContainer(ResourceType.MINION, 3));


        HashMap<ResourceType, ResourceContainer> map = Util.arraylistToMap(list);

        assertEquals(controlMap, map);


        controlMap.put(ResourceType.SHIELD, new ResourceContainer(ResourceType.SHIELD, 3));

        assertNotEquals(controlMap, map);

    }

}