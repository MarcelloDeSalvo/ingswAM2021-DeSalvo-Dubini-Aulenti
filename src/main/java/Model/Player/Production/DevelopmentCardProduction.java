package Model.Player.Production;

import Model.Cards.*;
import Model.Deck;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;

public class DevelopmentCardProduction implements ProductionSlot {
    private final Deck dev;

    public DevelopmentCardProduction() {
        dev = new Deck();
    }


    //SLOT MANAGEMENT---------------------------------------------------------------------------------------------------
    /**
     * When the stack is empty it checks if newDevelopmentCard's level is '1' when this is the case
     * it simply sets the new card status to "ON_TOP" and adds the new card in the stack
     * if the stack already has elements in it and if level+1 of the element on top == new card level
     * the method sets the first element of the stack to "ACTIVE" and then adds the new one
     * @param newDevelopmentCard a new DevelopmentCard to add to the queue in Deck
     * @return true if everything goes right, false if you can't add the newDevelopmentCard for some reasons
     */
    public boolean insertOnTop (DevelopmentCard newDevelopmentCard) {
        if(dev.getDeck().peekLast() != null) {
            if(dev.getDeck().element().getLevel()+1 == newDevelopmentCard.getLevel()) {
                dev.getDeck().element().changeStatus(Status.ACTIVE);
                newDevelopmentCard.changeStatus(Status.ON_TOP);
                dev.getDeck().push(newDevelopmentCard);
            }
            else
                return false;
        }
        else {
            if(newDevelopmentCard.getLevel() == 1) {
                newDevelopmentCard.changeStatus(Status.ON_TOP);
                dev.getDeck().push(newDevelopmentCard);
            }
            else
                return false;
        }

        return true;
    }


    @Override
    public int countCardsWith(int level, Colour c) {
        int i = 0;
        for (DevelopmentCard d: dev.getDeck()) {
            if (d.isSameLevelandColour(level, c))
                i++;
        }
        return i;
    }
    //------------------------------------------------------------------------------------------------------------------



    //GETTER AND SETTER------------------------------------------------------------------------------------------------
    public DevelopmentCard getElementOnTop () {
        return dev.getDeck().peek();
    }

    @Override
    public ArrayList<ResourceContainer> getProductionInput() {
        if(dev.getDeck().peek() != null)
            return dev.getDeck().element().getInput();
        else
            return null;
    }

    @Override
    public ArrayList<ResourceContainer> getProductionOutput() {
        if(dev.getDeck().peek() != null)
            return dev.getDeck().element().getOutput();
        else
            return null;
    }

    public Deck getSlot() {
        return dev;
    }
    //------------------------------------------------------------------------------------------------------------------


    //TO BE IMPLEMENTED METHODS---(to code if we want to make it extendable [Development Cards with question marks])----
    @Override
    public boolean fillQuestionMarkInput(ResourceType definedInput) throws NullPointerException, IllegalArgumentException{
        //if(definedInput!=null && inputBuffer.add(new ResourceContainer(definedInput, 1)))
        //    return true;
        return false;
    }


    @Override
    public boolean fillQuestionMarkOutput(ResourceType definedOutput) throws NullPointerException, IllegalArgumentException{
       // if(definedOutput!=null && outputBuffer.add((new ResourceContainer(definedOutput, 1))))
       //     return true;
        return false;
    }

    /**
     * clears the current buffer and then sets them to the original input/output's data
     * @return true if the add executes without errors
     */
    @Override
    public boolean clearCurrentBuffer() {
      /*  inputBuffer.clear();
        outputBuffer.clear();

        for (ResourceContainer rs: input) {
            if(!inputBuffer.add(rs))
                return false;
        }

        for (ResourceContainer rs: output) {
            if(!outputBuffer.add(rs))
                return false;
        }*/

        return true;
    }


    @Override
    public boolean hasQuestionMarks(){
       // return (questionMarkOnInput>0 || questionMarkOnOut >0);
        return false;
    }

    @Override
    public int getQMI() {
        return 0;
    }

    @Override
    public int getQMO() {
        return 0;
    }

    //---------------------------------------------------------------------------------
}
