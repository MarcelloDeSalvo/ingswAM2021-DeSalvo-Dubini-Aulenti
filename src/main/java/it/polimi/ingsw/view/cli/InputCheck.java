package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.resources.ResourceType;

public class InputCheck {

    //INPUT CHECK-------------------------------------------------------------------------------------------------------

    public static boolean not_vault_or_deposit(String x){
        return !x.toUpperCase().equals("DEPOSIT") && !x.toUpperCase().equals("VAULT");
    }

    public static boolean not_vault(String x){
        return !x.toUpperCase().equals("VAULT");
    }

    public static boolean not_deposit(String x){
        return !x.toUpperCase().equals("DEPOSIT");
    }

    public static boolean not_from(String x){
        return !x.toUpperCase().equals("FROM");
    }

    public static boolean not_to(String x){
        return !x.toUpperCase().equals("TO");
    }

    public static boolean not_row_or_column(String x){
        return !x.toUpperCase().equals("ROW") && !x.toUpperCase().equals("COLUMN");
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
