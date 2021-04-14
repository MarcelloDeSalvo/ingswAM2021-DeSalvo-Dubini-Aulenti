package Model.Resources;

import Model.FaithPath;
import Model.ObserverFaithPath;
import Model.Parser.FaithPathSetUpParser;
import Model.Player.Deposit.Deposit;
import Model.Player.Vault;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static Model.Parser.FaithPathSetUpParser.deserializeFaithPathSetUp;
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


    @Test
    void observerTest() throws FileNotFoundException {
        FaithPath faithPath = FaithPathSetUpParser.deserializeFaithPathSetUp(3);
        faithPath.setCurrentPlayer(0);

        ResourceContainer faithPoint = new ResourceContainer(ResourceType.FAITHPOINT, 1);
        ResourceContainer gold = new ResourceContainer(ResourceType.GOLD, 1);

        faithPoint.getResourceType().addObserver(faithPath);

        assertTrue(faithPoint.getResourceType().addToFaithPath(faithPoint));
        assertTrue(faithPoint.getResourceType().addToFaithPath(faithPoint));

        assertFalse(gold.getResourceType().addToFaithPath(gold));

        assertEquals(faithPath.getPositions().get(0), 2);


        //(??) funziona ma forse non ha molto senso, magari conviene spostare più in alto di ResourceType (es. in ResourceContainer)
        assertTrue(gold.getResourceType().addToFaithPath(faithPoint));
        assertEquals(faithPath.getPositions().get(0), 3);
    }
}