package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Colour;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LorenzoTest {

    static Game game;
    static Lorenzo lorenzo;

    @Test
    void shuffleActionTokens() {
        lorenzo.shuffleActionTokens();
        //System.out.println(lorenzo.getActionTokens().toString()); //used for checking manually
    }

    @Test
    void pickAction_1() throws FileNotFoundException {
        game = new Game("Lupo Lucio", true); //No shuffle mode (Gson inserts the tokens from the file without shuffling them)
        lorenzo = game.getLorenzo();
        game.startGame();

        Assertions.assertEquals(lorenzo.getActionTokens().get(0).getColour(), Colour.GREEN);

        //Player does something
        game.nextTurn(); //Player ends his turn, Lorenzo plays and ends his turn

        assertEquals(game.getCardgrid().getNumOfColor(Colour.GREEN),10); //Lorenzo removed two GREEN CARDS
        Assertions.assertEquals(lorenzo.getActionTokens().get(0).getColour(), Colour.BLUE); //The previous token has been moved to the end of the list
        Assertions.assertEquals(lorenzo.getActionTokens().get(6).getColour(), Colour.GREEN);

        //Player does something
        game.nextTurn();//Player ends his turn,, Lorenzo plays and ends his turn

        assertEquals(game.getCardgrid().getNumOfColor(Colour.BLUE),10); //Lorenzo removed two BLUE CARDS

    }

    @Test
    void pickAction_2() throws FileNotFoundException {
        game = new Game("Lennon", true); //No shuffle mode (Gson inserts the tokens from the file without shuffling them)
        lorenzo = game.getLorenzo();
        game.startGame();

        //Player does something
        game.nextTurn(); //Player ends his turn, Lorenzo plays and ends his turn
        assertEquals(game.getCardgrid().getNumOfColor(Colour.GREEN),10); //Lorenzo removed two GREEN CARDS

        game.nextTurn(); //Player ends his turn, Lorenzo plays and ends his turn
        assertEquals(game.getCardgrid().getNumOfColor(Colour.BLUE),10); //Lorenzo removed two BLUE CARDS

        game.nextTurn(); //Player ends his turn, Lorenzo plays and ends his turn
        assertEquals(game.getCardgrid().getNumOfColor(Colour.YELLOW),10); //Lorenzo removed two YELLOW CARDS

        game.nextTurn(); //Player ends his turn, Lorenzo plays and ends his turn
        assertEquals(game.getCardgrid().getNumOfColor(Colour.PURPLE),10); //Lorenzo removed two PURPLE CARDS

        game.nextTurn(); //Player ends his turn, Lorenzo plays and ends his turn
        assertEquals(0, game.getFaithPath().getPositions(0));
        assertEquals(2, game.getFaithPath().getPositions(1)); //Lorenzo has moved by 2 squares

        game.nextTurn(); //Player ends his turn, Lorenzo plays and ends his turn
        assertEquals(game.getFaithPath().getPositions(0),0);
        assertEquals(game.getFaithPath().getPositions(1),4); //Lorenzo has moved by 2 squares

        game.nextTurn(); //Player ends his turn, Lorenzo plays and ends his turn
        assertEquals(game.getFaithPath().getPositions(0),0);
        assertEquals(game.getFaithPath().getPositions(1),5); //Lorenzo has moved by 2 squares
        //THE LIST HAS BEEN SHUFFLED

        //System.out.println(lorenzo.getActionTokens().toString()); //used for checking manually

    }

    @Test
    void notifyEndGame() throws FileNotFoundException {
        game = new Game("Lennon", true); //No shuffle mode (Gson inserts the tokens from the file without shuffling them)
        lorenzo = game.getLorenzo();
        game.startGame();
        game.getFaithPath().incrementOthersPositions(25); //Lorenzo wins
        game.nextTurn();
        assertTrue(game.isGameEnded());
        game.winnerCalculator();
        assertEquals(game.getWinner().get(0), "LORENZO");
    }

    @Test
    void addObserver() throws FileNotFoundException {
        Lorenzo lore = new Lorenzo();
        Game g = new Game();

        lore.addObserver(g);
        assertEquals(lore.getObserversEndGame().size(),1);
    }

    @Test
    void removeObserver() throws FileNotFoundException {
        Lorenzo lore = new Lorenzo();
        Game g = new Game();

        lore.addObserver(g);
        assertEquals(lore.getObserversEndGame().size(),1);

        lore.removeObserver(g);
        assertEquals(lore.getObserversEndGame().size(),0);
    }
}