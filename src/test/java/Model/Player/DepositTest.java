package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
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
                deposit.getStorage().get(indexOfLds).addToDepositSlot(r);

            }
        }catch (DepositSlotMaxDimExceeded d){
            assertEquals("Maximum dimension exceeded", d.getMessage());
        }catch (DifferentResourceType diff){
            assertEquals("Not the same type", diff.getMessage());
        }finally {
            assertTrue(deposit.getStorage().get(indexOfLds).getDepositResourceType().equals(ResourceType.STONE));
        }



    }
}