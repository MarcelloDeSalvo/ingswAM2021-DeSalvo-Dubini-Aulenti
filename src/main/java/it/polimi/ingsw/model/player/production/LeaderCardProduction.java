package it.polimi.ingsw.model.player.production;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.ProductionAbility;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;


public class LeaderCardProduction implements ProductionSlot {

    /**
     * Contains the reference to the production input/output and the questionMarks
     */
    private final ProductionAbility productionAbility;

    private final ArrayList<ResourceContainer> inputBuffer;
    private final ArrayList<ResourceContainer> outputBuffer;

    public LeaderCardProduction(ProductionAbility productionAbility) {
        this.inputBuffer = new ArrayList<>(productionAbility.getInput());
        this.outputBuffer = new ArrayList<>(productionAbility.getOutput());
        this.productionAbility = productionAbility;

    }


    //SLOT MANAGEMENT---------------------------------------------------------------------------------------------------
    @Override
    public boolean hasQuestionMarks(){
        return (productionAbility.getQuestionMarkOnInput()>0 || productionAbility.getQuestionMarkOnOutput() >0);
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

        for (ResourceContainer rs: productionAbility.getInput()) {
            if(!inputBuffer.add(rs))
                return false;
        }

        for (ResourceContainer rs: productionAbility.getOutput()) {
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
    public boolean canInsertOnTop(DevelopmentCard newDevelopmentCard) {
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
        return inputBuffer;
    }

    @Override
    public ArrayList<ResourceContainer> getProductionOutput(){
        return outputBuffer;
    }

    @Override
    public int getQMI() {
        return productionAbility.getQuestionMarkOnInput();
    }

    @Override
    public int getQMO() {
        return productionAbility.getQuestionMarkOnOutput();
    }

    public ArrayList<ResourceContainer> getInputBuffer() {
        return inputBuffer;
    }

    public ArrayList<ResourceContainer> getOutputBuffer() {
        return outputBuffer;
    }

    @Override
    public int getVictoryPoints() {
        return 0;
    }
    //------------------------------------------------------------------------------------------------------------------


    //TO-STRING---------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "DEVELOPMENT SLOT " + "\n" +
                productionAbility.toString();
    }
    //------------------------------------------------------------------------------------------------------------------
}
