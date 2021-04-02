package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Exceptions.*;
import Model.Resources.ResourceType;

import java.util.ArrayList;

public class ConversionSite {
    /**
     * List of available conversion slots
      */
    private ArrayList<LeaderConversionSlot> conversionSlots;


    public ConversionSite(){
        this.conversionSlots=new ArrayList<LeaderConversionSlot>();
    }

    /**
     * This method adds a conversion type leader abilty to the list of the ones available.
     * @param lcs
     * @return false if the argument is null, true otherwise.
     */
    public boolean addConversionSlot (LeaderConversionSlot lcs){
        if(lcs!= null){
            conversionSlots.add(lcs);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * canConvert checks if and how many leader conversions are available
     * @return true if there's only one leader
     * @throws NoConversionLeadersActive if there's no conversion leader and thus no conversion is required
     * @throws MultipleConversionLeadersActive if there's more than one conversion leader and a choice by the user is required
     */
    public boolean canConvert ( )  throws NoConversionLeadersActive, MultipleConversionLeadersActive{
        switch (conversionSlots.size()){
            case 0:
                throw new NoConversionLeadersActive("You have no current conversion leaders active");
            case 1:
                return true;
            default:
                throw new MultipleConversionLeadersActive("You have more than one conversion leader");
        }
    }


    public boolean convert(ArrayList<ResourceContainer>  inputMarket ) throws NoConversionLeadersActive, MultipleConversionLeadersActive{




        return false;
    }











}
