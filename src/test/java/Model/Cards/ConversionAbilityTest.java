package Model.Cards;

import Model.Exceptions.InvalidColumnNumber;
import Model.Exceptions.MultipleConversionsActive;
import Model.Market;
import Model.Parser.LeaderCardParser;
import Model.Parser.MarketSetUpParser;
import Model.Player.Player;
import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

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

        assertTrue(leaderCards.get(2).executeAbility(p_playerBoard));
        assertEquals(p_playerBoard.getConvertionSite().getConversionsAvailable().size(), 1);
        assertEquals(p_playerBoard.getConvertionSite().getConversionsAvailable().get(0).getQty(), 1);
        assertEquals(p_playerBoard.getConvertionSite().getConversionsAvailable().get(0).getResourceType(), ResourceType.MINION);
    }

    @Test
    void conversionAbility_2(){

        assertTrue(leaderCards.get(2).executeAbility(p_playerBoard));

        LeaderCard conversion_1 = new LeaderCard(8);
        ConversionAbility conversionAbility = new ConversionAbility(ResourceType.GOLD);
        assertTrue(conversion_1.addAbility(conversionAbility));
        assertAll(()->conversion_1.executeAbility(p_playerBoard));

        assertEquals(p_playerBoard.getConvertionSite().getConversionsAvailable().size(), 2);
        assertEquals(p_playerBoard.getConvertionSite().getConversionsAvailable().get(1).getResourceType(), ResourceType.GOLD);
    }




}