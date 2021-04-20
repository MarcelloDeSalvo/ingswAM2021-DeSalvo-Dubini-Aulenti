package model.cards;

import model.player.Player;
import model.resources.ResourceContainer;
import model.resources.ResourceType;
import model.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaderCardTest {

    LeaderCard genericLeader = new LeaderCard(8);
    Player nick = new Player("nick");


    @Test
    void adds_three_leader_deposits(){
        StoreAbility storeAbility_1 = new StoreAbility(ResourceType.STONE, 2);
        StoreAbility storeAbility_2 = new StoreAbility(ResourceType.MINION, 2);
        StoreAbility storeAbility_3 = new StoreAbility(ResourceType.GOLD, 2);

        assertAll(()-> genericLeader.addAbility(storeAbility_1));
        assertAll(()-> genericLeader.addAbility(storeAbility_2));
        assertAll(()-> genericLeader.addAbility(storeAbility_3));
    }

    @Test
    void add_one_Development_Requirement_one_green_lv1(){
        DevelopmentRequirement developmentRequirement = new DevelopmentRequirement(1, 1,Colour.GREEN);
        assertAll(()->genericLeader.addRequirement(developmentRequirement));
    }

    @Test
    void add_one_green_lv1_card() {
        assertTrue(nick.insertBoughtCardOn(1,Util.getCardWithVpColour(1,Colour.GREEN)));
    }


    @Test
    void add_one_Gold_Requirement(){
        ResourceRequirement resourceRequirement = new ResourceRequirement(new ResourceContainer((ResourceType.GOLD),1));
        assertTrue(genericLeader.addRequirement(resourceRequirement));
    }

    @Test
    void add_3_gold(){
        nick.getPlayerBoard().getDepositSlotWithDim(3).addToDepositSlot(new ResourceContainer((ResourceType.GOLD),3));
    }

    @Test
    void addAbility_1() {
        StoreAbility storeAbility = new StoreAbility(ResourceType.STONE, 2);
        assertAll(()-> genericLeader.addAbility(storeAbility));
        assertEquals(genericLeader.getAbilities().size(),1);
    }

    @Test
    void addAbility_2() {
        StoreAbility storeAbility = new StoreAbility(ResourceType.STONE, 2);
        DiscountAbility discountAbility = new DiscountAbility(ResourceType.STONE, 2);
        assertAll(()-> genericLeader.addAbility(storeAbility));
        assertAll(()-> genericLeader.addAbility(discountAbility));
        assertEquals(genericLeader.getAbilities().size(),2);
    }

    @Test
    void addAbility_null() {
        StoreAbility storeAbility = null;
        assertAll(()->genericLeader.addAbility(storeAbility));
    }

    @Test
    void changeStatus() {
        assertTrue(genericLeader.changeStatus(Status.ACTIVE));
        assertEquals(genericLeader.getStatus(), Status.ACTIVE);
    }

    @Test
    void executeAbility() {
        adds_three_leader_deposits();

        assertTrue(genericLeader.executeAbility(nick.getPlayerBoard()));
        assertEquals(nick.getPlayerBoard().getLeaderDepositNumberX(1).getDepositResourceType(), ResourceType.STONE);
        assertEquals(nick.getPlayerBoard().getLeaderDepositNumberX(2).getDepositResourceType(), ResourceType.MINION);
        assertEquals(nick.getPlayerBoard().getLeaderDepositNumberX(3).getDepositResourceType(), ResourceType.GOLD);

    }

    @Test
    void checkRequirements_true() {
        add_one_Development_Requirement_one_green_lv1();
        add_one_Gold_Requirement();

        add_3_gold();
        add_one_green_lv1_card();

        assertTrue(genericLeader.checkRequirements(nick.getPlayerBoard()));
    }



    @Test
    void checkRequirements_false() {
        add_one_Development_Requirement_one_green_lv1();
        add_one_Gold_Requirement();

        add_one_green_lv1_card();

        assertFalse(genericLeader.checkRequirements(nick.getPlayerBoard()));
    }
}