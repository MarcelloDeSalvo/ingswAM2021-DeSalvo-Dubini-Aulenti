package model.parser;

import model.cards.Colour;
import model.cards.DevelopmentCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardParserTest {

    static ArrayList<DevelopmentCard> cards ;

    @BeforeAll
    static void setUp() throws FileNotFoundException {
        cards = DevelopmentCardParser.deserializeDevelopmentList();
    }

    @Test
    void deserializeDevelopmentList_GREEN() {
        //test a few green cards
        assertEquals(cards.get(0).getColour(), Colour.GREEN);
        assertEquals(cards.get(1).getLevel(), 3);

        assertEquals(cards.get(4).getColour(), Colour.GREEN);
        assertEquals(cards.get(5).getLevel(), 2);

        assertEquals(cards.get(8).getColour(), Colour.GREEN);
        assertEquals(cards.get(9).getLevel(), 1);
    }

    @Test
    void deserializeDevelopmentList_BLUE() {
        //test a few blue cards
        assertEquals(cards.get(12).getColour(), Colour.BLUE);
        assertEquals(cards.get(13).getLevel(), 3);

        assertEquals(cards.get(16).getColour(), Colour.BLUE);
        assertEquals(cards.get(17).getLevel(), 2);

        assertEquals(cards.get(20).getColour(), Colour.BLUE);
        assertEquals(cards.get(21).getLevel(), 1);
    }

    @Test
    void deserializeDevelopmentList_YELLOW() {
        //test a few yellow cards
        assertEquals(cards.get(24).getColour(), Colour.YELLOW);
        assertEquals(cards.get(25).getLevel(), 3);

        assertEquals(cards.get(28).getColour(), Colour.YELLOW);
        assertEquals(cards.get(29).getLevel(), 2);

        assertEquals(cards.get(32).getColour(), Colour.YELLOW);
        assertEquals(cards.get(33).getLevel(), 1);
    }

    @Test
    void deserializeDevelopmentList_PURPLE() {
        //test a few purple cards
        assertEquals(cards.get(36).getColour(), Colour.PURPLE);
        assertEquals(cards.get(37).getLevel(), 3);

        assertEquals(cards.get(40).getColour(), Colour.PURPLE);
        assertEquals(cards.get(41).getLevel(), 2);

        assertEquals(cards.get(44).getColour(), Colour.PURPLE);
        assertEquals(cards.get(45).getLevel(), 1);
    }

    @Test
    void deserializeDevelopmentList() {
        assertThrows(IndexOutOfBoundsException.class, () -> cards.get(48));
    }
}