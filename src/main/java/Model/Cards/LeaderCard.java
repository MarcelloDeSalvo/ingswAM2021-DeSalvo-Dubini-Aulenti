package Model.Cards;

import Model.Player.Player;
import Model.Player.PlayerBoard;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;
import java.util.Iterator;

public class LeaderCard extends Card{
    private ArrayList<Ability> abilities;

    public LeaderCard (int vpoints, ArrayList<Requirement> req, ArrayList<Ability> abilities) {
        super(vpoints, req, Status.HAND);
        this.abilities = new ArrayList<Ability>(abilities);
    }

    @Override
    public boolean changeStatus (Status status) {
        if (this.getStatus().equals(Status.HAND)) {
            this.setStatus(status);
            return true;
        }

        return false;
    }

    public boolean executeAbility (PlayerBoard playerBoard) {
        Iterator<Ability> iter = abilities.iterator();

        while(iter.hasNext()) {
            iter.next().useAbility(playerBoard);
        }
        return true;
    }

}
