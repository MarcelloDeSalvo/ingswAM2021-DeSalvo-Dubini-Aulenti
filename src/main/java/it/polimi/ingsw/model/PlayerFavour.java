package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * Support class for the Faith Path
 */
public class PlayerFavour {
    private final ArrayList<Integer> Favours;

    public PlayerFavour() {
        Favours = new ArrayList<>();
    }

    public boolean addFavour(int favourAmount) {
        Favours.add(favourAmount);
        return true;
    }

    public boolean isEmpty() {
        return (Favours.isEmpty());
    }

    /**
     * Sums all the integers that represent all the Favours a player has collected over the game.
     */
    public int favourVictoryTotal() {
        int c = 0;
        for (int i : Favours) {
            c = c + i;
        }
        return c;

    }

    public ArrayList<Integer> getFavours() {
        return Favours;
    }

}
