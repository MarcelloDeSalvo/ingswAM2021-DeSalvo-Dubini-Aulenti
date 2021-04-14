package Model.Parser;

import Model.FaithPath;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class FaithPathSetUpParserTest {

    @Test
    void FaithPathSetUpParserTest1() throws FileNotFoundException {
        assertAll(()->FaithPathSetUpParser.deserializeFaithPathSetUp(3));
        FaithPath test= FaithPathSetUpParser.deserializeFaithPathSetUp(3);
        assertEquals(test.getLength(),25);
        assertEquals(test.getVaticanReports().get(0),'E');
        assertEquals(test.getVictoryPoints().get(0),0);
        assertEquals(test.getPapalFavours().get(0),2);
        assertEquals(test.getNumberOfPlayers(), 3);

    }

}