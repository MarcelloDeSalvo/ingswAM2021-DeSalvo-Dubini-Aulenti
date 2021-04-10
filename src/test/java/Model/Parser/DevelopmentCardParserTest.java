package Model.Parser;

import Model.Cards.DevelopmentCard;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DevelopmentCardParserTest {

    @Test
    void deserializeDevelopmentList() throws FileNotFoundException {
        ArrayList<DevelopmentCard> cards ;
        cards = DevelopmentCardParser.deserializeDevelopmentList();
        assertEquals(cards.get(0).getLevel(),1);
        System.out.println(cards.toString());
    }
}