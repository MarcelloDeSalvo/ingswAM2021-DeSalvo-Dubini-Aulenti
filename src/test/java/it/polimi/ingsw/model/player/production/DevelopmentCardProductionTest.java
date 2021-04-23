package it.polimi.ingsw.model.player.production;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.parser.DevelopmentCardParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardProductionTest {

    ArrayList<DevelopmentCard> developmentCards;
    DevelopmentCardProduction developmentCardProduction;

    @BeforeEach
    void setUp() {
        assertAll(() -> developmentCards = DevelopmentCardParser.deserializeDevelopmentList());
        developmentCardProduction = new DevelopmentCardProduction();
    }

    @Test
    void insertOnTop() {
        //insertOnTop of a lvl1
        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(8)));
        assertEquals(developmentCards.get(8), developmentCardProduction.getElementOnTop());

        //insertOnTop of a lvl3
        assertFalse(developmentCardProduction.insertOnTop(developmentCards.get(0)));
        //insertOnTop of a lvl1
        assertFalse(developmentCardProduction.insertOnTop(developmentCards.get(21)));



        //insertOnTop of a lvl2
        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(28)));
        assertEquals(developmentCards.get(28), developmentCardProduction.getElementOnTop());

        //insertOnTop of a lvl2
        assertFalse(developmentCardProduction.insertOnTop(developmentCards.get(18)));
        //insertOnTop of a lvl1
        assertFalse(developmentCardProduction.insertOnTop(developmentCards.get(10)));



        //insertOnTop of a lvl3
        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(37)));
        assertEquals(developmentCards.get(37), developmentCardProduction.getElementOnTop());

        //insertOnTop of a lvl3
        assertFalse(developmentCardProduction.insertOnTop(developmentCards.get(26)));
        //insertOnTop of a lvl2
        assertFalse(developmentCardProduction.insertOnTop(developmentCards.get(30)));
        //insertOnTop of a lvl1
        assertFalse(developmentCardProduction.insertOnTop(developmentCards.get(45)));
    }

    @Test
    void insertOnTop2() {
        //insertOnTop of a lvl3 in an empty Stack
        assertFalse(developmentCardProduction.insertOnTop(developmentCards.get(0)));

        //insertOnTop of a lvl2 in an empty Stack
        assertFalse(developmentCardProduction.insertOnTop(developmentCards.get(31)));

        //getElementOnTop of an empty Stack must be null
        assertNull(developmentCardProduction.getElementOnTop());
    }

    @Test
    void getProductionInput() {
        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(9)));
        assertEquals(developmentCardProduction.getProductionInput(), developmentCards.get(9).getInput());


        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(28)));
        assertEquals(developmentCardProduction.getProductionInput(), developmentCards.get(28).getInput());


        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(37)));
        assertEquals(developmentCardProduction.getProductionInput(), developmentCards.get(37).getInput());
    }

    @Test
    void getProductionOutput() {
        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(9)));
        assertEquals(developmentCardProduction.getProductionOutput(), developmentCards.get(9).getOutput());


        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(29)));
        assertEquals(developmentCardProduction.getProductionOutput(), developmentCards.get(29).getOutput());


        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(37)));
        assertEquals(developmentCardProduction.getProductionOutput(), developmentCards.get(37).getOutput());
    }

    @Test
    void getProductionEmptyInputAndOutput() {
        //production Input and Output of an empty Stuck must return null
        assertNull(developmentCardProduction.getProductionInput());
        assertNull(developmentCardProduction.getProductionOutput());
    }


    @Test
    void countCardsWith() {
        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(8)));
        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(4)));

        assertEquals(developmentCardProduction.countCardsWith(1, Colour.GREEN), 1);
        assertEquals(developmentCardProduction.countCardsWith(0, Colour.BLUE), 0);

        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(0)));

        assertEquals(developmentCardProduction.countCardsWith(0, Colour.GREEN), 3);

    }

    @Test
    void countCardsWith2() {
        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(8)));
        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(31)));

        assertEquals(developmentCardProduction.countCardsWith(1, Colour.GREEN), 1);
        assertEquals(developmentCardProduction.countCardsWith(2, Colour.YELLOW), 1);

        assertTrue(developmentCardProduction.insertOnTop(developmentCards.get(3)));

        assertEquals(developmentCardProduction.countCardsWith(3, Colour.GREEN), 1);
        assertEquals(developmentCardProduction.countCardsWith(0, Colour.GREEN), 2);

    }

    /*@Test
    void fillQuestionMarkInput() {
    }

    @Test
    void fillQuestionMarkOutput() {
    }

    @Test
    void clearCurrentBuffer() {
    }

    @Test
    void hasQuestionMarks() {
    }*/
}