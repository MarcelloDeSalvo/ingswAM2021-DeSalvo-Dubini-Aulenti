package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;
import java.util.Iterator;


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
    public ConversionMode canConvert (){
        switch (conversionsAvailable.size()){
            case 0:
                return ConversionMode.INACTIVE;
            case 1:
                return ConversionMode.AUTOMATIC;
            default:
                return ConversionMode.CHOICE_REQUIRED;
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

    /**
     * Counts the convertible marbles inside an array
     * @return the counter
     */
    public int countConvertible(ArrayList<ResourceContainer> marketOut){
        int i=0;
        for (ResourceContainer c: marketOut ) {
            if (c.getResourceType() == defaultConverted)
                i++;
        }

        return i;
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
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
