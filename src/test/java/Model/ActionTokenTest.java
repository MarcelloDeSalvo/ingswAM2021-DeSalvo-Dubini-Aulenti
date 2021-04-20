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
        game.startGame();
        lorenzo = game.getLorenzo();
        cardgrid = game.getCardgrid();
        faithPath = game.getFaithPath();
        actionTokens = lorenzo.getActionTokens();
        game.nextTurn();
    }


    //This test is used for checking if "removeCards" actions works fine
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

        //CHECKS IF THE GAME ENDS
        assertTrue(game.isFinalTurn());
        game.nextTurn();
        assertTrue(game.isGameEnded());
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

        actionTokens.get(5).getActions().get(1).doAction(game);
        assertEquals(4, faithPath.getPositions().get(1));

        actionTokens.get(4).getActions().get(1).doAction(game);
        actionTokens.get(4).getActions().get(1).doAction(game);
        actionTokens.get(4).getActions().get(1).doAction(game);
        actionTokens.get(4).getActions().get(1).doAction(game);
        actionTokens.get(4).getActions().get(1).doAction(game);
        actionTokens.get(4).getActions().get(1).doAction(game);
        actionTokens.get(4).getActions().get(1).doAction(game);
        actionTokens.get(4).getActions().get(1).doAction(game);
        actionTokens.get(4).getActions().get(1).doAction(game);
        actionTokens.get(4).getActions().get(1).doAction(game);

        assertEquals(24, faithPath.getPositions().get(1));

        actionTokens.get(4).getActions().get(1).doAction(game); //END OF FAITHPATH

        //CHECKS IF THE GAME ENDS
        assertTrue(game.isFinalTurn());
        game.nextTurn();
        assertTrue(game.isGameEnded());
    }

    @Test
    void incrementFaithPointsTest2() {
        actionTokens.get(6).getActions().get(1).doAction(game);
        assertEquals(1, faithPath.getPositions().get(1));

        ArrayList<ActionToken> shuffledActionTokens = lorenzo.getActionTokens();

        //System.out.println(shuffledActionTokens.toString()); print of the shuffled ArrayList: used for checking manually
    }
}