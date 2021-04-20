package model;

import model.cards.Colour;
import model.cards.DevelopmentCard;
import model.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ObservableEndGameTest{

    Game g = new Game();
    Player winner = new Player("Winner winner chicken dinner!");

    ObservableEndGameTest() throws FileNotFoundException {
    }


    @Test
    void buy_6_cards(){
        DevelopmentCard dev_1 = new DevelopmentCard(8, 1, Colour.YELLOW);
        DevelopmentCard dev_2 = new DevelopmentCard(8, 2, Colour.GREEN);
        DevelopmentCard dev_3 = new DevelopmentCard(8, 3, Colour.BLUE);
        DevelopmentCard dev_4 = new DevelopmentCard(8, 1, Colour.YELLOW);
        DevelopmentCard dev_5 = new DevelopmentCard(8, 2, Colour.BLUE);
        DevelopmentCard dev_6 = new DevelopmentCard(8, 3, Colour.PURPLE);

        assertTrue(winner.insertBoughtCardOn(1,dev_1));
        assertTrue(winner.insertBoughtCardOn(1,dev_2));
        assertTrue(winner.insertBoughtCardOn(1,dev_3));
        assertTrue(winner.insertBoughtCardOn(2,dev_4));
        assertTrue(winner.insertBoughtCardOn(2,dev_5));
        assertTrue(winner.insertBoughtCardOn(2,dev_6));
    }


    @Test
    void buy_7th_card(){
        DevelopmentCard dev_7 = new DevelopmentCard(8, 1, Colour.YELLOW);
        assertTrue(winner.insertBoughtCardOn(3,dev_7));
    }

    @Test
    void addObserver() {
        winner.getPlayerBoard().addObserver(g);
        assertEquals(winner.getPlayerBoard().getObserversEndGame().size(), 1);
    }

    @Test
    void removeObserver() {
        addObserver();

        winner.getPlayerBoard().removeObserver(g);
        assertEquals(winner.getPlayerBoard().getObserversEndGame().size(), 0);
    }

    @Test
    void notifyEndGame() {
        addObserver();

        buy_6_cards();
        assertFalse(winner.getPlayerBoard().victoryConditions());

        buy_7th_card();
        assertTrue(winner.getPlayerBoard().victoryConditions());

        assertTrue(g.isFinalTurn());
    }


}