package Model.Player;
import Model.Cards.LeaderCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void addToHand() {
        LeaderCard leaderCard = new LeaderCard(2);
        Player p = new Player("Nicola Furia");

        assertTrue(p.addToHand(leaderCard));
        assertEquals(p.getHand().size(),1);
        assertEquals(p.getHand().get(0).getVictoryPoints(),2);
    }

    @Test
    void discardFromHand() {
        LeaderCard leaderCard = new LeaderCard(2);
        Player p = new Player("Nicola Furia");

        assertTrue(p.addToHand(leaderCard));
        assertEquals(p.getHand().size(),1);
        assertEquals(p.getHand().get(0).getVictoryPoints(),2);

        assertTrue(p.discardFromHand(0));
        assertEquals(p.getHand().size(),0);
    }

    @Test
    void activateLeader() {
    }
}