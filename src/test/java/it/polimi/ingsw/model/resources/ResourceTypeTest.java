package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.deposit.Deposit;
import it.polimi.ingsw.model.player.Vault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTypeTest {

    @Test
    void goldToDepositTest(){
        ResourceContainer aCoin= new ResourceContainer(ResourceType.GOLD,1);
        ResourceContainer aFP= new ResourceContainer(ResourceType.FAITH,1);

        assertTrue(aCoin.getResourceType().canAddToDeposit());
        assertTrue(aCoin.getResourceType().canAddToVault());
        assertFalse(aCoin.getResourceType().canAddToFaithPath());

        Deposit deposit = new Deposit(2, true);
        Vault vault = new Vault(true);

        assertTrue(deposit.getDefaultSlot_WithDim(1).addToDepositSlot(aCoin));
        assertTrue(vault.addToVault(aCoin));
        assertFalse(aFP.addToDeposit(deposit.getDefaultSlot_WithDim(2)));
        assertFalse(aFP.addToVault(vault));
    }


    @Test
    void observerTest() throws FileNotFoundException {
        Game game = new Game();

        assertEquals(game.getNumOfPlayers(), 4);
        FaithPath faithPath = game.getFaithPath();
        assertEquals(faithPath.getLength(),25);
        assertEquals(faithPath.getPlayersFavourList().size(),4);
        ResourceContainer faithPoint = new ResourceContainer(ResourceType.FAITH, 1);
        ResourceContainer gold = new ResourceContainer(ResourceType.GOLD, 1);

        assertTrue(faithPoint.addToFaithPath(game.getFaithPath()));
        assertTrue(faithPoint.addToFaithPath(game.getFaithPath()));

        assertFalse(gold.addToFaithPath(game.getFaithPath()));

        assertEquals(faithPath.getPositions().get(0), 2);
    }
}