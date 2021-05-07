package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.cards.LeaderCard;
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
        assertEquals(cards.get(4).getVictoryPoints(), 3);
        assertEquals(cards.get(8).getVictoryPoints(), 4);
        assertEquals(cards.get(12).getVictoryPoints(), 5);
    }

    /*
    @Test
    void printTest() throws FileNotFoundException {
        ArrayList<LeaderCard> cards;
        cards = LeaderCardParser.deserializeLeaderList();

        for (LeaderCard card : cards) {
            System.out.println(card.toString() + "\n");
        }
    }*/
}