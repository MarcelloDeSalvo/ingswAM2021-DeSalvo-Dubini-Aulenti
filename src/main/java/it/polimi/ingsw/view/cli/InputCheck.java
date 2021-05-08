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
