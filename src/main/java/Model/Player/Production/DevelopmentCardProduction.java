package Model.Player.Production;

import Model.Cards.*;
import Model.Deck;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;

public class DevelopmentCardProduction implements ProductionSlot {
    private Deck dev;

    public DevelopmentCardProduction() {
        dev = new Deck();
    }

    public boolean insertOnTop (DevelopmentCard developmentCard) {
        if(dev.getDeck().peek() != null)
            dev.getDeck().element().changeStatus(Status.ACTIVE);

        developmentCard.changeStatus(Status.ON_TOP);
        dev.getDeck().add(developmentCard);

        return true;
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

    @Override
    public int countCardsWith(int level, Colour c) {
        int i = 0;
        for (DevelopmentCard d: dev.getDeck()) {
            if (d.isSameLevelandColour(level, c))
                i++;
        }
        return i;
    }


    //implemented methods ------------------------------------
    @Override
    public boolean fillQuestionMarkInput(ResourceType definedInput) throws NullPointerException, IllegalArgumentException{
        //if(definedInput!=null && inputBuffer.add(new ResourceContainer(definedInput, 1)))
        //    return true;
        return false;
    }


    @Override
    public boolean fillQuestionMarkOutput(ResourceType definedOutput) throws NullPointerException, IllegalArgumentException{
       // if(definedOutput!=null && inputBuffer.add((new ResourceContainer(definedOutput, 1))))
       //     return true;
        return false;
    }

    /**
     * clears the current buffer and then sets them to the original input/otuput's data
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

    //---------------------------------------------------------------------------------
}
