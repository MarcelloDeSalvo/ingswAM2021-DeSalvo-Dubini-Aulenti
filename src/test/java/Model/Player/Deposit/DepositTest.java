package Model.Player.Deposit;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.NotEnoughResources;
import Model.Exceptions.ResourceTypeAlreadyStored;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepositTest {

    @BeforeEach
    void clearStaticSet(){
        DefaultDeposit clear = new DefaultDeposit(1);
        clear.clearSet();
    }

    @Test
    void pyramid3() {
        Deposit deposit = new Deposit(3);

        assertEquals(deposit.getDepositList().get(0).getMaxDim(), 1);
        assertEquals(deposit.getDepositList().get(1).getMaxDim(), 2);
        assertEquals(deposit.getDepositList().get(2).getMaxDim(), 3);
        assertEquals(deposit.getDepositList().size(), 3);
    }

    @Test
    void pyramid5() {
        Deposit deposit = new Deposit(5);

        assertEquals(deposit.getDepositList().get(0).getMaxDim(), 1);
        assertEquals(deposit.getDepositList().get(1).getMaxDim(), 2);
        assertEquals(deposit.getDepositList().get(2).getMaxDim(), 3);
        assertEquals(deposit.getDepositList().get(3).getMaxDim(), 4);
        assertEquals(deposit.getDepositList().get(4).getMaxDim(), 5);
        assertEquals(deposit.getDepositList().size(), 5);
    }

    @Test
    void addDepositSlot() {
        Deposit deposit = new Deposit(3);

        LeaderDeposit lds = new LeaderDeposit(ResourceType.STONE,2);
        assertTrue(deposit.addDepositSlot(lds));
        assertTrue(deposit.getDepositList().contains(lds));
    }

    @Test
    void removeDepositSlot() {
    }

    @Test
    void moveTo_1(){
        Deposit deposit = new Deposit(3);

        LeaderDeposit lds1 = new LeaderDeposit(ResourceType.STONE,2);
        lds1.addToDepositSlot(new ResourceContainer(ResourceType.STONE, 2));

        LeaderDeposit lds2= new LeaderDeposit(ResourceType.SHIELD,2);
        lds2.addToDepositSlot(new ResourceContainer(ResourceType.SHIELD, 2));

        assertAll(()->deposit.moveTo(lds1,1, deposit.getDefaultSlot_WithDim(3)));
        assertAll(()->deposit.moveTo(lds2,2, deposit.getDefaultSlot_WithDim(2)));
        assertAll(()->deposit.moveTo(deposit.getDefaultSlot_WithDim(3), 1, deposit.getDefaultSlot_WithDim(1)));
        assertAll(()->deposit.moveTo(deposit.getDefaultSlot_WithDim(2), 2, deposit.getDefaultSlot_WithDim(3)));

        assertEquals(lds1.getResourceQty(), 1);
        assertEquals(lds2.getResourceQty(), 0);
        assertEquals(deposit.getDefaultSlot_WithDim(1).getResourceQty(), 1);
        assertEquals(deposit.getDefaultSlot_WithDim(2).getResourceQty(), 0);
        assertEquals(deposit.getDefaultSlot_WithDim(3).getResourceQty(), 2);

        assertThrows(ResourceTypeAlreadyStored.class,()->deposit.getDefaultSlot_WithDim(2).canAddToDepositSlot(new ResourceContainer(ResourceType.STONE,2)));
        assertAll(()->deposit.moveTo(deposit.getDefaultSlot_WithDim(1), 1, deposit.getDefaultSlot_WithDim(2)));
        assertEquals(deposit.getDefaultSlot_WithDim(2).getResourceQty(), 1);
    }

    @Test
    void moveTo_2(){
        Deposit deposit = new Deposit(3);

        LeaderDeposit lds1 = new LeaderDeposit(ResourceType.STONE,2);
        lds1.addToDepositSlot(new ResourceContainer(ResourceType.STONE, 2));

        LeaderDeposit lds2= new LeaderDeposit(ResourceType.SHIELD,2);
        lds2.addToDepositSlot(new ResourceContainer(ResourceType.SHIELD, 2));

        assertAll(()->deposit.moveTo(lds1,1, deposit.getDefaultSlot_WithDim(3)));
        assertAll(()->deposit.moveTo(lds2,2, deposit.getDefaultSlot_WithDim(2)));
        assertAll(()->deposit.moveTo(deposit.getDefaultSlot_WithDim(3), 1, deposit.getDefaultSlot_WithDim(1)));
        assertAll(()->deposit.moveTo(deposit.getDefaultSlot_WithDim(2), 2, deposit.getDefaultSlot_WithDim(3)));

    }


    @Test
    void canTransferDeposit_1() {
        Deposit deposit = new Deposit(3);

        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD,2);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD,2));

        assertTrue(deposit.getDepositList().get(0).isEmpty());
        assertThrows(DepositSlotMaxDimExceeded.class, ()-> deposit.canTransferDeposit(lds,2, deposit.getDepositList().get(0)));
        assertAll( ()-> deposit.canTransferDeposit(lds,2, deposit.getDepositList().get(1)));
    }

    @Test
    void canTransferDeposit_2() {
        Deposit deposit = new Deposit(3);
        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD,2);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD,2));

        assertThrows(NotEnoughResources.class, ()-> deposit.canTransferDeposit(lds,3, deposit.getDepositList().get(0)));
    }


    @Test
    void canTransferDeposit_3() {
        Deposit deposit = new Deposit(3);

        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD,2);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD,2));

        deposit.getDefaultSlot_WithDim(3).addToDepositSlot(new ResourceContainer(ResourceType.GOLD, 2));
        assertEquals(deposit.getDefaultSlot_WithDim(3).getResourceQty(), 2);

        assertAll(()-> deposit.canTransferDeposit(lds,1,deposit.getDefaultSlot_WithDim(3)));
        assertTrue(deposit.transferToDeposit(lds,1,deposit.getDefaultSlot_WithDim(3)));

        assertEquals(lds.getResourceQty(), 1);
        assertEquals(deposit.getDefaultSlot_WithDim(3).getResourceQty(), 3);
    }


    @Test
    void canTransferDeposit_4() {
        Deposit deposit = new Deposit(3);
        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD,2);
        deposit.addDepositSlot(lds);
        deposit.getDefaultSlot_WithDim(2).addToDepositSlot(new ResourceContainer(ResourceType.GOLD, 2));
        assertTrue(deposit.transferToDeposit(deposit.getDefaultSlot_WithDim(2),2, deposit.getDepositList().get(deposit.getDepositList().indexOf(lds))));
        assertEquals(lds.getResourceQty(),2);
        assertEquals(deposit.getDefaultSlot_WithDim(2).getResourceQty(),0);
        deposit.getDefaultSlot_WithDim(2).addToDepositSlot(new ResourceContainer(ResourceType.GOLD,1));
        assertThrows(DepositSlotMaxDimExceeded.class,()->deposit.canTransferDeposit(deposit.getDefaultSlot_WithDim(2),1, deposit.getDepositList().get(deposit.getDepositList().indexOf(lds))));
    }

    @Test
    void canSwitchDeposit(){
        Deposit deposit = new Deposit(3);

        ResourceContainer container1 = new ResourceContainer(ResourceType.STONE, 1);
        ResourceContainer container2 = new ResourceContainer(ResourceType.GOLD, 1);
        deposit.getDefaultSlot_WithDim(1).addToDepositSlot(container1);
        deposit.getDefaultSlot_WithDim(2).addToDepositSlot(container2);

        assertAll(()->deposit.canSwitchDeposit( deposit.getDefaultSlot_WithDim(1),  deposit.getDefaultSlot_WithDim(2)));
        assertAll(()->deposit.moveTo(deposit.getDefaultSlot_WithDim(1),1, deposit.getDefaultSlot_WithDim(2)));
        assertAll(()->deposit.switchToDeposit(deposit.getDefaultSlot_WithDim(1), deposit.getDefaultSlot_WithDim(2)));

    }

    @Test
    void TypeAlreadyStored_1(){
        Deposit deposit = new Deposit(3);

        ResourceContainer container1 = new ResourceContainer(ResourceType.GOLD, 1);
        ResourceContainer container2 = new ResourceContainer(ResourceType.GOLD, 2);
        assertAll(()->deposit.getDefaultSlot_WithDim(1).canAddToDepositSlot(container1));
        assertAll(()->deposit.getDefaultSlot_WithDim(2).addToDepositSlot(container2));
        assertThrows(ResourceTypeAlreadyStored.class, ()->deposit.getDefaultSlot_WithDim(1).canAddToDepositSlot(container1));
    }

    @Test
    void TypeAlreadyStored_2(){
        Deposit deposit = new Deposit(3);
        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD, 1);
        ResourceContainer container1 = new ResourceContainer(ResourceType.GOLD, 1);
        ResourceContainer container2 = new ResourceContainer(ResourceType.GOLD, 2);
        lds.addToDepositSlot(container1);
        assertAll(()->deposit.getDefaultSlot_WithDim(2).addToDepositSlot(container2));
        assertThrows(ResourceTypeAlreadyStored.class, ()->deposit.moveTo(lds,1,deposit.getDefaultSlot_WithDim(1)));
    }
    @Test
    void checkDeposit(){
        Deposit deposit = new Deposit(2);
        deposit.getDefaultSlot_WithDim(2).addToDepositSlot(new ResourceContainer(ResourceType.GOLD,2));
        assertEquals(deposit.checkDeposit(ResourceType.GOLD),2);
        LeaderDeposit lds = new LeaderDeposit(ResourceType.GOLD, 2);
        lds.addToDepositSlot(new ResourceContainer(ResourceType.GOLD,2));
        deposit.addDepositSlot(lds);
        assertEquals(deposit.checkDeposit(ResourceType.GOLD),4);
    }

}