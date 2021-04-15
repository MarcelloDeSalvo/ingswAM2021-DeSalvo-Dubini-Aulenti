package Model.Cards;

import Model.Player.Player;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiscountAbilityTest {
    LeaderCard discoLeader= new LeaderCard(8);
    DiscountAbility disco=new DiscountAbility(ResourceType.GOLD,2);

    @BeforeEach
    void createCard(){
        discoLeader.addAbility(disco);
    }

    @Test
    void discountAbility1 (){

        Player jhon=new Player("ElToro69");
        DevelopmentCard cardIReallyWant= new DevelopmentCard(8,1,Colour.YELLOW);
        cardIReallyWant.addPrice(new ResourceContainer(ResourceType.GOLD, 7));
        discoLeader.executeAbility(jhon.getPlayerBoard());
        //System.out.println(cardIReallyWant.getDiscountedPrice(jhon.getPlayerBoard()));
        assertEquals(cardIReallyWant.getPrice().get(0).getQty()- disco.getDiscount(), cardIReallyWant.getDiscountedPrice(jhon.getPlayerBoard()).get(0).getQty());

    }

    @Test
    void discountAbility2 (){
        DiscountAbility noodle=new DiscountAbility(ResourceType.MINION,1);
        discoLeader.addAbility(noodle);
        Player jhon=new Player("ElRatton420");
        DevelopmentCard cardThatWinsMeGame= new DevelopmentCard(8,1,Colour.YELLOW);
        cardThatWinsMeGame.addPrice(new ResourceContainer(ResourceType.GOLD,5));
        cardThatWinsMeGame.addPrice(new ResourceContainer(ResourceType.MINION, 6));
        discoLeader.executeAbility(jhon.getPlayerBoard());
        ArrayList<Integer> sconti=new ArrayList<>();
        sconti.add(disco.getDiscount());
        sconti.add(noodle.getDiscount());
        int i=0;
        //System.out.println(cardThatWinsMeGame.getDiscountedPrice(jhon.getPlayerBoard()).toString());
        for (ResourceContainer resourseCont: cardThatWinsMeGame.getDiscountedPrice(jhon.getPlayerBoard())) {
            assertEquals(cardThatWinsMeGame.getPrice().get(i).getQty()-sconti.get(i), resourseCont.getQty());
            i++;
        }

    }

    @Test
    void discountAbilityMaxDiscount (){
        Player alessandro=new Player("Margara");
        DevelopmentCard cardThatIsPrettyCheap= new DevelopmentCard(8,1,Colour.YELLOW);
        cardThatIsPrettyCheap.addPrice(new ResourceContainer(ResourceType.GOLD,2));
        discoLeader.executeAbility(alessandro.getPlayerBoard());
        assertEquals(cardThatIsPrettyCheap.getDiscountedPrice(alessandro.getPlayerBoard()).get(0).getQty(), 0);
    }

    @Test
    void discountAbilityOverDiscount (){
        Player player=new Player("Who lives in a pineapple under the sea? SPONGEBOB SQUAREPANTS");
        DevelopmentCard cardThatIsPrettyCheap= new DevelopmentCard(8,1,Colour.YELLOW);
        cardThatIsPrettyCheap.addPrice(new ResourceContainer(ResourceType.GOLD,1));
        discoLeader.executeAbility(player.getPlayerBoard());
        assertEquals(cardThatIsPrettyCheap.getDiscountedPrice(player.getPlayerBoard()).get(0).getQty(), 0);
    }

    @Test
    void discountAbilityDifferentType (){
        Player alessandro=new Player("Margara");
        DevelopmentCard cardThatIsPrettyCheap= new DevelopmentCard(8,1,Colour.YELLOW);
        cardThatIsPrettyCheap.addPrice(new ResourceContainer(ResourceType.MINION,1));
        discoLeader.executeAbility(alessandro.getPlayerBoard());
        assertEquals(cardThatIsPrettyCheap.getDiscountedPrice(alessandro.getPlayerBoard()).get(0).getQty(), 1);
    }



}