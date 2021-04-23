package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ObserverFaithPathTest {

    ObserverFaithPathTest ()throws FileNotFoundException{

    }
    Game g = new Game();
    FaithPath faithPath=g.getFaithPath();

    @Test
    void updateTest(){
        assertEquals(0,faithPath.getPositions().get(0));
        assertEquals(0,faithPath.getPositions().get(1));
        faithPath.update(1);
        assertEquals(1,faithPath.getPositions().get(0));
        assertEquals(0,faithPath.getPositions().get(1));
        faithPath.setCurrentPlayer(2);
        faithPath.update(15);
        assertEquals(15,faithPath.getPositions().get(2));


    }

    @Test
    void updateEveryOneElseTest(){
        assertEquals(0,faithPath.getPositions().get(0));
        assertEquals(0,faithPath.getPositions().get(1));

        faithPath.updateEveryOneElse(2);

        assertEquals(0,faithPath.getPositions().get(0));
        assertEquals(2,faithPath.getPositions().get(1));
        assertEquals(2,faithPath.getPositions().get(2));
        assertEquals(2,faithPath.getPositions().get(3));

        faithPath.setCurrentPlayer(1);
        faithPath.updateEveryOneElse(1);

        assertEquals(1,faithPath.getPositions().get(0));
        assertEquals(2,faithPath.getPositions().get(1));
        assertEquals(3,faithPath.getPositions().get(2));
        assertEquals(3,faithPath.getPositions().get(3));


    }




}