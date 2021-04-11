package Model.Cards;

import Model.Player.Player;
import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LeaderCard extends Card{
    private ArrayList<Ability> abilities;

    //main constructor
    public LeaderCard(int vpoints, ArrayList<Requirement> req, ArrayList<Ability> abilities) {
        super(vpoints, req, Status.HAND);
        this.abilities = abilities;
    }

    //constructor used for a few tests
    public LeaderCard (int victoryPoints) {
        super(victoryPoints, Status.HAND);
        this.abilities = new ArrayList<>();
    }

    //constructor with extensibility for "price"
    public LeaderCard(int vpoints, ArrayList<Requirement> req, ArrayList<ResourceContainer> price, ArrayList<Ability> abilities) {
        super(vpoints, Status.HAND, req, price);
        this.abilities = abilities;
    }


    /**
     * adds an ability to the list
     * @param ability
     * @return false if it can't be added
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public boolean addAbility (Ability ability) throws NullPointerException, IllegalArgumentException {
        if(ability != null && abilities.add(ability))
            return true;
        return false;
    }

    @Override
    public boolean changeStatus (Status status) {
        if (this.getStatus().equals(Status.HAND)) {
            this.setStatus(status);
            return true;
        }

        return false;
    }

    /**
     * Executes all the Leader's ability, scrolling through them one by one
     * @param playerBoard is the current player's playerBoard
     * @return true
     */
    public boolean executeAbility (PlayerBoard playerBoard) {
        Iterator<Ability> iter = abilities.iterator();

        while(iter.hasNext()) {
            iter.next().useAbility(playerBoard);
        }
        return true;
    }

    /**
     * Checks automatically if all card's requirements are satisfied
     * @param playerBoard is the current player's playerBoard
     * @return true if all requirements are satisfied, false if there's at least one not satisfied
     */
    public boolean checkRequirements(PlayerBoard playerBoard){
        for (Requirement req: super.getRequirements()) {
            if(!req.checkRequirements(playerBoard))
                return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "LeaderCard{" +
                "abilities=" + abilities +
                "} " + super.toString();
    }
}
