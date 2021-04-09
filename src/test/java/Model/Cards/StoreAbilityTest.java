package Model.Cards;

import Model.Player.Player;
import Model.Player.PlayerBoard;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

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
        assertEquals(f_PlayerBoard.getDepositSlotByID(3).getMaxDim(), 3);
        assertEquals(f_PlayerBoard.getDepositSlotByID(3).getDepositResourceType(), ResourceType.GOLD);
    }

    @Test
    void useAbility_2() {
        assertTrue(leaderCardM.executeAbility(f_PlayerBoard));
        assertEquals(f_PlayerBoard.getDepositSlotByID(3).getMaxDim(), 9);
        assertEquals(f_PlayerBoard.getDepositSlotByID(3).getDepositResourceType(), ResourceType.MINION);
    }

    @Test
    void useAbility_1_AND_2() {
        int size = f_PlayerBoard.getDeposit().getDepositList().size();

        assertTrue(leaderCardM.executeAbility(f_PlayerBoard));
        assertEquals(f_PlayerBoard.getDeposit().getDepositList().size(), size+1);
        assertTrue(leaderCardG.executeAbility(f_PlayerBoard));
        assertEquals(f_PlayerBoard.getDeposit().getDepositList().size(), size+2);

        assertEquals(f_PlayerBoard.getDepositSlotByID(3).getMaxDim(), 9);
        assertEquals(f_PlayerBoard.getDepositSlotByID(4).getDepositResourceType(), ResourceType.GOLD);
    }

    @Test
    void doublePower(){

        int size = f_PlayerBoard.getDeposit().getDepositList().size();

        assertTrue(doublePower.executeAbility(f_PlayerBoard));
        assertEquals(f_PlayerBoard.getDeposit().getDepositList().size(), size+2);

        assertEquals(f_PlayerBoard.getDepositSlotByID(3).getDepositResourceType(), ResourceType.MINION);
        assertEquals(f_PlayerBoard.getDepositSlotByID(4).getDepositResourceType(), ResourceType.GOLD);

    }

}