package Model.Cards;

import Model.Player.Deposit.DefaultDepositSlot;
import Model.Player.Player;
import Model.Player.PlayerBoard;
import Model.Player.Production.LeaderCardProduction;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceRequirementTest {

    Player p = new Player("Eren");

    LeaderCard leaderGoldDisc =  new LeaderCard(1);
    Ability goldDiscount = new DiscountAbility(ResourceType.GOLD, 1);

    DevelopmentCard developmentCard = new DevelopmentCard(1,1,Colour.BLUE);
    ResourceContainer price = new ResourceContainer(ResourceType.GOLD, 5);



    @BeforeEach
    void createLeaderDiscountCards(){
        leaderGoldDisc.addAbility(goldDiscount);
        leaderGoldDisc.executeAbility(p.getPlayerBoard());

        developmentCard.addPrice(price);
    }

    @BeforeEach
    void clearStaticSet(){
        DefaultDepositSlot clear = new DefaultDepositSlot(1);
        clear.clearSet();
    }

    @Test
    void price_1(){

        p.getPlayerBoard().getDeposit().getDefaultSlot_WithDim(3).addToDepositSlot(new ResourceContainer(ResourceType.GOLD, 3));
        p.getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.GOLD, 2));

        for (Requirement r: developmentCard.getRequirements()) {
            r.checkRequirements(p.getPlayerBoard());
        }

        for (ResourceContainer r : developmentCard.getDiscountedPrice(p.getPlayerBoard())) {
               assertEquals(r.getQty(),4);
        }
    }

}