package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
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


        assertEquals(Enum.valueOf(ConversionMode.class, "AUTOMATIC"), conversionSite.canConvert());

        conversionSite.convertSingleElement(marketOutput.get(1), conversionSite.getConversionsAvailable().get(0));
        conversionSite.convertSingleElement(marketOutput.get(3), conversionSite.getConversionsAvailable().get(0));

        assertEquals(ResourceType.GOLD, marketOutput.get(0).getResourceType());
        assertEquals(ResourceType.STONE, marketOutput.get(1).getResourceType());
        assertEquals(2, marketOutput.get(1).getQty());

        assertEquals(ResourceType.MINION, marketOutput.get(2).getResourceType());
        assertEquals(ResourceType.STONE, marketOutput.get(3).getResourceType());
        assertEquals(2, marketOutput.get(3).getQty());
    }

    @Test
    void canConvertTest (){
        ConversionSite conversionSite = new ConversionSite();

        //case when conversionSite is still empty
        assertEquals(Enum.valueOf(ConversionMode.class, "INACTIVE"),conversionSite.canConvert());

        conversionSite.addConversion(new ResourceContainer(ResourceType.STONE,2));
        conversionSite.addConversion(new ResourceContainer(ResourceType.GOLD,1));


        assertEquals(Enum.valueOf(ConversionMode.class, "CHOICE_REQUIRED"),conversionSite.canConvert());
    }

    @Test
    void convertTest(){
        ConversionSite conversionSite = new ConversionSite();
        conversionSite.addConversion(new ResourceContainer(ResourceType.MINION,3));

        assertEquals(Enum.valueOf(ConversionMode.class, "AUTOMATIC"),conversionSite.canConvert());

        ArrayList<ResourceContainer> marketOutput = new ArrayList<>();
        marketOutput.add(new ResourceContainer(ResourceType.BLANK,1));
        marketOutput.add(new ResourceContainer(ResourceType.STONE,1));
        marketOutput.add(new ResourceContainer(ResourceType.BLANK,1));
        marketOutput.add(new ResourceContainer(ResourceType.GOLD,1));

        assertTrue(conversionSite.convert(marketOutput));

        assertEquals( ResourceType.MINION, marketOutput.get(0).getResourceType());
        assertEquals( 3, marketOutput.get(0).getQty());
        assertEquals( ResourceType.STONE, marketOutput.get(1).getResourceType());
        assertEquals( ResourceType.MINION, marketOutput.get(2).getResourceType());
        assertEquals( 3, marketOutput.get(2).getQty());
        assertEquals( ResourceType.GOLD, marketOutput.get(3).getResourceType());


        ArrayList<ResourceContainer> old_marketOutput = (ArrayList<ResourceContainer>)marketOutput.clone();
        //it shouldn't change again
        assertTrue(conversionSite.convert(marketOutput));
        assertEquals(old_marketOutput, marketOutput);
    }
}