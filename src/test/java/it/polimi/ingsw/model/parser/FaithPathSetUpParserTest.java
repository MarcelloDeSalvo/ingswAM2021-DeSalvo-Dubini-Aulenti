package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.FaithPath;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class FaithPathSetUpParserTest {



    @Test
    void FaithPathSetUpParserTest1() throws FileNotFoundException {
        assertAll(()->FaithPathSetUpParser.deserializeFaithPathSetUp());
        FaithPath test= FaithPathSetUpParser.deserializeFaithPathSetUp();
        assertEquals(test.getLength(),25);
        assertEquals(test.getVaticanReports().get(0),'E');
        assertEquals(test.getVictoryPoints().get(0),0);
        assertEquals(test.getPapalFavours().get(0),2);
    }

}