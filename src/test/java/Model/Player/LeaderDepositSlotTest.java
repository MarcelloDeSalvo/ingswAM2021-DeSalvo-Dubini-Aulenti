package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Player.Deposit.LeaderDepositSlot;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaderDepositSlotTest {

    @Test
    void basicLeaderDepositConstructor(){
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,2);
        assertTrue(lds.getMaxDim()==2);
        assertTrue(lds.getDepositResourceType()==ResourceType.GOLD);
        assertTrue(lds.getResourceQty()==0);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD,1));
        assertTrue(lds.getResourceQty()==1);
    }

    @Test
    void canAddToDepositSlotTest() {
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,2);
        ResourceContainer aCoin= new ResourceContainer(ResourceType.GOLD, 1);
        ResourceContainer aShield= new ResourceContainer(ResourceType.SHIELD, 1);
        assertAll(()->lds.canAddToDepositSlot(aCoin));
        //lds.getDepositContainer().setQty(2);

        assertThrows(DifferentResourceType.class, ()-> lds.canAddToDepositSlot(aShield));
        aCoin.addQty(4);
        assertThrows(DepositSlotMaxDimExceeded.class, ()->lds.canAddToDepositSlot(aCoin));
        aShield.addQty(5);
        assertThrows(DifferentResourceType.class, ()->lds.canAddToDepositSlot(aShield));
    }

    @Test
    void canRemoveFromDepositSlotTest (){
        ResourceContainer inputCont= new ResourceContainer(ResourceType.BLANK, 0 );
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,5);
        lds.getDepositContainer().setQty(5);
        inputCont.setResourceType(ResourceType.GOLD);
        inputCont.setQty(3);
        assertAll(()->lds.canRemoveFromDepositSlot(inputCont));
        inputCont.addQty(5);
        assertThrows(NotEnoughResources.class, ()->lds.canRemoveFromDepositSlot(inputCont));
        inputCont.setResourceType(ResourceType.MINION);
        assertThrows(DifferentResourceType.class, ()->lds.canRemoveFromDepositSlot(inputCont));
    }

    @Test
    void canRemoveFromDepositSlotTest_1 (){
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,1);
        lds.getDepositContainer().setQty(1);
        ResourceContainer inputCont= new ResourceContainer(ResourceType.GOLD, 1 );
        assertAll(()->lds.canRemoveFromDepositSlot(inputCont));
        inputCont.addQty(-1);
        assertAll(()->lds.canRemoveFromDepositSlot(inputCont));
        inputCont.addQty(3);
        assertThrows(NotEnoughResources.class, ()->lds.canRemoveFromDepositSlot(inputCont));
        lds.setMaxDim(4);
    }

    @Test
    void addToDepositSlotTest(){
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,3);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD, 1));
        assertTrue(lds.getResourceQty()==1);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD, 2));
        assertTrue(lds.getResourceQty()==3);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.BLANK, 0));     //Adding empty blank containers doesn't crash
        assertTrue(lds.getResourceQty()==3);
    }

    @Test
    void removeFromDepositSlot(){
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,5);
        lds.getDepositContainer().setQty(5);
        lds.removeFromDepositSlot(new ResourceContainer(ResourceType.GOLD,0));
        assertTrue(lds.getResourceQty()==5);
        lds.removeFromDepositSlot(new ResourceContainer(ResourceType.GOLD,2));
        assertTrue(lds.getResourceQty()==3);
    }

    @Test
    void ldsComplexTest(){
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.GOLD,5);
        ResourceContainer bagoCoins=new ResourceContainer(ResourceType.GOLD,4);
        assertAll(()->lds.canAddToDepositSlot(bagoCoins));
        assertThrows(NotEnoughResources.class, ()->lds.canRemoveFromDepositSlot(bagoCoins));
        lds.addToDepositSlot(bagoCoins);
        bagoCoins.setQty(2);
        assertThrows(DepositSlotMaxDimExceeded.class, ()->lds.canAddToDepositSlot(bagoCoins));
        assertAll(()->lds.canRemoveFromDepositSlot(bagoCoins));
        lds.removeFromDepositSlot(bagoCoins);
        assertTrue(lds.getResourceQty()==2);
    }


}

