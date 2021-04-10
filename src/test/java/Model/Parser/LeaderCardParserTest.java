package Model.Parser;

import Model.Cards.DevelopmentCard;
import Model.Cards.LeaderCard;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LeaderCardParserTest {

    @Test
    void deserializeLeaderList() throws FileNotFoundException {
        ArrayList<LeaderCard> cards;
        cards = LeaderCardParser.deserializeLeaderList();

        assertEquals(cards.get(0).getVictoryPoints(), 2);
        System.out.println(cards.toString());
    }
}