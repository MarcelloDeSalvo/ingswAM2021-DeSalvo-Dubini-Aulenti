package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class CardgridTest {

    @Test
    void cardGridConstructorTest() throws FileNotFoundException {
        Cardgrid cardgrid=new Cardgrid();
        cardgrid.printGrid();
    }

}