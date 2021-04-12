package Model.Cards;

import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public class  DevelopmentCard extends Card{
    private final int level;
    private final Colour colour;
    private final ArrayList<ResourceContainer> input;
    private final ArrayList<ResourceContainer> output;

    //JSON constructors-------------------------------------------------------------------------------------------------
    public DevelopmentCard (int victoryPoints, ArrayList<Requirement> req, int level, Colour colour, ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output, ArrayList<ResourceContainer> price) {
        super(victoryPoints,Status.PURCHASABLE, req, price);
        this.level = level;
        this.colour = colour;
        this.input = new ArrayList<>(input);
        this.output = new ArrayList<>(output);
    }

    public DevelopmentCard (int victoryPoints, int level, Colour colour, ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output, ArrayList<ResourceContainer> price) {
        super(victoryPoints,Status.PURCHASABLE, price);
        this.level = level;
        this.colour = colour;
        this.input = new ArrayList<>(input);
        this.output = new ArrayList<>(output);
    }

    //Test constructors-------------------------------------------------------------------------------------------------
    public DevelopmentCard (int victoryPoints, int level, Colour colour, ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output) {
        super(victoryPoints, Status.PURCHASABLE);
        this.level = level;
        this.colour = colour;
        this.input = new ArrayList<>(input);
        this.output = new ArrayList<>(output);
    }

    public DevelopmentCard (int victoryPoints, int level, Colour colour) {
        super(victoryPoints, Status.PURCHASABLE);
        this.level = level;
        this.colour = colour;
        this.input = new ArrayList<>();
        this.output = new ArrayList<>();
    }



    //CARD MANAGEMENT---------------------------------------------------------------------------------------------------
    @Override
    public boolean changeStatus (Status status) {
        setStatus(status);
        return true;
    }


    /**
     * check if this card has the level and colour required
     * @param l is the level required [Level = 0 means any level]
     * @param c is the colour required
     * @return true if the inputs are equal to the card attributes
     */
    public boolean isSameLevelandColour(int l, Colour c){
        return this.colour == c && (this.level == l || l == 0);
    }
    //------------------------------------------------------------------------------------------------------------------



    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
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
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "DevelopmentCard{" +
                "level=" + level +
                ", colour=" + colour +
                ", input=" + input +
                ", output=" + output +
                "} " + super.toString() + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DevelopmentCard)) return false;
        DevelopmentCard that = (DevelopmentCard) o;
        return level == that.level && colour == that.colour && input.equals(that.input) && output.equals(that.output);
    }
}
