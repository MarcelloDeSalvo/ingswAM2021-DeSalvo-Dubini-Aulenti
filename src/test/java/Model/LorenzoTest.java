package Model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LorenzoTest {

    static Game game;
    static Lorenzo lorenzo;
    static ArrayList<ActionToken> actionTokens;

    @BeforeAll
    static void setUp() throws FileNotFoundException {
        game = new Game("Lupo Lucio", true);
        lorenzo = game.getLorenzo();
        actionTokens = lorenzo.getActionTokens();
    }

    @Test
    void shuffleActionTokens() {
        lorenzo.shuffleActionTokens();
        //System.out.println(lorenzo.getActionTokens().toString()); used for checking manually
    }

    @Test
    void pickAction() {
        lorenzo.pickAction(game);
    }

    @Test
    void notifyEndGame() {
    }

    @Test
    void addObserver() {
    }

    @Test
    void removeObserver() {
    }
}