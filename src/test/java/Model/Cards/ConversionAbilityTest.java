package Model.Cards;

import Model.Exceptions.InvalidColumnNumber;
import Model.Exceptions.MultipleConversionsActive;
import Model.Market;
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
    ArrayList<ResourceContainer> marblesMarket= new  ArrayList<>();
    Market market;


    @BeforeEach
    void constructRandomMarbles(){
        for(int i=0; i<13; i++) {
            Random random=new Random();
            int randomNum= random.nextInt(6);
            switch (randomNum){
                case 0:
                    marblesMarket.add(new ResourceContainer(ResourceType.GOLD,1));
                case 1:
                    marblesMarket.add(new ResourceContainer(ResourceType.MINION,1));
                case 2:
                    marblesMarket.add(new ResourceContainer(ResourceType.STONE  ,1));
                case 3:
                    marblesMarket.add(new ResourceContainer(ResourceType.SHIELD,1));
                case 4:
                    marblesMarket.add(new ResourceContainer(ResourceType.FAITHPOINT,1));
                case 5:
                    marblesMarket.add(new ResourceContainer(ResourceType.BLANK,1));
              /*  default:
                    System.out.println("Errore!");*/
            }
        }

        market = new Market(marblesMarket);

        conversionLeader_1.addAbility(conversion_1);
        conversionLeader_2.addAbility(conversion_2);
        conversionLeader_3.addAbility(conversion_3);
    }

}