package Model.Cards;

import Model.Exceptions.InvalidColumnNumber;
import Model.Exceptions.MultipleConversionsActive;
import Model.Market;
import Model.Parser.MarketSetUpParser;
import Model.Player.Player;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ConversionAbilityTest {
    LeaderCard conversionLeader_1 = new LeaderCard(7);
    Ability conversion_1 = new ConversionAbility(ResourceType.GOLD);

    LeaderCard conversionLeader_2 = new LeaderCard(8);
    Ability conversion_2 = new ConversionAbility(ResourceType.SHIELD);

    LeaderCard conversionLeader_3 = new LeaderCard(9);
    Ability conversion_3 = new ConversionAbility(ResourceType.MINION);

    Player p = new Player("Jotaro");
    Market market;
    ArrayList<ResourceContainer> marblesMarket;

    @BeforeEach
    void marketSetUp(){
        assertAll(()->marblesMarket = MarketSetUpParser.deserializeMarketElements());
        market = new Market(marblesMarket);

        conversionLeader_1.addAbility(conversion_1);
        conversionLeader_2.addAbility(conversion_2);
        conversionLeader_3.addAbility(conversion_3);
    }

}