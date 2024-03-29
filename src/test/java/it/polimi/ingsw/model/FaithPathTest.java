package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

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
        faithPath.incrementPosition(1);
        assertEquals(faithPath.getPositions().get(0),1);
        faithPath.setCurrentPlayer(1);
        faithPath.incrementPosition(3);
        assertEquals(faithPath.getPositions().get(1),3);
        faithPath.setCurrentPlayer(0);
        faithPath.incrementPosition(1);
        assertEquals(faithPath.getPositions().get(0),2);
        faithPath.setCurrentPlayer(3);
        faithPath.incrementPosition(5);
        assertEquals(6,faithPath.getPositions().get(3));
    }

    @Test
    void faithTestPapalFavours(){
        faithPath.incrementPosition(7);
        faithPath.setCurrentPlayer(1);
        faithPath.incrementPosition(8);
        assertEquals(2,faithPath.getPlayersFavourList().get(1).getFavours().get(0));
        assertEquals(2,faithPath.getPlayersFavourList().get(0).getFavours().get(0));
        assertTrue(faithPath.getPlayersFavourList().get(2).isEmpty());
        assertEquals(3,faithPath.getPapalFavours().get(0));
        faithPath.setCurrentPlayer(2);
        faithPath.incrementPosition(8);
        assertTrue(faithPath.getPlayersFavourList().get(2).isEmpty());
        faithPath.setCurrentPlayer(0);
        faithPath.incrementPosition(9);
        assertEquals(2,faithPath.getPlayersFavourList().get(0).getFavours().get(0));
        assertEquals(3,faithPath.getPlayersFavourList().get(0).getFavours().get(1));
        assertEquals(2,faithPath.getPlayersFavourList().get(1).getFavours().get(0));
        assertEquals(1,faithPath.getPlayersFavourList().get(1).getFavours().size());
        assertTrue(faithPath.getPlayersFavourList().get(2).isEmpty());
        faithPath.setCurrentPlayer(1);
        faithPath.incrementPosition(100);
        faithPath.incrementPosition(2);
        assertEquals(4,faithPath.getPlayersFavourList().get(1).getFavours().get(1));
        assertEquals(2,faithPath.getPlayersFavourList().get(1).getFavours().get(0));
        assertEquals(2,faithPath.getPlayersFavourList().get(0).getFavours().get(0));
        assertEquals(3,faithPath.getPlayersFavourList().get(0).getFavours().get(1));
        assertEquals(5, faithPath.getPlayersFavourList().get(0).favourVictoryTotal());

        assertTrue(faithPath.getPlayersFavourList().get(3).isEmpty());
    }

    @Test
    void faithTestResourceOverFlow(){

        faithPath.incrementPosition(7);
        faithPath.setCurrentPlayer(1);
        faithPath.incrementOthersPositions(3);


        assertEquals(10,faithPath.getPositions().get(0));
        assertEquals(0,faithPath.getPositions().get(1));
        assertEquals(4,faithPath.getPositions().get(2));

        assertEquals(2,faithPath.getPlayersFavourList().get(0).getFavours().get(0));

        faithPath.setCurrentPlayer(0);
        faithPath.incrementPosition(1);
        faithPath.setCurrentPlayer(1);
        faithPath.incrementPosition(15);
        faithPath.setCurrentPlayer(2);
        faithPath.incrementOthersPositions(1);

        assertEquals(12,faithPath.getPositions().get(0));
        assertEquals(16,faithPath.getPositions().get(1));
        assertEquals(4,faithPath.getPositions().get(2));

        assertEquals(3,faithPath.getPlayersFavourList().get(0).getFavours().get(1));
        assertEquals(3,faithPath.getPlayersFavourList().get(1).getFavours().get(0));

        faithPath.setCurrentPlayer(3);
        faithPath.incrementPosition(15);
        faithPath.setCurrentPlayer(2);
        faithPath.incrementOthersPositions(25);

        assertEquals(24,faithPath.getPositions().get(0));
        assertEquals(24,faithPath.getPositions().get(1));
        assertEquals(4,faithPath.getPositions().get(2));
        assertEquals(24,faithPath.getPositions().get(3));

        assertEquals(2,faithPath.getPlayersFavourList().get(0).getFavours().get(0));
        assertEquals(3,faithPath.getPlayersFavourList().get(0).getFavours().get(1));
        //assertEquals(4,faithPath.getPlayersFavourList().get(0).getFavours().get(2));
        //Player 0 doesn't receive the last papal favour because he's too behind and he doesn't enter the vatican report in time
        assertEquals(3,faithPath.getPlayersFavourList().get(1).getFavours().get(0));
        assertEquals(4,faithPath.getPlayersFavourList().get(1).getFavours().get(1));
        assertEquals(4,faithPath.getPlayersFavourList().get(3).getFavours().get(0));


    }


    @Test
    void faithTestGetAllFavours(){
        faithPath.incrementPosition(24);
        assertEquals(2,faithPath.getPlayersFavourList().get(0).getFavours().get(0));
        assertEquals(3,faithPath.getPlayersFavourList().get(0).getFavours().get(1));
        assertEquals(4,faithPath.getPlayersFavourList().get(0).getFavours().get(2));

    }

    @Test
    void faithTestEveryoneAtTheEnd(){
        faithPath.setCurrentPlayer(1);
        faithPath.incrementPosition(1);
        faithPath.setCurrentPlayer(0);
        faithPath.incrementOthersPositions(25);
        assertEquals(2,faithPath.getPlayersFavourList().get(1).getFavours().get(0));
        assertEquals(2,faithPath.getPlayersFavourList().get(2).getFavours().get(0));
        assertEquals(2,faithPath.getPlayersFavourList().get(3).getFavours().get(0));
        assertEquals(3,faithPath.getPlayersFavourList().get(1).getFavours().get(1));
        assertEquals(3,faithPath.getPlayersFavourList().get(2).getFavours().get(1));
        assertEquals(3,faithPath.getPlayersFavourList().get(3).getFavours().get(1));
        assertEquals(4,faithPath.getPlayersFavourList().get(1).getFavours().get(2));
        assertEquals(4,faithPath.getPlayersFavourList().get(2).getFavours().get(2));
        assertEquals(4,faithPath.getPlayersFavourList().get(3).getFavours().get(2));
        assertEquals(9, faithPath.getPlayersFavourList().get(2).favourVictoryTotal());
        assertEquals(29, faithPath.victoryPointCountFaithPath(2));

    }

    @Test
    void faithTestVictoryPoints(){
        faithPath.setCurrentPlayer(1);
        faithPath.incrementPosition(4);
        assertEquals(1, faithPath.victoryPointCountFaithPath(1));
        faithPath.incrementPosition(4);
        assertEquals(4, faithPath.victoryPointCountFaithPath(1));
        faithPath.setCurrentPlayer(0);
        faithPath.incrementPosition(16);
        assertEquals(12, faithPath.victoryPointCountFaithPath(0));
        faithPath.incrementPosition(4);
        faithPath.setCurrentPlayer(1);
        faithPath.incrementPosition(50);
        assertEquals(26, faithPath.victoryPointCountFaithPath(1));
        assertEquals(19, faithPath.victoryPointCountFaithPath(0));


    }

    /*@Test
    void toStringTest(){
        faithPath.incrementPosition(15);
        ArrayList<String> nicks= new ArrayList<>();
        nicks.add("Pippo");
        nicks.add("Pino");
        nicks.add("Gigi");
        nicks.add("Francesco");
        System.out.println(faithPath.toString(nicks));
    }*/

}