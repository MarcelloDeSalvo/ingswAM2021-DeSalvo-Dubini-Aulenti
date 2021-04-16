package Model;

import Model.Parser.FaithPathSetUpParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class FaithPathTest {
    FaithPath faithPath;
    Game myGame;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        myGame=new Game();
        faithPath=myGame.getFaithPath();
        //faithPath.setNumOfPlayers(4);

    }

    @Test
    void faithTestBasicMovement(){
        faithPath.update(1);
        assertEquals(faithPath.getPositions().get(0),1);
        faithPath.setCurrentPlayer(1);
        faithPath.update(3);
        assertEquals(faithPath.getPositions().get(1),3);
        faithPath.setCurrentPlayer(0);
        faithPath.update(1);
        assertEquals(faithPath.getPositions().get(0),2);
        faithPath.setCurrentPlayer(3);
        faithPath.update(5);
        assertEquals(5,faithPath.getPositions().get(3));
    }

    @Test
    void faithTestPapalFavours(){
        faithPath.update(7);
        faithPath.setCurrentPlayer(1);
        faithPath.update(8);
        assertEquals(2,faithPath.getPlayersFavourList().get(1).getFavours().get(0));
        assertEquals(2,faithPath.getPlayersFavourList().get(0).getFavours().get(0));
        assertTrue(faithPath.getPlayersFavourList().get(2).isEmpty());
        assertEquals(3,faithPath.getPapalFavours().get(0));
        faithPath.setCurrentPlayer(2);
        faithPath.update(8);
        assertTrue(faithPath.getPlayersFavourList().get(2).isEmpty());
        faithPath.setCurrentPlayer(0);
        faithPath.update(9);
        assertEquals(2,faithPath.getPlayersFavourList().get(0).getFavours().get(0));
        assertEquals(3,faithPath.getPlayersFavourList().get(0).getFavours().get(1));
        assertEquals(2,faithPath.getPlayersFavourList().get(1).getFavours().get(0));
        assertEquals(1,faithPath.getPlayersFavourList().get(1).getFavours().size());
        assertTrue(faithPath.getPlayersFavourList().get(2).isEmpty());
        faithPath.setCurrentPlayer(1);
        faithPath.update(100);
        System.out.println(faithPath.getPositions().get(1));
        faithPath.update(2);
        assertEquals(4,faithPath.getPlayersFavourList().get(1).getFavours().get(1));
        assertEquals(2,faithPath.getPlayersFavourList().get(1).getFavours().get(0));
        assertEquals(2,faithPath.getPlayersFavourList().get(0).getFavours().get(0));
        assertEquals(3,faithPath.getPlayersFavourList().get(0).getFavours().get(1));
        assertTrue(faithPath.getPlayersFavourList().get(3).isEmpty());
    }


}