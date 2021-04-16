package Model;

import Model.Exceptions.InvalidColumnNumber;
import Model.Exceptions.InvalidRowNumber;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    void standard_parameters_check_4_Player(Game game, int numOfPlayers) throws InvalidRowNumber, InvalidColumnNumber {
        assertEquals(numOfPlayers, game.getNumOfPlayers());
        assertEquals(16, game.getLeaderCards().size());
        assertEquals(48, game.getDevelopmentCards().size());
        assertEquals(13, game.getMarbles().size());
        assertEquals(25, game.getFaithPath().getLength());
        assertEquals( numOfPlayers, game.getFaithPath().getNumOfPlayers());
        assertEquals(3, game.getCardgrid().getDevelopmentCardOnTop(1,1).getLevel());
    }


    @Test
    void standard_deck_start() throws FileNotFoundException, InvalidRowNumber, InvalidColumnNumber {
        Game game = new Game();
        standard_parameters_check_4_Player(game,4);

    }

    @Test
    void custom_deck_default_param_start() {
    }

    @Test
    void standard_single_player_start() {
    }

    @Test
    void newPlayerOrder() throws FileNotFoundException {
        Game game = new Game();
        assertEquals(4,game.getPlayerList().size());
        assertEquals(0,game.getCurrentPlayer());
        assertEquals(0,game.getPlayerList().get(0).getOrderID());
        assertEquals(3,game.getPlayerList().get(3).getOrderID());
        //System.out.println(game.getPlayerList().toString());

    }

    @Test
    void testNewPlayerOrder() throws FileNotFoundException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Player 1");
        nicknames.add("Player 2");
        Game game = new Game(nicknames,4,4,2);

        assertEquals(2,game.getPlayerList().size());
        assertEquals(0,game.getCurrentPlayer());
        assertEquals(0,game.getPlayerList().get(0).getOrderID());
        assertEquals(1,game.getPlayerList().get(1).getOrderID());
        assertThrows(IndexOutOfBoundsException.class, ()->game.getPlayerList().get(2));

        assertEquals(4,game.getPlayerList().get(0).getPlayerBoard().getProductionSite().getDefaultNum());
        assertNotNull(game.getPlayerList().get(0).getPlayerBoard().getDepositSlotWithDim(4));
    }

    @Test
    void distributeRandomLeadersToHands() throws FileNotFoundException, InvalidRowNumber, InvalidColumnNumber {
        Game game = new Game();
        standard_parameters_check_4_Player(game,4);
        assertEquals(4, game.getPlayerList().get(0).getHand().size());
        assertEquals(4, game.getPlayerList().get(1).getHand().size());
        assertEquals(4, game.getPlayerList().get(2).getHand().size());
        assertEquals(4, game.getPlayerList().get(3).getHand().size());

        assertNotEquals(game.getPlayer(0).getHand().get(0), game.getPlayer(0).getHand().get(1));
        System.out.println(game.getPlayer(0).getHand().toString());

    }

    @Test
    void setUpObserves() {
    }

    @Test
    void nextTurn() throws FileNotFoundException {
        Game game = new Game();

        assertEquals(0,game.getCurrentPlayer());
        assertEquals(0, game.getTurnNumber());
        assertEquals(0, game.getFaithPath().getCurrentPlayer());

        game.startGame();
        game.nextTurn();

        assertEquals(1, game.getCurrentPlayer());
        assertEquals(1, game.getTurnNumber());
        assertEquals(1, game.getFaithPath().getCurrentPlayer());
    }

    @Test
    void update() {
    }
}