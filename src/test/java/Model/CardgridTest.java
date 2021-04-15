package Model;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Exceptions.InvalidColumnNumber;
import Model.Exceptions.InvalidRowNumber;
import Model.Parser.DevelopmentCardParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardgridTest {

    private static ArrayList<DevelopmentCard> developmentCards;

    @BeforeAll
    static void setUP() throws FileNotFoundException {
        developmentCards = DevelopmentCardParser.deserializeDevelopmentList();
    }

    @Test
    void cardGridConstructorTest() throws FileNotFoundException {
        Cardgrid cardgrid=new Cardgrid(developmentCards);
        cardgrid.printGrid();
    }

    @Test
    void cardGridGetDevelopmentCardOnTopTest() throws FileNotFoundException, InvalidRowNumber, InvalidColumnNumber {
        Cardgrid cardgrid=new Cardgrid(developmentCards);
        cardgrid.printGreenCardsLevel1();
        DevelopmentCard tempCard;
        tempCard=cardgrid.getDevelopmentCardOnTop(Colour.GREEN,3);
        System.out.println("Card's level: "+tempCard.getLevel() + " and VP: "+tempCard.getVictoryPoints());
        assertEquals(cardgrid.getDevelopmentCardOnTop(Colour.GREEN,3), cardgrid.getDevelopmentCardOnTop(1,1));

    }

    @Test
    void removeDevelopmentCardTest()throws FileNotFoundException{
        Cardgrid cardgrid=new Cardgrid(developmentCards);
        Deck greenLevel3s;
        greenLevel3s = cardgrid.getDeckFromGrid(Colour.GREEN,3);
        DevelopmentCard cardNum1=greenLevel3s.getDeck().get(0);
        DevelopmentCard cardNum2=greenLevel3s.getDeck().get(1);
        DevelopmentCard cardNum3=greenLevel3s.getDeck().get(2);
        DevelopmentCard cardNum4=greenLevel3s.getDeck().get(3);

        //cardgrid.printGrid();

        assertEquals(cardgrid.getDevelopmentCardOnTop(Colour.GREEN,3),cardNum1);

        assertTrue(cardgrid.removeDevelopmentCard(Colour.GREEN,3));
        assertEquals(cardgrid.getDevelopmentCardOnTop(Colour.GREEN,3),cardNum2);
        assertTrue(cardgrid.removeDevelopmentCard(Colour.GREEN,3));
        assertEquals(cardgrid.getDevelopmentCardOnTop(Colour.GREEN,3),cardNum3);
        assertTrue(cardgrid.removeDevelopmentCard(Colour.GREEN,3));
        assertEquals(cardgrid.getDevelopmentCardOnTop(Colour.GREEN,3),cardNum4);
        assertTrue(cardgrid.removeDevelopmentCard(Colour.GREEN,3));
        //cardgrid.printGrid();
        assertFalse(cardgrid.removeDevelopmentCard(Colour.GREEN,3));
        assertNull(cardgrid.getDevelopmentCardOnTop(Colour.GREEN, 3));

    }

}