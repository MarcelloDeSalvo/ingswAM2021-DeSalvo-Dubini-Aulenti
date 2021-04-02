package Model.Player;

import Model.Exceptions.MultipleConversionsActive;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ConversionSiteTest {

    @Test
    void convertTest1 (){
        ConversionSite conversionSite= new ConversionSite();
        conversionSite.addConversion(new ResourceContainer(ResourceType.STONE,2));
        ArrayList<ResourceContainer> marketInput= new ArrayList<>();
        marketInput.add(new ResourceContainer(ResourceType.GOLD,1));
        marketInput.add(new ResourceContainer(ResourceType.BLANK,1));
        marketInput.add(new ResourceContainer(ResourceType.MINION,1));
        marketInput.add(new ResourceContainer(ResourceType.BLANK,1));
        System.out.println(conversionSite.getConversionsAvailable().size());


        assertAll(()->conversionSite.canConvert());
        conversionSite.convertSingleBlank(marketInput.get(1),conversionSite.getConversionsAvailable().get(0));
        conversionSite.convertSingleBlank(marketInput.get(3),conversionSite.getConversionsAvailable().get(0));
        assertEquals(marketInput.get(0).getResourceType(),ResourceType.GOLD);
        assertEquals(marketInput.get(1).getResourceType(),ResourceType.STONE);
        assertEquals(marketInput.get(2).getResourceType(),ResourceType.MINION);
        assertEquals(marketInput.get(3).getResourceType(),ResourceType.STONE);
        assertEquals(marketInput.get(3).getQty(), 2);
    }

    @Test
    void convertTest2 (){
        ConversionSite conversionSite= new ConversionSite();
        assertDoesNotThrow(()->conversionSite.canConvert());
        conversionSite.addConversion(new ResourceContainer(ResourceType.STONE,2));
        conversionSite.addConversion(new ResourceContainer(ResourceType.GOLD,1));
        ArrayList<ResourceContainer> marketInput= new ArrayList<>();
        marketInput.add(new ResourceContainer(ResourceType.BLANK,1));
        assertThrows(MultipleConversionsActive.class, ()->conversionSite.canConvert());

    }

}