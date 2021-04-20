package Model;

import Model.Exceptions.InvalidColumnNumber;
import Model.Exceptions.InvalidRowNumber;
import Model.Parser.MarketSetUpParser;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarketTest {
    ArrayList<ResourceContainer> marblesMarket;
    @BeforeEach
    void marketSetUp(){
        assertAll(()->marblesMarket = MarketSetUpParser.deserializeMarketElements());
    }

    @Test
    void marketConstructor(){
        Market market = new Market(marblesMarket);
        //market.printMarket();
    }

    @Test
    void getRowTest(){
        Market market=new Market(marblesMarket);
        ResourceType num1=market.getMarketCell(0,0).getResourceType();
        ResourceType num2=market.getMarketCell(0,1).getResourceType();
        ResourceType num3=market.getMarketCell(0,2).getResourceType();
        ResourceType num4=market.getMarketCell(0,3).getResourceType();
        ResourceType num5=market.getVacant().getResourceType();
        assertThrows(InvalidRowNumber.class,()->market.getRow(-1));
        assertThrows(InvalidRowNumber.class,()->market.getRow(4));
        ArrayList<ResourceContainer> marketOut=new ArrayList<>();

        assertAll(()->market.getRow(1));
        assertEquals(market.getVacant().getResourceType(), num1);
        assertEquals(market.getMarketCell(0,0).getResourceType(),num2);
        assertEquals(market.getMarketCell(0,1).getResourceType(),num3);
        assertEquals(market.getMarketCell(0,2).getResourceType(),num4);
        assertEquals(market.getMarketCell(0,3).getResourceType(),num5);
        num1=market.getMarketCell(1,0).getResourceType();
        num2=market.getMarketCell(1,1).getResourceType();
        num3=market.getMarketCell(1,2).getResourceType();
        num4=market.getMarketCell(1,3).getResourceType();
        num5=market.getVacant().getResourceType();
        assertAll(()->market.getRow(2));
        assertEquals(market.getVacant().getResourceType(), num1);
        assertEquals(market.getMarketCell(1,0).getResourceType(),num2);
        assertEquals(market.getMarketCell(1,1).getResourceType(),num3);
        assertEquals(market.getMarketCell(1,2).getResourceType(),num4);
        assertEquals(market.getMarketCell(1,3).getResourceType(),num5);

    }

    @Test
    void getColumnTest(){
        Market market=new Market(marblesMarket);
        assertThrows(InvalidColumnNumber.class,()->market.getColumn(-1));
        assertThrows(InvalidColumnNumber.class,()->market.getColumn(5));
        ArrayList<ResourceContainer> marketOut=new ArrayList<>();
        ResourceType num1=market.getMarketCell(0,0).getResourceType();
        ResourceType num2=market.getMarketCell(1,0).getResourceType();
        ResourceType num3=market.getMarketCell(2,0).getResourceType();
        ResourceType num4=market.getVacant().getResourceType();
        assertAll(()->market.getColumn(1));

        assertEquals(market.getVacant().getResourceType(), num1);
        assertEquals(market.getMarketCell(0,0).getResourceType(),num2);
        assertEquals(market.getMarketCell(1,0).getResourceType(),num3);
        assertEquals(market.getMarketCell(2,0).getResourceType(),num4);

        num1=market.getMarketCell(0,1).getResourceType();
        num2=market.getMarketCell(1,1).getResourceType();
        num3=market.getMarketCell(2,1).getResourceType();
        num4=market.getVacant().getResourceType();
        assertAll(()->market.getColumn(2));

        assertEquals(market.getVacant().getResourceType(), num1);
        assertEquals(market.getMarketCell(0,1).getResourceType(),num2);
        assertEquals(market.getMarketCell(1,1).getResourceType(),num3);
        assertEquals(market.getMarketCell(2,1).getResourceType(),num4);

    }


    @Test
    void marketComprehensiveTest(){
        Market market=new Market(marblesMarket);

        assertThrows(InvalidColumnNumber.class,()->market.getColumn(-1));
        assertThrows(InvalidRowNumber.class,()->market.getRow(1337));
        ArrayList<ResourceContainer> marketOut=new ArrayList<>();
        ResourceType num1=market.getMarketCell(0,0).getResourceType();
        ResourceType num2=market.getMarketCell(1,0).getResourceType();
        ResourceType num3=market.getMarketCell(2,0).getResourceType();
        ResourceType num5=market.getVacant().getResourceType();
        assertAll(()->market.getColumn(1));

        assertEquals(market.getVacant().getResourceType(), num1);
        assertEquals(market.getMarketCell(0,0).getResourceType(),num2);
        assertEquals(market.getMarketCell(1,0).getResourceType(),num3);
        assertEquals(market.getMarketCell(2,0).getResourceType(),num5);

        num1=market.getMarketCell(0,0).getResourceType();
        num2=market.getMarketCell(0,1).getResourceType();
        num3=market.getMarketCell(0,2).getResourceType();
        ResourceType num4=market.getMarketCell(0,3).getResourceType();
        num5=market.getVacant().getResourceType();

        assertAll(()->market.getRow(1));

        assertEquals(market.getVacant().getResourceType(), num1);
        assertEquals(market.getMarketCell(0,0).getResourceType(),num2);
        assertEquals(market.getMarketCell(0,1).getResourceType(),num3);
        assertEquals(market.getMarketCell(0,2).getResourceType(),num4);
        assertEquals(market.getMarketCell(0,3).getResourceType(),num5);

        num1=market.getMarketCell(0,3).getResourceType();
        num2=market.getMarketCell(1,3).getResourceType();
        num3=market.getMarketCell(2,3).getResourceType();
        num5=market.getVacant().getResourceType();
        assertAll(()->market.getColumn(4));

        assertEquals(market.getVacant().getResourceType(), num1);
        assertEquals(market.getMarketCell(0,3).getResourceType(),num2);
        assertEquals(market.getMarketCell(1,3).getResourceType(),num3);
        assertEquals(market.getMarketCell(2,3).getResourceType(),num5);

        num1=market.getMarketCell(2,0).getResourceType();
        num2=market.getMarketCell(2,1).getResourceType();
        num3=market.getMarketCell(2,2).getResourceType();
        num4=market.getMarketCell(2,3).getResourceType();
        num5=market.getVacant().getResourceType();

        assertAll(()->market.getRow(3));

        assertEquals(market.getVacant().getResourceType(), num1);
        assertEquals(market.getMarketCell(2,0).getResourceType(),num2);
        assertEquals(market.getMarketCell(2,1).getResourceType(),num3);
        assertEquals(market.getMarketCell(2,2).getResourceType(),num4);
        assertEquals(market.getMarketCell(2,3).getResourceType(),num5);


    }



}

