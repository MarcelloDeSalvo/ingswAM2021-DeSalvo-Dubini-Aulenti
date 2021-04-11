package Model.Player.Production;

import Model.Cards.DevelopmentCard;
import Model.Parser.DevelopmentCardParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static Model.Parser.DevelopmentCardParser.*;
import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardProductionTest {

    ArrayList<DevelopmentCard> developmentCards;
    DevelopmentCardProduction developmentCardProduction;

    @BeforeEach
    void setUp() {
        assertAll(() -> developmentCards = deserializeDevelopmentList());
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
    }

    @Test
    void getProductionOutput() {
    }

    @Test
    void countCardsWith() {
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