package Model.Cards;

import Model.Player.Player;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
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

}