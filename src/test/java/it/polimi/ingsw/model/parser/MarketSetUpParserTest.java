package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarketSetUpParserTest {

    @Test
    void deserializeMarketElements() throws FileNotFoundException {
        ArrayList<ResourceContainer> marbles ;
        marbles = MarketSetUpParser.deserializeMarketElements();

        assertEquals(marbles.get(0).getResourceType(), ResourceType.FAITHPOINT);
        assertEquals(marbles.get(1).getResourceType(), ResourceType.BLANK);
        assertEquals(marbles.get(2).getResourceType(), ResourceType.BLANK);
        assertEquals(marbles.get(3).getResourceType(), ResourceType.BLANK);
        assertEquals(marbles.get(4).getResourceType(), ResourceType.BLANK);
        assertEquals(marbles.get(5).getResourceType(), ResourceType.STONE);
        assertEquals(marbles.get(6).getResourceType(), ResourceType.STONE);
        assertEquals(marbles.get(7).getResourceType(), ResourceType.GOLD);
        assertEquals(marbles.get(8).getResourceType(), ResourceType.GOLD);
        assertEquals(marbles.get(9).getResourceType(), ResourceType.MINION);
        assertEquals(marbles.get(10).getResourceType(), ResourceType.MINION);
        assertEquals(marbles.get(11).getResourceType(), ResourceType.SHIELD);
        assertEquals(marbles.get(12).getResourceType(), ResourceType.SHIELD);

        assertThrows(IndexOutOfBoundsException.class, () -> marbles.get(13));
    }

}