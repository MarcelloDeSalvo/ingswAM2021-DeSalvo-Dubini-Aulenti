package Model.Player;

import Model.Resources.ResourceContainer;

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


    public boolean canConvert () /* throws NoLeadersActive, MultipleLeadersActive*/{
        return true;
    }





}
