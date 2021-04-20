package Model;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Exceptions.InvalidColumnNumber;
import Model.Exceptions.InvalidRowNumber;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
        //System.out.println(game.getPlayer(0).getHand().toString());

    }

    @Test
    void setUpObserves() {
    }


    void nextTurn_check(Game game, int turnNumber){
        assertEquals(turnNumber%game.getNumOfPlayers(), game.getCurrentPlayer());
        assertEquals(turnNumber, game.getTurnNumber());
        assertEquals(turnNumber%game.getNumOfPlayers(), game.getFaithPath().getCurrentPlayer());
    }

    @Test
    void nextTurn_1() throws FileNotFoundException {
        Game game = new Game();

        assertEquals(0, game.getCurrentPlayer());
        assertEquals(0, game.getTurnNumber());
        assertEquals(0, game.getFaithPath().getCurrentPlayer());

        game.startGame();
        game.nextTurn();
        nextTurn_check(game, 1);
    }

    @Test
    void nextTurn_2() throws FileNotFoundException {
        Game game = new Game();
        game.startGame();

        game.nextTurn();
        nextTurn_check(game, 1);

        game.nextTurn();
        nextTurn_check(game, 2);

        game.nextTurn();
        nextTurn_check(game, 3);
    }


    @Test
    void nextTurn_finalTurnMiddleOfTheRound() throws FileNotFoundException {
        Game game = new Game();
        game.startGame();

        nextTurn_check(game, 0);  //ink well
        game.nextTurn();

        nextTurn_check(game, 1);  //first Player
        game.update();                      //he activates some win conditions
        assertTrue(game.isFinalTurn());
        game.nextTurn();                    //ends his turn, but since he's the one sitting to the left of the first player, the game will end in two rounds

        nextTurn_check(game, 2);  //second Player
        game.nextTurn();

        nextTurn_check(game, 3);  //third Player
        game.nextTurn();

        assertTrue(game.isGameEnded());
        nextTurn_check(game, 3);

    }


    @Test
    void nextTurn_finalTurnThatEndsTheGame() throws FileNotFoundException {
        Game game = new Game();
        game.startGame();

        nextTurn_check(game, 0);  //ink well
        game.nextTurn();

        nextTurn_check(game, 1);  //first Player
        game.nextTurn();

        nextTurn_check(game, 2);  //second Player
        game.nextTurn();

        nextTurn_check(game, 3);  //third Player
        game.update(); //FinalTurn          //he activates some win conditions
        game.nextTurn();                    //ends his turn, but since he's the one sitting to the right of the first player, the game ends

        assertTrue(game.isFinalTurn());
        assertTrue(game.isGameEnded());
        nextTurn_check(game, 3);

    }


    @Test
    void player_3_wins() throws FileNotFoundException {
        Game game = new Game();
        game.startGame();

        DevelopmentCard dev1 = new DevelopmentCard(2,1, Colour.YELLOW);
        game.getPlayer(0).insertBoughtCardOn(1,dev1);
        game.nextTurn();

        DevelopmentCard dev2 = new DevelopmentCard(3,1, Colour.YELLOW);
        game.getPlayer(1).insertBoughtCardOn(1, dev2);
        game.nextTurn();

        game.getPlayer(2).getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.STONE, 20));

        game.winnerCalculator();

        assertEquals(game.getPlayer(2).getNickname(), game.getWinner().get(0));
    }

    @Test
    void player_1_wins() throws FileNotFoundException {
        Game game = new Game();
        game.startGame();

        DevelopmentCard dev1 = new DevelopmentCard(8,1, Colour.YELLOW);
        game.getPlayer(0).insertBoughtCardOn(1,dev1);
        game.nextTurn();

        DevelopmentCard dev2 = new DevelopmentCard(3,1, Colour.YELLOW);
        game.getPlayer(1).insertBoughtCardOn(1, dev2);
        game.nextTurn();

        game.getPlayer(2).getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.STONE, 20));

        game.winnerCalculator();
        assertEquals(game.getPlayer(0).getNickname(), game.getWinner().get(0));
    }

    @Test
    void draw() throws FileNotFoundException {
        Game game = new Game();
        game.startGame();

        DevelopmentCard dev1 = new DevelopmentCard(8,1, Colour.YELLOW);
        game.getPlayer(0).insertBoughtCardOn(1,dev1);
        game.nextTurn();

        DevelopmentCard dev2 = new DevelopmentCard(8,1, Colour.YELLOW);
        game.getPlayer(1).insertBoughtCardOn(1, dev2);
        game.nextTurn();

        game.getPlayer(2).getPlayerBoard().getVault().addToVault(new ResourceContainer(ResourceType.STONE, 20));

        game.winnerCalculator();
        assertEquals(2, game.getWinner().size());
        assertEquals(game.getPlayer(0).getNickname(), game.getWinner().get(0));
        assertEquals(game.getPlayer(1).getNickname(), game.getWinner().get(1));
    }

}