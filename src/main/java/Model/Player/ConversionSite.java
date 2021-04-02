package Model.Player;

import Model.Player.Deposit.DepositSlot;
import Model.Resources.ResourceContainer;
import Model.Exceptions.*;
import Model.Resources.ResourceType;

import java.util.ArrayList;
import java.util.Iterator;

public class ConversionSite {
    /**
     * List of available conversion slots
      */
    private ArrayList<ResourceContainer> conversionsAvailable;
    private ResourceType defaultConverted;

    public ConversionSite() {
        this.defaultConverted = ResourceType.BLANK;
        this.conversionsAvailable=new ArrayList<ResourceContainer>();
    }

    /**
     * This method adds a conversion  to the list of the ones available.
     * @param inputConversion
     * @return false if the argument is null, true otherwise.
     */
    public boolean addConversion (ResourceContainer inputConversion){
        if(inputConversion!= null){
            conversionsAvailable.add(inputConversion);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * canConvert checks if and how many conversions are available
     * @return true if there's only one conversion
     * @throws MultipleConversionsActive if there's more than one conversion leader and a choice by the user is required
     */
    public boolean canConvert ( )  throws  MultipleConversionsActive{
        switch (conversionsAvailable.size()){
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new MultipleConversionsActive("You have more than one conversion available. Which one would you like to use?");
        }
    }

 /*     this part of the conversion has been moved to the Controller as a choice from the user is required if MultipleConversionsActive is thrown.
 public boolean convertMarketArray(ArrayList<ResourceContainer> marketOutput) throws NoConversionActive, MultipleConversionsActive {
        Iterator<ResourceContainer> iter = marketOutput.iterator();
        ResourceContainer current;
        while(iter.hasNext()){
            current=iter.next();
            if(current.getResourceType()==ResourceType.BLANK){
                convertSingleBlank(current,conversionsAvailable.get(1));
            }
        }

        return true;


                /*Iterator<ResourceContainer> iter = marketInput.iterator();
        ResourceContainer current;
        while(iter.hasNext()){
            try{
                current= iter.next();
                if(conversionSite.canConvert() &&  (current.getResourceType()==conversionSite.getDefaultConverted()))
                    System.out.println("Pippo !");
                    System.out.println(current.getResourceType());
                    conversionSite.convertSingleBlank(current,conversionSite.getConversionsAvailable().get(0));


            }
            catch (NoConversionActive noConversionActive){
            }
            catch (MultipleConversionsActive multipleConversionsActive){
            }

        }*/


    /**
     * converts an input ResourceContainer into another one (possibly taken from availableConversions)
     * @param input
     * @param chosenConversion
     * @return
     */
    public boolean convertSingleBlank (ResourceContainer input,ResourceContainer chosenConversion){
        input.setQty(chosenConversion.getQty());
        input.setResourceType(chosenConversion.getResourceType());
        return true;
    }


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
}
