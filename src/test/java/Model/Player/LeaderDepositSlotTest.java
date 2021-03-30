package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class LeaderDepositSlotTest {

    @Test
    void canAddToDepositSlotTest(ResourceContainer inputCont) {
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,2);
        ResourceContainer aCoin= new ResourceContainer(ResourceType.GOLD, 1);
        ResourceContainer aShield= new ResourceContainer(ResourceType.SHIELD, 1);

        assertAll(()->lds.canAddToDepositSlot(aCoin));

        assertThrows(DifferentResourceType.class, ()-> lds.canAddToDepositSlot(aShield));
        aCoin.addQta(4);
        assertThrows(DepositSlotMaxDimExceeded.class, ()->lds.canAddToDepositSlot(aCoin));
        aShield.addQta(5);
        assertThrows(DifferentResourceType.class, ()->lds.canAddToDepositSlot(aShield));
    }
    @Test
    void canRemoveFromDepositSlotTest (ResourceContainer inputCont){
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,5);
        inputCont.setResourceType(ResourceType.GOLD);
        inputCont.setQty(3);
        assertAll(()->lds.canRemoveFromDepositSlot(inputCont));

    }

}

