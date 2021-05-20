package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.resources.ResourceType;

import java.util.ArrayList;

public class InputCheck {

    //INPUT CHECK-------------------------------------------------------------------------------------------------------

    public static boolean not_vault_or_deposit(String x){
        return !x.equalsIgnoreCase("DEPOSIT") && !x.equalsIgnoreCase("VAULT");
    }

    public static boolean not_vault(String x){
        return !x.equalsIgnoreCase("VAULT");
    }

    public static boolean not_deposit(String x){
        return !x.equalsIgnoreCase("DEPOSIT");
    }

    public static boolean not_from(String x){
        return !x.equalsIgnoreCase("FROM");
    }

    public static boolean not_to(String x){
        return !x.equalsIgnoreCase("TO");
    }

    public static boolean not_with(String x){ return !x.equalsIgnoreCase("WITH"); }

    public static boolean not_in(String x){ return !x.equalsIgnoreCase("IN"); }


    public static boolean not_row_or_column(String x){
        return !x.equalsIgnoreCase("ROW") && !x.equalsIgnoreCase("COLUMN");
    }

    public static boolean duplicatedElement(ArrayList<Integer> x) {
        for(int i = 0; i < x.size(); i++) {
            int currElement = x.get(i);

            for(int j = 0; j < x.size(); j++) {
                if(i != j && currElement == x.get(j))
                    return true;
            }
        }

        return false;
    }


    /**
     * Checks if a string can be converted into a ResourceType
     * @param x is the input string
     * @return the ResourceType value if the string can be converted or NULL
     */
    public static ResourceType resourceType_null(String x){
        ResourceType resourceType;

        try {
            resourceType = ResourceType.valueOf(x.toUpperCase());

        }catch (IllegalArgumentException e){
            return null;
        }

        return resourceType;
    }
    //------------------------------------------------------------------------------------------------------------------

}
