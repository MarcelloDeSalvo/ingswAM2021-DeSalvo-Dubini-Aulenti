package Model;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    @Test
    void getCardWithVpTest1(){
        DevelopmentCard myCard= Util.getCardWithVpColour(9, Colour.YELLOW);
        assertEquals(myCard.getPrice().get(0).getResourceType(), ResourceType.STONE);
        assertEquals(myCard.getPrice().get(0).getQty(), 6);
    }
    @Test
    void getCardWithVpTest2(){
        DevelopmentCard myCard= Util.getCardWithVpColour(1, Colour.PURPLE);
        assertEquals(myCard.getPrice().get(0).getResourceType(), ResourceType.MINION);
        assertEquals(myCard.getPrice().get(0).getQty(), 2);

    }

}