package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepositTest {

    @Test
    void switchDeposit() {
        Deposit deposit = new Deposit(3);
        LeaderDepositSlot lds = new LeaderDepositSlot(ResourceType.STONE,2);

        deposit.addDepositSlot(lds);
        assertTrue(deposit.getStorage().contains(lds));
        int indexOfLds = deposit.getStorage().indexOf(lds);
        ResourceContainer r_max = new ResourceContainer(ResourceType.STONE, 3);
        ResourceContainer r = new ResourceContainer(ResourceType.STONE, 2);

        try {
            if(lds.canAddtoDepositSlot(r_max)){
                deposit.getStorage().get(indexOfLds).addToDepositSlot(r_max);

            }
        }catch (DepositSlotMaxDimExceeded d){
            assertEquals("Maximum dimension exceeded", d.getMessage());
        }catch (DifferentResourceType diff){
            assertEquals("Not the same type", diff.getMessage());
        }finally {
            assertTrue(deposit.getStorage().get(indexOfLds).getDepositResourceType().equals(ResourceType.STONE));
        }

        try {
            if(lds.canAddtoDepositSlot(r)){
                assertTrue(deposit.getStorage().get(indexOfLds).addToDepositSlot(r));
            }
        }catch (DepositSlotMaxDimExceeded d){
            assertEquals("Maximum dimension exceeded", d.getMessage());
        }catch (DifferentResourceType diff){
            assertEquals("Not the same type", diff.getMessage());
        }finally {
            assertTrue(deposit.getStorage().get(indexOfLds).getDepositResourceType().equals(ResourceType.STONE));
        }

        try {
            if(deposit.canSwitchDeposit(lds,1, deposit.getStorage().get(0))) {
                assertTrue(deposit.switchDeposit(lds, 1, deposit.getStorage().get(0)));
            }
        }catch (DepositSlotMaxDimExceeded d){
            assertEquals("Maximum dimension exceeded", d.getMessage());
        }catch (DifferentResourceType differentResourceType){
            assertEquals(differentResourceType.getMessage(),"Not the same type");

        }catch (NotEnoughResources notEnoughResources){
            assertEquals(notEnoughResources.getMessage(),"Not enough resources");
        }



    }

    @Test
    void pyramid3() {
        Deposit deposit = new Deposit(3);

        assertEquals(deposit.getStorage().get(0).getMaxDim(), 1);
        assertEquals(deposit.getStorage().get(1).getMaxDim(), 2);
        assertEquals(deposit.getStorage().get(2).getMaxDim(), 3);
        assertEquals(deposit.getStorage().size(), 3);
    }

    @Test
    void pyramid5() {
        Deposit deposit = new Deposit(5);

        assertEquals(deposit.getStorage().get(0).getMaxDim(), 1);
        assertEquals(deposit.getStorage().get(1).getMaxDim(), 2);
        assertEquals(deposit.getStorage().get(2).getMaxDim(), 3);
        assertEquals(deposit.getStorage().get(3).getMaxDim(), 4);
        assertEquals(deposit.getStorage().get(4).getMaxDim(), 5);
        assertEquals(deposit.getStorage().size(), 5);
    }


}