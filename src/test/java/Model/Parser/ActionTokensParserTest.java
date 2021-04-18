package Model.Parser;

import Model.ActionToken;
import Model.Cards.Colour;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ActionTokensParserTest {

    static ArrayList<ActionToken> actionTokens;

    @BeforeAll
    static void setUp() throws FileNotFoundException {
        actionTokens = ActionTokensParser.deserializeActionTokens();
    }

    @Test
    void deserializeActionTokensTest() {
        assertEquals(Colour.GREEN, actionTokens.get(0).getColour());
        assertEquals(2, actionTokens.get(0).getAmountOfCards());
        assertEquals(0, actionTokens.get(0).getFaithPoints());

        assertEquals(Colour.BLUE, actionTokens.get(1).getColour());
        assertEquals(2, actionTokens.get(1).getAmountOfCards());
        assertEquals(0, actionTokens.get(1).getFaithPoints());

        assertEquals(Colour.YELLOW, actionTokens.get(2).getColour());
        assertEquals(2, actionTokens.get(2).getAmountOfCards());
        assertEquals(0, actionTokens.get(2).getFaithPoints());

        assertEquals(Colour.PURPLE, actionTokens.get(3).getColour());
        assertEquals(2, actionTokens.get(3).getAmountOfCards());
        assertEquals(0, actionTokens.get(3).getFaithPoints());

        assertNull(actionTokens.get(4).getColour());
        assertEquals(0, actionTokens.get(4).getAmountOfCards());
        assertEquals(2, actionTokens.get(4).getFaithPoints());

        assertNull(actionTokens.get(5).getColour());
        assertEquals(0, actionTokens.get(5).getAmountOfCards());
        assertEquals(2, actionTokens.get(5).getFaithPoints());

        assertNull(actionTokens.get(6).getColour());
        assertEquals(0, actionTokens.get(6).getAmountOfCards());
        assertEquals(1, actionTokens.get(6).getFaithPoints());
    }
}