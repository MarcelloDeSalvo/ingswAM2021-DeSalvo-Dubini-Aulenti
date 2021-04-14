package Model.Player;

import Model.Exceptions.MultipleConversionsActive;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ConversionSiteTest {

    @Test
    void convertSingleBlankTest (){
        ConversionSite conversionSite = new ConversionSite();
        conversionSite.addConversion(new ResourceContainer(ResourceType.STONE,2));

        ArrayList<ResourceContainer> marketOutput = new ArrayList<>();
        marketOutput.add(new ResourceContainer(ResourceType.GOLD,1));
        marketOutput.add(new ResourceContainer(ResourceType.BLANK,1));
        marketOutput.add(new ResourceContainer(ResourceType.MINION,1));
        marketOutput.add(new ResourceContainer(ResourceType.BLANK,1));


        assertAll(()->conversionSite.canConvert());

        conversionSite.convertSingleElement(marketOutput.get(1), conversionSite.getConversionsAvailable().get(0));
        conversionSite.convertSingleElement(marketOutput.get(3), conversionSite.getConversionsAvailable().get(0));

        assertEquals(marketOutput.get(0).getResourceType(), ResourceType.GOLD);
        assertEquals(marketOutput.get(1).getResourceType(), ResourceType.STONE);
        assertEquals(marketOutput.get(1).getQty(), 2);
        assertEquals(marketOutput.get(2).getResourceType(), ResourceType.MINION);
        assertEquals(marketOutput.get(3).getResourceType(), ResourceType.STONE);
        assertEquals(marketOutput.get(3).getQty(), 2);
    }

    @Test
    void canConvertTest () throws MultipleConversionsActive {
        ConversionSite conversionSite = new ConversionSite();

        //case when conversionSite is still empty
        assertFalse(conversionSite.canConvert());
        assertDoesNotThrow(()->conversionSite.canConvert());

        conversionSite.addConversion(new ResourceContainer(ResourceType.STONE,2));
        conversionSite.addConversion(new ResourceContainer(ResourceType.GOLD,1));


        assertThrows(MultipleConversionsActive.class, ()->conversionSite.canConvert());
    }

    @Test
    void convertTest() throws MultipleConversionsActive {
        ConversionSite conversionSite = new ConversionSite();
        conversionSite.addConversion(new ResourceContainer(ResourceType.MINION,3));

        assertTrue(conversionSite.canConvert());

        ArrayList<ResourceContainer> marketOutput = new ArrayList<>();
        marketOutput.add(new ResourceContainer(ResourceType.BLANK,1));
        marketOutput.add(new ResourceContainer(ResourceType.STONE,1));
        marketOutput.add(new ResourceContainer(ResourceType.BLANK,1));
        marketOutput.add(new ResourceContainer(ResourceType.GOLD,1));

        assertTrue(conversionSite.convert(marketOutput));

        assertEquals(marketOutput.get(0).getResourceType(), ResourceType.MINION);
        assertEquals(marketOutput.get(0).getQty(), 3);
        assertEquals(marketOutput.get(1).getResourceType(), ResourceType.STONE);
        assertEquals(marketOutput.get(2).getResourceType(), ResourceType.MINION);
        assertEquals(marketOutput.get(2).getQty(), 3);
        assertEquals(marketOutput.get(3).getResourceType(), ResourceType.GOLD);


        //it shouldn't change again
        assertTrue(conversionSite.convert(marketOutput));
        assertEquals(marketOutput, marketOutput);
    }
}