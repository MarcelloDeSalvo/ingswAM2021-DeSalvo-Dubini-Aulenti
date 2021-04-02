package Model.Cards;

import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public class DevelopmentCard extends Card{
    private int level;
    private Colour colour;
    private ArrayList<ResourceContainer> input;
    private ArrayList<ResourceContainer> output;

    public DevelopmentCard (int victorypoints, ArrayList<Requirement> req, int level, Colour colour, ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output) {
        super(victorypoints, req, Status.PURCHASABLE);
        this.level = level;
        this.colour = colour;
        this.input = new ArrayList<ResourceContainer>(input);
        this.output = new ArrayList<ResourceContainer>(output);
    }

    public DevelopmentCard (int victorypoints, int level, Colour colour) {
        super(victorypoints, Status.PURCHASABLE);
        this.level = level;
        this.colour = colour;
        this.input = new ArrayList<ResourceContainer>();
        this.output = new ArrayList<ResourceContainer>();
    }

    public void addInput (ResourceContainer resourceContainer) {
        this.input.add(resourceContainer);
    }

    public void addOutput (ResourceContainer resourceContainer) {
        this.output.add(resourceContainer);
    }

    @Override
    public boolean changeStatus (Status status) {
        setStatus(status);
        return true;
    }

    public int getLevel() {
        return level;
    }

    public Colour getColour() {
        return colour;
    }

    public ArrayList<ResourceContainer> getInput() {
        return input;
    }

    public ArrayList<ResourceContainer> getOutput() {
        return output;
    }
}
