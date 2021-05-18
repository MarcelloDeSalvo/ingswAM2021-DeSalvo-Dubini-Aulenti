package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.parser.FaithPathSetUpParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LiteFaithPathTest {
    LiteFaithPath liteFaithPath;
    @BeforeEach
    void setup(){
        assertAll(()->liteFaithPath= FaithPathSetUpParser.deserializeLiteFaithPathSetUp());
        ArrayList<String> nicks=new ArrayList<>();
        nicks.add("Pepo");
        nicks.add("Giggino");
        nicks.add("MarseloSauro9000");
        liteFaithPath.setUpPositions(nicks);
    }

    @Test
    void LiteFaithPathTestBasic(){
        liteFaithPath.incrementPosition(2,"MarseloSauro9000");
        liteFaithPath.incrementOthersPositions(15, "Giggino");
        ArrayList<Integer> mockFavours=new ArrayList<>();
        mockFavours.add(2);
        mockFavours.add(0);
        mockFavours.add(2);
        liteFaithPath.incrementPlayerFavours(mockFavours);
        System.out.println(liteFaithPath.toString());

    }



}