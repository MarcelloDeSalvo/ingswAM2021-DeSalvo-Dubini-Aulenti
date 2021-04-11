package Model.Parser;

import Model.Cards.Colour;
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
        assertEquals(cards.get(0).getColour(), Colour.GREEN);
        System.out.println(cards.toString());
    }
}