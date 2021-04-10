package Model.Cards;

import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public class  DevelopmentCard extends Card{
    private int level;
    private Colour colour;
    private ArrayList<ResourceContainer> input;
    private ArrayList<ResourceContainer> output;

    //JSON constructors
    public DevelopmentCard (int victoryPoints, ArrayList<Requirement> req, int level, Colour colour, ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output, ArrayList<ResourceContainer> price) {
        super(victoryPoints,Status.PURCHASABLE, req, price);
        this.level = level;
        this.colour = colour;
        this.input = new ArrayList<>(input);
        this.output = new ArrayList<>(output);
    }

    public DevelopmentCard (int victorypoints, int level, Colour colour, ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output, ArrayList<ResourceContainer> price) {
        super(victorypoints,Status.PURCHASABLE, price);
        this.level = level;
        this.colour = colour;
        this.input = new ArrayList<>(input);
        this.output = new ArrayList<>(output);
    }

    //Test constructors
    public DevelopmentCard (int victorypoints, int level, Colour colour, ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output) {
        super(victorypoints, Status.PURCHASABLE);
        this.level = level;
        this.colour = colour;
        this.input = new ArrayList<>(input);
        this.output = new ArrayList<>(output);
    }

    public DevelopmentCard (int victorypoints, int level, Colour colour) {
        super(victorypoints, Status.PURCHASABLE);
        this.level = level;
        this.colour = colour;
        this.input = new ArrayList<>();
        this.output = new ArrayList<>();
    }



    @Override
    public boolean changeStatus (Status status) {
        setStatus(status);
        return true;
    }

    /**
     * Adds a production input to the list
     * @param resourceContainer
     */
    public boolean addInput (ResourceContainer resourceContainer) throws NullPointerException, IllegalArgumentException{
        if(resourceContainer!= null && input.add(resourceContainer))
            return true;
        return false;

    }

    /**
     * Adds a production output to the list
     * @param resourceContainer
     */
    public boolean addOutput (ResourceContainer resourceContainer)throws NullPointerException, IllegalArgumentException {
        if(resourceContainer!= null && output.add(resourceContainer))
            return true;
        return false;
    }


    /**
     * check if this card has the level and colour required
     * @param l is the level required [Level = 0 means any level]
     * @param c is the colour required
     * @return true if the inputs are equal to the card attributes
     */
    public boolean isSameLevelandColour(int l, Colour c){
        if(this.getColour() == c && (this.getLevel() == l || l==0))
            return true;
        return false;
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

    @Override
    public String toString() {
        return "DevelopmentCard{" +
                "level=" + level +
                ", colour=" + colour +
                ", input=" + input +
                ", output=" + output +
                '}';
    }
}
