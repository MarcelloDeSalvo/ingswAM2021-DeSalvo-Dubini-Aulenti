package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ObservableFaithPathTest{

    Game g = new Game();
    Player winner = new Player("Winner winner chicken dinner!");

    ObservableFaithPathTest() throws FileNotFoundException{

    }

    @Test
    void addObserver() {

        assertEquals(g.getFaithPath().getObserversEndGame().size(), 1);
        g.getFaithPath().addObserver(g);
        assertEquals(g.getFaithPath().getObserversEndGame().size(), 2);

    }

    @Test
    void removeObserver(){

        g.getFaithPath().removeObserver(g);
        assertEquals(g.getFaithPath().getObserversEndGame().size(), 0);
    }

    @Test
    void notifyEndGame1() {

        g.getFaithPath().update(23);
        assertFalse(g.isFinalTurn());
        g.getFaithPath().update(2);
        assertTrue(g.isFinalTurn());
    }

    @Test
    void notifyEndGame2() {

        g.getFaithPath().update(23);
        g.getFaithPath().setCurrentPlayer(1);
        g.getFaithPath().update(23);
        assertFalse(g.isFinalTurn());
        g.getFaithPath().setCurrentPlayer(2);
        g.getFaithPath().updateEveryOneElse(2);
    }

}