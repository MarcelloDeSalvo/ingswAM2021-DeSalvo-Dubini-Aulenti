package Model.Resources;

import Model.Player.Deposit.Deposit;
import Model.Player.Vault;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTypeTest {

    @Test
    void goldToDepositTest(){
        ResourceContainer aCoin= new ResourceContainer(ResourceType.GOLD,1);
        ResourceContainer aFP= new ResourceContainer(ResourceType.FAITHPOINT,1);

        assertTrue(aCoin.getResourceType().canAddToDeposit());
        assertTrue(aCoin.getResourceType().canAddToVault());
        assertFalse(aCoin.getResourceType().canAddToFaithPath());
        Deposit deposit= new Deposit(2);
        Vault vault=new Vault();
        assertTrue(deposit.getDefaultSlot_WithDim(1).addToDepositSlot(aCoin));
        assertTrue(vault.addToVault(aCoin));
        assertFalse(aFP.getResourceType().addToDeposit(aFP,deposit.getDefaultSlot_WithDim(2)));
        assertFalse(aFP.getResourceType().addToVault(aFP,vault));


    }


}