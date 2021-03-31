package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;

public class LeaderConversionSlot implements ConvertSlot{
    private ArrayList<ResourceContainer> InputContainer;
    private ArrayList<ResourceContainer> OutputContainer;


    //getter
    public ArrayList<ResourceContainer> getInputContainer() {
        return InputContainer;
    }

    public ArrayList<ResourceContainer> getOutputContainer() {
        return OutputContainer;
    }
    //setter
    public void setInputContainer(ArrayList<ResourceContainer> inputContainer) {
        InputContainer = inputContainer;
    }

    public void setOutputContainer(ArrayList<ResourceContainer> outputContainer) {
        OutputContainer = outputContainer;
    }
}
