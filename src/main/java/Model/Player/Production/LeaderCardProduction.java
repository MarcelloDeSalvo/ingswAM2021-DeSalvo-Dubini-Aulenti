package Model.Player.Production;

import Model.Cards.Colour;
import Model.Cards.DevelopmentCard;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;


public class LeaderCardProduction implements ProductionSlot {

    ArrayList<ResourceContainer> input; //only defined input types
    int questionMarkOnInput;
    ArrayList<ResourceContainer> output;
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

    @Override
    public int countCardsWith(int level, Colour c) {
        return 0;
    }

    @Override
    public ArrayList<ResourceContainer> getProductionInput(){
        return getInputBuffer();
    }

    @Override
    public ArrayList<ResourceContainer> getProductionOutput(){
        return getOutputBuffer();
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
    public boolean hasQuestionMarks(){
        return (questionMarkOnInput>0 || questionMarkOnOut >0);
    }

    @Override
    public boolean insertOnTop(DevelopmentCard newDevelopmentCard) {
        return false;
    }


    //getter and setter


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

    public void setInputBuffer(ArrayList<ResourceContainer> inputBuffer) {
        this.inputBuffer = inputBuffer;
    }

    public ArrayList<ResourceContainer> getOutputBuffer() {
        return outputBuffer;
    }

    public void setOutputBuffer(ArrayList<ResourceContainer> outputBuffer) {
        this.outputBuffer = outputBuffer;
    }

    public ArrayList<ResourceContainer> getInput() {
        return input;
    }

    public ArrayList<ResourceContainer> getOutput() {
        return output;
    }

}
