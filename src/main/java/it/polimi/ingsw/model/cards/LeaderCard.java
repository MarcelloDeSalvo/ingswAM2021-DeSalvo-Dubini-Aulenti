package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.view.cli.Color;

import java.util.ArrayList;

public class LeaderCard extends Card{
    private final ArrayList<Ability> abilities;

    /**
     * Main constructor
     */
    public LeaderCard(int vPoints, ArrayList<Requirement> req, ArrayList<Ability> abilities) {
        super(vPoints, req, Status.HAND);
        this.abilities = abilities;
    }

    /**
     * constructor used for a few tests
     */
    public LeaderCard (int victoryPoints) {
        super(victoryPoints, Status.HAND);
        this.abilities = new ArrayList<>();
    }

    /**
     * constructor used for a few tests
     */
    public LeaderCard (int victoryPoints, int id) {
        super(victoryPoints, Status.HAND, id);
        this.abilities = new ArrayList<>();
    }

    /**
     * constructor with extensibility for "price"
     */
    public LeaderCard(int vPoints, ArrayList<Requirement> req, ArrayList<ResourceContainer> price, ArrayList<Ability> abilities) {
        super(vPoints, Status.HAND, req, price);
        this.abilities = abilities;
    }



    //CARD MANAGEMENT---------------------------------------------------------------------------------------------------
    /**
     * Executes all the Leader's ability, scrolling through them one by one
     * @param playerBoard is the current player's playerBoard
     */
    public boolean executeAbility (PlayerBoard playerBoard) {
        for (Ability ability : abilities) {
            ability.useAbility(playerBoard);
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
    public boolean changeStatus (Status status) {
        if (this.getStatus().equals(Status.HAND)) {
            this.setStatus(status);
            return true;
        }

        return false;
    }

    /**
     * adds an ability to the list
     */
    public void addAbility (Ability ability) throws NullPointerException, IllegalArgumentException {
        if (ability != null) {
            abilities.add(ability);
        }
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public ArrayList<Ability> getAbilities() {
        return abilities;
    }
    //------------------------------------------------------------------------------------------------------------------


    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(Color.ANSI_BLUE.escape()).append("LeaderCard ").append(Color.ANSI_RESET.escape());
        stringBuilder.append(super.toString());
        stringBuilder.append(Color.ANSI_CYAN.escape()).append("\nAbilities: : ").append(Color.ANSI_RESET.escape());

        for(Ability ability : abilities)
            stringBuilder.append(ability.toString());

        return stringBuilder.toString();
    }
    //------------------------------------------------------------------------------------------------------------------

}
