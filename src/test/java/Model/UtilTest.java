package Model;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

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
        assertEquals(myCard.getOutput().get(1).getResourceType(),ResourceType.FAITHPOINT);
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
        assertEquals(myCard.getOutput().get(0).getResourceType(),ResourceType.FAITHPOINT);
        assertEquals(myCard.getOutput().get(0).getQty(),1);

    }

}