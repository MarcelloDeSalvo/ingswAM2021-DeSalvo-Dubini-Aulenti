package Model.Cards;

import java.util.ArrayList;

abstract class Card {
    private int victorypoints;
    private Status status;
    private ArrayList<Requirement> requirements;

    public Card(int vpoints, ArrayList<Requirement> req, Status status){
        this.victorypoints = vpoints;
        this.status = status;
        this.requirements = new ArrayList<Requirement>(req);
    }

    public abstract boolean changeStatus(Status status);

    public int getVictorypoints() {
        return victorypoints;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Requirement> getRequirements() {
        return requirements;
    }
}
