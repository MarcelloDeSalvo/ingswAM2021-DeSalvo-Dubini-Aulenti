package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoreAbilityTest {

    LeaderCard leaderCardM = new LeaderCard(8);
    LeaderCard leaderCardG = new LeaderCard(8);
    StoreAbility storeAbilityMinion9 = new StoreAbility(ResourceType.MINION, 9);
    StoreAbility storeAbilityGold3 = new StoreAbility(ResourceType.GOLD, 3);

    LeaderCard doublePower = new LeaderCard(8);

    Player freddie = new Player("Freddie");
    PlayerBoard f_PlayerBoard = freddie.getPlayerBoard();

    @BeforeEach
    void initialization(){
        leaderCardM.addAbility(storeAbilityMinion9);
        leaderCardG.addAbility(storeAbilityGold3);

        doublePower.addAbility(storeAbilityMinion9);
        doublePower.addAbility(storeAbilityGold3);
    }

    @Test
    void useAbility_1() {
        assertTrue(leaderCardG.executeAbility(f_PlayerBoard));
        assertEquals( 3, f_PlayerBoard.getDepositSlotByID(4).getMaxDim());
        assertEquals( ResourceType.GOLD, f_PlayerBoard.getDepositSlotByID(4).getDepositResourceType());
    }

    @Test
    void useAbility_2() {
        assertTrue(leaderCardM.executeAbility(f_PlayerBoard));
        assertEquals( 9, f_PlayerBoard.getDepositSlotByID(4).getMaxDim());
        assertEquals( ResourceType.MINION, f_PlayerBoard.getDepositSlotByID(4).getDepositResourceType());
    }

    @Test
    void useAbility_1_AND_2() {
        int size = f_PlayerBoard.getDeposit().getDepositList().size();

        assertTrue(leaderCardM.executeAbility(f_PlayerBoard));
        assertEquals( size+1, f_PlayerBoard.getDeposit().getDepositList().size());
        assertTrue(leaderCardG.executeAbility(f_PlayerBoard));
        assertEquals( size+2, f_PlayerBoard.getDeposit().getDepositList().size());

        assertEquals(9, f_PlayerBoard.getDepositSlotByID(4).getMaxDim());
        assertEquals( ResourceType.GOLD, f_PlayerBoard.getDepositSlotByID(5).getDepositResourceType());
    }

    @Test
    void doublePower(){

        int size = f_PlayerBoard.getDeposit().getDepositList().size();

        assertTrue(doublePower.executeAbility(f_PlayerBoard));
        assertEquals(f_PlayerBoard.getDeposit().getDepositList().size(), size+2);

        assertEquals( ResourceType.MINION, f_PlayerBoard.getDepositSlotByID(4).getDepositResourceType());
        assertEquals( ResourceType.GOLD, f_PlayerBoard.getDepositSlotByID(5).getDepositResourceType());

    }

}