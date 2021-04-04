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

    @Test
    void useAbility_ControllerSimulation_OneAvailable() throws IOException {
        conversionLeader_1.executeAbility(p.getPlayerBoard());
        ArrayList<ResourceContainer> marketOut = new ArrayList<>();

        try {
            marketOut = market.getColumn(1);
            System.out.println(marketOut.toString());
        }catch (InvalidColumnNumber e){
            System.out.println(e.getMessage());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String name;

        try {
            if(p.getPlayerBoard().getConvertionSite().canConvert()) {
                p.getPlayerBoard().getConvertionSite().convert(marketOut);
                System.out.println(marketOut.toString());

            }
        }catch (MultipleConversionsActive m){
            for (ResourceContainer rc: marketOut) {
                if(p.getPlayerBoard().getConvertionSite().getDefaultConverted() == rc.getResourceType()){
                    // Reading data using readLine
                    name = reader.readLine();

                    // Printing the read line
                    System.out.println(name);

                    if(p.getPlayerBoard().getConvertionSite().getConversionsAvailable().contains(ResourceType.valueOf(name)))
                        p.getPlayerBoard().getConvertionSite().convertSingleBlank(rc,new ResourceContainer(ResourceType.valueOf(name),1));
                        System.out.println("Non esiste");
                }
            }
        }

    }

    @Test
    void useAbility_ControllerSimulation_TwoAvailable(){
        conversionLeader_1.executeAbility(p.getPlayerBoard());
        conversionLeader_2.executeAbility(p.getPlayerBoard());
        ArrayList<ResourceContainer> marketOut = new ArrayList<>();

        try {
            marketOut = market.getColumn(1);
            System.out.println("ORIGINAL: " + marketOut.toString());
        }catch (InvalidColumnNumber e){
            System.out.println(e.getMessage());
        }

        String name = new String();
        ResourceContainer selected;

        try {
            if(p.getPlayerBoard().getConvertionSite().canConvert()) {
                p.getPlayerBoard().getConvertionSite().convert(marketOut);
                System.out.println(marketOut.toString());

            }
        }catch (MultipleConversionsActive m){
            int i = 0;
            for (ResourceContainer rc: marketOut) {
                if(p.getPlayerBoard().getConvertionSite().getDefaultConverted() == rc.getResourceType()){

                    do{
                        // Reading data using readLine
                        switch (i){
                            case 0: name = "GoLD";
                                break;
                            case 1: name ="MINION";
                                break;
                            case 2: name ="SHIELD";
                                break;
                        }

                        // Printing the read line
                        System.out.println(name);
                        name = name.toUpperCase();
                        selected = new ResourceContainer(ResourceType.valueOf(name), 1);
                        i++;
                    }while (!p.getPlayerBoard().getConvertionSite().getConversionsAvailable().contains(selected));

                    p.getPlayerBoard().getConvertionSite().convertSingleBlank(rc, new ResourceContainer(ResourceType.valueOf(name), 1));

                }

                System.out.println(marketOut.toString());
            }
        }

    }
}