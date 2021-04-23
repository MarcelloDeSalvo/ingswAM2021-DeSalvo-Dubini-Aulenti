package it.polimi.ingsw.model.player.deposit;

import it.polimi.ingsw.model.exceptions.DepositSlotMaxDimExceeded;
import it.polimi.ingsw.model.exceptions.DifferentResourceType;
import it.polimi.ingsw.model.exceptions.NotEnoughResources;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaderDepositTest {

    @Test
    void basicLeaderDepositConstructor(){
        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD,2);
        assertTrue(lds.getMaxDim()==2);
        assertTrue(lds.getDepositResourceType()==ResourceType.GOLD);
        assertTrue(lds.getResourceQty()==0);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD,1));
        assertTrue(lds.getResourceQty()==1);
    }

    @Test
    void canAddToDepositSlotTest() {
        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD,2);
        ResourceContainer aCoin= new ResourceContainer(ResourceType.GOLD, 1);
        ResourceContainer aShield= new ResourceContainer(ResourceType.SHIELD, 1);
        assertAll(()->lds.canAddToDepositSlot(aCoin));
        //lds.getDepositContainer().setQty(2);

        assertThrows(DifferentResourceType.class, ()-> lds.canAddToDepositSlot(aShield));
        aCoin.addQty(4);
        assertThrows(DepositSlotMaxDimExceeded.class, ()->lds.canAddToDepositSlot(aCoin));
        aShield.addQty(5);
        assertThrows(DifferentResourceType.class, ()->lds.canAddToDepositSlot(aShield));
        assertThrows(DifferentResourceType.class, ()->lds.canAddToDepositSlot(new ResourceContainer(ResourceType.BLANK,0)));

    }

    @Test
    void canRemoveFromDepositSlotTest (){
        ResourceContainer inputCont= new ResourceContainer(ResourceType.BLANK, 0 );
        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD,5);
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
        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD,1);
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
        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD,3);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD, 1));
        assertTrue(lds.getResourceQty()==1);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD, 2));
        assertTrue(lds.getResourceQty()==3);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.BLANK, 0));     //Adding empty blank containers doesn't crash
        assertTrue(lds.getResourceQty()==3);

    }

    @Test
    void removeFromDepositSlot(){
        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD,5);
        lds.getDepositContainer().setQty(5);
        lds.removeFromDepositSlot(new ResourceContainer(ResourceType.GOLD,0));
        assertTrue(lds.getResourceQty()==5);
        lds.removeFromDepositSlot(new ResourceContainer(ResourceType.GOLD,2));
        assertTrue(lds.getResourceQty()==3);
    }

    @Test
    void ldsComplexTest(){
        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD,5);
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

