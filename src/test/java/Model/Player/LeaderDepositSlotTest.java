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
    void canAddToDepositSlotTest(ResourceContainer inputCont) throws DifferentResourceType,DepositSlotMaxDimExceeded{
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,2);
        ResourceContainer aCoin= new ResourceContainer(ResourceType.GOLD, 1);
        ResourceContainer aShield= new ResourceContainer(ResourceType.SHIELD, 1);

        assertTrue(lds.canAddtoDepositSlot(aCoin));

        assertThrows(DifferentResourceType.class, ()-> lds.canAddtoDepositSlot(aShield));
        aCoin.addQta(4);
        assertThrows(DepositSlotMaxDimExceeded.class, ()->lds.canAddtoDepositSlot(aCoin));
        aShield.addQta(5);
        assertThrows(DifferentResourceType.class, ()->lds.canAddtoDepositSlot(aShield));
    }
    @Test
    void canRemoveFromDepositSlotTest (ResourceContainer inputCont) throws NotEnoughResources, DifferentResourceType {
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,5);
        inputCont.setResourceType(ResourceType.GOLD);
        inputCont.setQta(3);
        assertTrue(lds.canRemoveFromDepositSlot(inputCont));







    }

    }

