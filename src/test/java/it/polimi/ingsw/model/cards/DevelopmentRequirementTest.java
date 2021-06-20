package it.polimi.ingsw.model.cards;


import it.polimi.ingsw.model.Util;
import it.polimi.ingsw.model.parser.DevelopmentCardParser;
import it.polimi.ingsw.model.parser.LeaderCardParser;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerBoard;
import org.junit.jupiter.api.Assertions;
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


        //Yellow level 1
        assertTrue(p_playerBoard.insertBoughtCard(p_playerBoard.getProductionSlotByID(1), Util.getCardWithVpColour(1, Colour.YELLOW)));
        assertEquals(developmentCards.get(35).getColour(), Colour.YELLOW);
        assertEquals(developmentCards.get(35).getLevel(), 1);

        //Blue level 2
        assertTrue(p_playerBoard.insertBoughtCard(p_playerBoard.getProductionSlotByID(1), developmentCards.get(16)));
        assertEquals(developmentCards.get(16).getColour(), Colour.BLUE);
        assertEquals(developmentCards.get(16).getLevel(), 2);

        //Green level 3
        assertTrue(p_playerBoard.insertBoughtCard(p_playerBoard.getProductionSlotByID(1),developmentCards.get(1)));
        assertEquals(developmentCards.get(1).getColour(), Colour.GREEN);
        assertEquals(developmentCards.get(1).getLevel(), 3);
    }


    @Test
    void sameLevel(){
        assertNotNull(Util.getCardWithVpColour(6,Colour.BLUE));
        Assertions.assertTrue(Util.getCardWithVpColour(6,Colour.BLUE).isSameLevelAndColour(2, Colour.BLUE));
    }

    @Test
    void checkSlot(){
        p_playerBoard.getProductionSite().hasEnoughDevelopmentCardsWith(1,1,Colour.YELLOW);
        p_playerBoard.getProductionSite().hasEnoughDevelopmentCardsWith(1,2,Colour.BLUE);
        p_playerBoard.getProductionSite().hasEnoughDevelopmentCardsWith(1,3,Colour.GREEN);
    }


    @Test
    void checkRequirements_AnyLevel_True(){
        //He needs one generic Green and one generic Yellow
        assertTrue(leaderCards.get(0).checkRequirements(p_playerBoard));
    }

    @Test
    void checkRequirements_AnyLevel_False(){
        //He needs one generic Green and one generic Blue
        assertTrue(leaderCards.get(1).checkRequirements(p_playerBoard));
    }

    @Test
    void checkRequirements_SpecificLevel_False(){
        //He needs one level 2 blue
        assertEquals(leaderCards.get(11).getRequirements().get(0).getColour(), Colour.BLUE);
        assertEquals(leaderCards.get(11).getRequirements().get(0).getLevel(), 2);
        assertTrue(leaderCards.get(11).checkRequirements(p_playerBoard));
        //He needs one level 2 purple
        assertFalse(leaderCards.get(10).checkRequirements(p_playerBoard));
    }
}