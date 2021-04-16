package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Exceptions.*;
import Model.Resources.ResourceType;

import java.util.ArrayList;
import java.util.Iterator;

enum conversionMode{
    INACTIVE, AUTOMATIC, CHOICE_REQUIRED;
}


public class ConversionSite {
    /**
     * List of available conversions
      */
    private ArrayList<ResourceContainer> conversionsAvailable;
    /**
     * Input resourceType of the conversion
     */
    private ResourceType defaultConverted;

    public ConversionSite() {
        this.defaultConverted = ResourceType.BLANK;
        this.conversionsAvailable = new ArrayList<>();
    }



    //CONVERSION MANAGEMENT---------------------------------------------------------------------------------------------
    /**
     * This method adds a conversion  to the list of the ones available.
     * @return false if the argument is null, true otherwise.
     */
    public boolean addConversion (ResourceContainer inputConversion){
        if(inputConversion != null){
            conversionsAvailable.add(inputConversion);
            return true;
        }
        else
            return false;
    }

    /**
     * canConvert checks if and how many conversions are available
     * @return true if there's only one conversion
     */
    public conversionMode canConvert (){
        switch (conversionsAvailable.size()){
            case 0:
                return conversionMode.INACTIVE;
            case 1:
                return conversionMode.AUTOMATIC;
            default:
                return conversionMode.CHOICE_REQUIRED;
        }
    }

    /**
     * Method called for conversion when there's only one a single conversion available, thus no choice by the user is needed.
     * @return The converted input array
     */
    public boolean convert(ArrayList<ResourceContainer> marketOutput){
        Iterator<ResourceContainer> iter = marketOutput.iterator();
        ResourceContainer current;
        while (iter.hasNext()) {
            current = iter.next();
            if (current.getResourceType() == defaultConverted) {
                convertSingleElement(current, conversionsAvailable.get(0));
            }
        }

        return true;
    }

    /**
     * Converts an input ResourceContainer into another one (possibly taken from availableConversions)
     */
    public boolean convertSingleElement (ResourceContainer input, ResourceContainer chosenConversion){
        input.setQty(chosenConversion.getQty());
        input.setResourceType(chosenConversion.getResourceType());
        return true;
    }
    //-----------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER------------------------------------------------------------------------------------------------
    public ArrayList<ResourceContainer> getConversionsAvailable() {
        return conversionsAvailable;
    }

    public ResourceType getDefaultConverted() {
        return defaultConverted;
    }

    public void setDefaultConverted(ResourceType defaultConverted) {
        this.defaultConverted = defaultConverted;
    }

    public void setConversionsAvailable(ArrayList<ResourceContainer> conversionsAvailable) {
        this.conversionsAvailable = conversionsAvailable;
    }
    //------------------------------------------------------------------------------------------------------------------

}
