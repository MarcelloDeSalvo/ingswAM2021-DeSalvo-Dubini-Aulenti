package Model.Cards;

import Model.Market;
import Model.Parser.LeaderCardParser;
import Model.Parser.MarketSetUpParser;
import Model.Player.Player;
import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class ConversionAbilityTest {

    Player p = new Player("Jotaro");
    PlayerBoard p_playerBoard = p.getPlayerBoard();
    Market market;
    ArrayList<ResourceContainer> marblesMarket;
    ArrayList<LeaderCard> leaderCards;

    @BeforeEach
    void marketSetUp() throws FileNotFoundException {

        leaderCards = LeaderCardParser.deserializeLeaderList();
        assertAll(()->marblesMarket = MarketSetUpParser.deserializeMarketElements());
        market = new Market(marblesMarket);
    }

    @Test
    void conversionAbility_1(){

        assertTrue(leaderCards.get(12).executeAbility(p_playerBoard));
        assertEquals(p_playerBoard.getConversionSite().getConversionsAvailable().size(), 1);
        assertEquals(p_playerBoard.getConversionSite().getConversionsAvailable().get(0).getQty(), 1);
        assertEquals(p_playerBoard.getConversionSite().getConversionsAvailable().get(0).getResourceType(), ResourceType.MINION);
    }

    @Test
    void conversionAbility_2(){

        assertTrue(leaderCards.get(12).executeAbility(p_playerBoard));
        assertTrue(leaderCards.get(15).executeAbility(p_playerBoard));

        assertEquals(p_playerBoard.getConversionSite().getConversionsAvailable().size(), 2);
        assertEquals(p_playerBoard.getConversionSite().getConversionsAvailable().get(1).getResourceType(), ResourceType.GOLD);
    }




}