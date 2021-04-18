package Model;

import Model.Cards.Colour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ActionTokenTest {

    Game game;
    Lorenzo lorenzo;
    Cardgrid cardgrid;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        game = new Game("Bandito", true);
        lorenzo = game.getLorenzo();
        cardgrid = game.getCardgrid();
    }

    @Test
    void actionTest() {
        ArrayList<ActionToken> actionTokens = lorenzo.getActionTokens();

        actionTokens.get(0).getActions().get(0).doAction(game);

        assertEquals(10, cardgrid.getNumOfColor(Colour.GREEN));
    }
}