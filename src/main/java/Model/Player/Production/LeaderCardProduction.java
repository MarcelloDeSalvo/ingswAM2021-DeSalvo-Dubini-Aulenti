package Model.Player.Production;

import Model.Cards.Colour;
import Model.Exceptions.MaterialChoiceRequired;
import Model.Resources.ResourceContainer;
import java.util.ArrayList;


public class LeaderCardProduction implements ProductionSlot {

    ArrayList<ResourceContainer> input; //only defined input types
    int questionMarkOnInput;
    ArrayList<ResourceContainer> output;
    int questionMarkOnOut;

    ArrayList<ResourceContainer> inputBuffer;
    ArrayList<ResourceContainer> outputBuffer;

    public LeaderCardProduction(ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output, int QMI, int QMO) {
        this.input = input;
        this.output = output;
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

    /**
     * Called by Controller when the user defines a Question Mark input
     * @param definedInput
     * @return true if there is no errors
     */
    public boolean addToBufferInput(ResourceContainer definedInput){
        if(definedInput!=null && inputBuffer.add(definedInput))
            return true;
        return false;
    }

    /**
     * Called by Controller when the user defines a Question Mark output
     * @param definedOutput
     * @return true if there is no errors
     */
    public boolean addToBufferOutput(ResourceContainer definedOutput) throws NullPointerException, IllegalArgumentException{
        if(definedOutput!=null && inputBuffer.add(definedOutput))
            return true;
        return false;
    }

    public boolean hasQuestionMarks(){
        return (questionMarkOnInput>0 || questionMarkOnOut >0);
    }


    //-----------------


    public int getQuestionMarkOnInput() {
        return questionMarkOnInput;
    }

    public void setQuestionMarkOnInput(int questionMarkOnInput) {
        this.questionMarkOnInput = questionMarkOnInput;
    }

    public int getQuestionMarkOnOut() {
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
