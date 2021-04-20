package model.cards;

import model.player.Player;
import model.resources.ResourceContainer;
import model.resources.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductionAbilityTest {
    @Test
    void productionAbility1(){
        LeaderCard bakerLeader= new LeaderCard(8);
        ArrayList<ResourceContainer> inputCard= new ArrayList<>();
        ArrayList<ResourceContainer> outputCard= new ArrayList<>();
        inputCard.add(new ResourceContainer(ResourceType.GOLD,1));
        outputCard.add(new ResourceContainer(ResourceType.MINION,1));
        ProductionAbility bake=new ProductionAbility(inputCard,outputCard,1,1);
        bakerLeader.addAbility(bake);
        Player gianEnrico= new Player("ing.Conti");
        bakerLeader.executeAbility(gianEnrico.getPlayerBoard());
        assertEquals(gianEnrico.getPlayerBoard().getProductionSite().getProductionSlotByID(4).getProductionInput().get(0).getResourceType(), ResourceType.GOLD);
        assertEquals(gianEnrico.getPlayerBoard().getProductionSite().getProductionSlotByID(4).getProductionOutput().get(0).getResourceType(), ResourceType.MINION);

        assertThrows(NullPointerException.class, ()-> gianEnrico.getPlayerBoard().getProductionSite().getProductionSlotByID(5).getProductionInput().get(0).getResourceType());

    }

    @Test
    void productionAbility2() {
        LeaderCard bakerLeader= new LeaderCard(8);
        ArrayList<ResourceContainer> inputCard1= new ArrayList<>();
        ArrayList<ResourceContainer> outputCard1= new ArrayList<>();
        inputCard1.add(new ResourceContainer(ResourceType.GOLD,1));
        outputCard1.add(new ResourceContainer(ResourceType.MINION,1));
        ProductionAbility bake=new ProductionAbility(inputCard1,outputCard1,1,1);
        bakerLeader.addAbility(bake);
        LeaderCard cookLeader= new LeaderCard(5);
        ArrayList<ResourceContainer> inputCard2= new ArrayList<>();
        ArrayList<ResourceContainer> outputCard2= new ArrayList<>();
        inputCard2.add(new ResourceContainer(ResourceType.STONE,1));
        outputCard2.add(new ResourceContainer(ResourceType.GOLD,1));
        ProductionAbility cook=new ProductionAbility(inputCard2,outputCard2,0,0);
        bakerLeader.addAbility(cook);
        Player gianEnrico= new Player("ing.Conti");
        bakerLeader.executeAbility(gianEnrico.getPlayerBoard());
        cookLeader.executeAbility(gianEnrico.getPlayerBoard());

        assertEquals(gianEnrico.getPlayerBoard().getProductionSite().getProductionSlotByID(4).getProductionInput().get(0).getResourceType(), ResourceType.GOLD);
        assertEquals(gianEnrico.getPlayerBoard().getProductionSite().getProductionSlotByID(4).getProductionOutput().get(0).getResourceType(), ResourceType.MINION);
        assertEquals(gianEnrico.getPlayerBoard().getProductionSite().getProductionSlotByID(5).getProductionInput().get(0).getResourceType(), ResourceType.STONE);
        assertEquals(gianEnrico.getPlayerBoard().getProductionSite().getProductionSlotByID(5).getProductionOutput().get(0).getResourceType(), ResourceType.GOLD);

        assertThrows(NullPointerException.class, ()-> gianEnrico.getPlayerBoard().getProductionSite().getProductionSlotByID(6).getProductionInput().get(0).getResourceType());

    }

}


