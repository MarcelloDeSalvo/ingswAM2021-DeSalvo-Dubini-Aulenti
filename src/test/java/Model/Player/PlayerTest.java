package Model.Player;
import Model.Cards.LeaderCard;
import Model.Parser.LeaderCardParser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void addToHand_1() {
        LeaderCard leaderCard = new LeaderCard(2);
        Player p = new Player("Nicola Furia");

        assertTrue(p.addToHand(leaderCard));
        assertEquals(p.getHand().size(),1);
        assertEquals(p.getHand().get(0).getVictoryPoints(),2);
    }

    @Test
    void addToHand_2() {
        LeaderCard leaderCard1 = new LeaderCard(1);
        LeaderCard leaderCard2 = new LeaderCard(2);
        LeaderCard leaderCard3 = new LeaderCard(3);

        Player p = new Player("Nicola Gabbia");

        assertTrue(p.addToHand(leaderCard1));
        assertEquals(p.getHand().size(),1);
        assertEquals(p.getHand().get(0).getVictoryPoints(),1);

        assertTrue(p.addToHand(leaderCard2));
        assertEquals(p.getHand().size(),2);
        assertEquals(p.getHand().get(1).getVictoryPoints(),2);

        assertTrue(p.addToHand(leaderCard3));
        assertEquals(p.getHand().size(),3);
        assertEquals(p.getHand().get(2).getVictoryPoints(), 3);
    }

    @Test
    void discardFromHand_1() {
        LeaderCard leaderCard = new LeaderCard(2);
        Player p = new Player("Guglielmo Cancelli");

        assertTrue(p.addToHand(leaderCard));
        assertEquals(p.getHand().size(),1);
        assertEquals(p.getHand().get(0).getVictoryPoints(),2);

        assertTrue(p.discardFromHand(0));
        assertEquals(p.getHand().size(),0);
    }

    @Test
    void discardFromHand_2() {
        LeaderCard leaderCard1 = new LeaderCard(1);
        LeaderCard leaderCard2 = new LeaderCard(2);
        LeaderCard leaderCard3 = new LeaderCard(3);
        Player p = new Player("Federico Mercurio");

        assertTrue(p.addToHand(leaderCard1));
        assertTrue(p.addToHand(leaderCard2));
        assertTrue(p.addToHand(leaderCard3));
        assertEquals(p.getHand().size(),3);

        assertTrue(p.discardFromHand(0));
        assertTrue(p.discardFromHand(0));
        assertTrue(p.discardFromHand(0));
        assertEquals(p.getHand().size(),0);
    }

    @Test
    void activateLeader() throws FileNotFoundException {
        ArrayList<LeaderCard> list = LeaderCardParser.deserializeLeaderList();
        LeaderCard leaderCard = list.get(0);
    }
}