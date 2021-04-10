package Model.Parser;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarketSetUpParserTest {

    @Test
    void deserializeMarketElements() throws FileNotFoundException {
        ArrayList<ResourceContainer> marbles ;
        marbles = MarketSetUpParser.deserializeMarketElements();
        //System.out.println(marbles.toString());
        assertEquals(marbles.get(0).getResourceType(), ResourceType.FAITHPOINT);
    }

}