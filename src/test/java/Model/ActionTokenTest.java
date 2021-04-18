package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ActionTokenTest {

    Game game;
    Lorenzo lorenzo;
    Cardgrid cardgrid;
    FaithPath faithPath;
    ArrayList<ActionToken> actionTokens;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        game = new Game("Bandit", true);
        lorenzo = game.getLorenzo();
        cardgrid = game.getCardgrid();
        faithPath = game.getFaithPath();
        actionTokens = lorenzo.getActionTokens();

        faithPath.setCurrentPlayer(1);
    }


    //this test is used for checking if "removeCards" actions works fine
    //it also checks if the NotifyEndGame works properly
    @Test
    void removeCardsActionTest() {
        //checks that LORENZO's position in FaithPath is NOT incremented
        actionTokens.get(0).getActions().get(1).doAction(game);
        assertEquals(0, faithPath.getPositions().get(1));


        //checks that the DevelopmentCards are removed correctly
        actionTokens.get(0).getActions().get(0).doAction(game);
        assertEquals(10, cardgrid.getNumOfColor(actionTokens.get(0).getColour())); //GREEN


        actionTokens.get(0).getActions().get(0).doAction(game);
        assertEquals(8, cardgrid.getNumOfColor(actionTokens.get(0).getColour())); //GREEN


        actionTokens.get(0).getActions().get(0).doAction(game);
        actionTokens.get(0).getActions().get(0).doAction(game);
        actionTokens.get(0).getActions().get(0).doAction(game);
        actionTokens.get(0).getActions().get(0).doAction(game);

        assertEquals(0, cardgrid.getNumOfColor(actionTokens.get(0).getColour())); //GREEN

        assertTrue(game.isFinalTurn());
    }

    @Test
    void removeCardsActionTest2() {
        actionTokens.get(0).getActions().get(0).doAction(game);
        assertEquals(10, cardgrid.getNumOfColor(actionTokens.get(0).getColour())); //GREEN

        actionTokens.get(1).getActions().get(0).doAction(game);
        assertEquals(10, cardgrid.getNumOfColor(actionTokens.get(1).getColour())); //BLUE

        actionTokens.get(2).getActions().get(0).doAction(game);
        assertEquals(10, cardgrid.getNumOfColor(actionTokens.get(2).getColour())); //YELLOW

        actionTokens.get(3).getActions().get(0).doAction(game);
        assertEquals(10, cardgrid.getNumOfColor(actionTokens.get(3).getColour())); //PURPLE


        //checks that LORENZO's position in FaithPath is NOT incremented
        actionTokens.get(0).getActions().get(1).doAction(game);
        assertEquals(0, faithPath.getPositions().get(1));

        actionTokens.get(1).getActions().get(1).doAction(game);
        assertEquals(0, faithPath.getPositions().get(1));

        actionTokens.get(2).getActions().get(1).doAction(game);
        assertEquals(0, faithPath.getPositions().get(1));

        actionTokens.get(3).getActions().get(1).doAction(game);
        assertEquals(0, faithPath.getPositions().get(1));
    }

    @Test
    void nullColorTest() {
        actionTokens.get(4).getActions().get(0).doAction(game);
        assertEquals(0, cardgrid.getNumOfColor(actionTokens.get(4).getColour()));

        actionTokens.get(5).getActions().get(0).doAction(game);
        assertEquals(0, cardgrid.getNumOfColor(actionTokens.get(5).getColour()));

        actionTokens.get(6).getActions().get(0).doAction(game);
        assertEquals(0, cardgrid.getNumOfColor(actionTokens.get(6).getColour()));
    }

    @Test
    void incrementFaithPointsTest() {
        actionTokens.get(4).getActions().get(1).doAction(game);

        assertEquals(2, faithPath.getPositions().get(1));

    }
}