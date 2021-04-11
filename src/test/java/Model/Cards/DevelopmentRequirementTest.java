package Model.Cards;

import Model.Market;
import Model.Parser.DevelopmentCardParser;
import Model.Parser.LeaderCardParser;
import Model.Parser.MarketSetUpParser;
import Model.Player.Player;
import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DevelopmentRequirementTest {
    Player p = new Player("Super Mario");
    PlayerBoard p_playerBoard = p.getPlayerBoard();
    ArrayList<LeaderCard> leaderCards;
    ArrayList<DevelopmentCard> developmentCards;

    @BeforeEach
    void setUp() throws FileNotFoundException {

        leaderCards = LeaderCardParser.deserializeLeaderList();
        developmentCards = DevelopmentCardParser.deserializeDevelopmentList();

        //Green level 3
        assertTrue(p_playerBoard.insertBoughtCard(p_playerBoard.getProductionSlotByID(1),developmentCards.get(0)));
        assertEquals(developmentCards.get(1).getColour(), Colour.GREEN);
        assertEquals(developmentCards.get(1).getLevel(), 3);

        //Blue level 2
        assertTrue(p_playerBoard.insertBoughtCard(p_playerBoard.getProductionSlotByID(2), developmentCards.get(16)));
        assertEquals(developmentCards.get(16).getColour(), Colour.BLUE);
        assertEquals(developmentCards.get(16).getLevel(), 2);

        //Yellow level 1
        assertTrue(p_playerBoard.insertBoughtCard(p_playerBoard.getProductionSlotByID(3), developmentCards.get(35)));
        assertEquals(developmentCards.get(3).getColour(), Colour.YELLOW);
        assertEquals(developmentCards.get(3).getLevel(), 1);

    }

    @Test
    void checkRequirements_AnyLevel_True(){
        //He needs one generic Green and one generic Yellow
        assertTrue(leaderCards.get(0).checkRequirements(p_playerBoard));
    }

    @Test
    void checkRequirements_AnyLevel_False(){
        //He needs one generic Yellow and one generic Blue
        assertFalse(leaderCards.get(1).checkRequirements(p_playerBoard));
    }

    @Test
    void checkRequirements_SpecificLevel_False(){
        //He needs one level 2 blue
        assertTrue(leaderCards.get(12).checkRequirements(p_playerBoard));
        //He needs one level 2 purple
        assertFalse(leaderCards.get(11).checkRequirements(p_playerBoard));
    }
}