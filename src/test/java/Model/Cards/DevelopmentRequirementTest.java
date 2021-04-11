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

        assertTrue(p_playerBoard.insertBoughtCard(p_playerBoard.getProductionSlotByID(1),developmentCards.get(0)));
        assertTrue(p_playerBoard.insertBoughtCard(p_playerBoard.getProductionSlotByID(2), developmentCards.get(4)));

    }

    @Test
    void checkRequirements(){
        assertTrue(leaderCards.get(0).checkRequirements(p_playerBoard));
    }
}