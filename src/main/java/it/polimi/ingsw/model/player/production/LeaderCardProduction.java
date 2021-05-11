package it.polimi.ingsw.model.player.production;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;


public class LeaderCardProduction implements ProductionSlot {

    /**
     * Contains all the defined inputs
     */
    private final ArrayList<ResourceContainer> input;

    /**
     * Contains the number of the definable inputs
     */
    private final int questionMarkOnInput;

    /**
     * Contains all the defined outputs
     */
    private final ArrayList<ResourceContainer> output;

    /**
     * Contains the number of the definable outputs
     */
    int questionMarkOnOut;

    ArrayList<ResourceContainer> inputBuffer;
    ArrayList<ResourceContainer> outputBuffer;

    public LeaderCardProduction(ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output, int QMI, int QMO) {
        this.input = new ArrayList<>(input);
        this.output = new ArrayList<>(output);
        this.inputBuffer = input;
        this.outputBuffer = output;

        this.questionMarkOnInput = QMI;
        this.questionMarkOnOut = QMO;

    }


    //SLOT MANAGEMENT---------------------------------------------------------------------------------------------------
    @Override
    public boolean hasQuestionMarks(){
        return (questionMarkOnInput>0 || questionMarkOnOut >0);
    }

    @Override
    public boolean fillQuestionMarkInput(ResourceType definedInput) throws NullPointerException, IllegalArgumentException{
        return definedInput != null && inputBuffer.add(new ResourceContainer(definedInput, 1));
    }

    @Override
    public boolean fillQuestionMarkOutput(ResourceType definedOutput) throws NullPointerException, IllegalArgumentException{
        return definedOutput != null && outputBuffer.add((new ResourceContainer(definedOutput, 1)));
    }

    /**
     * clears the current buffer and then sets them to the original input/output's data
     * @return true if the add executes without errors
     */
    @Override
    public boolean clearCurrentBuffer() {
        inputBuffer.clear();
        outputBuffer.clear();

        for (ResourceContainer rs: input) {
            if(!inputBuffer.add(rs))
                return false;
        }

        for (ResourceContainer rs: output) {
            if(!outputBuffer.add(rs))
                return false;
        }

        return true;
    }

    @Override
    public boolean insertOnTop(DevelopmentCard newDevelopmentCard) {
        return false;
    }

    @Override
    public int countCardsWith(int level, Colour c) {
        return 0;
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    @Override
    public ArrayList<ResourceContainer> getProductionInput(){
        return getInputBuffer();
    }

    @Override
    public ArrayList<ResourceContainer> getProductionOutput(){
        return getOutputBuffer();
    }

    @Override
    public int getQMI() {
        return questionMarkOnInput;
    }

    @Override
    public int getQMO() {
        return questionMarkOnOut;
    }


    public void setQuestionMarkOnOut(int getQuestionMarkOnOut) {
        this.questionMarkOnOut = getQuestionMarkOnOut;
    }

    public ArrayList<ResourceContainer> getInputBuffer() {
        return inputBuffer;
    }

    public ArrayList<ResourceContainer> getOutputBuffer() {
        return outputBuffer;
    }

    public ArrayList<ResourceContainer> getInput() {
        return input;
    }

    public ArrayList<ResourceContainer> getOutput() {
        return output;
    }

    @Override
    public int getVictoryPoints() {
        return 0;
    }
    //------------------------------------------------------------------------------------------------------------------


}
