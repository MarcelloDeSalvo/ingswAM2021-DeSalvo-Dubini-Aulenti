package Model.Player;

import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;


public class DefaultDepositSlot extends DepositSlot {

    public DefaultDepositSlot(int maxDim, ResourceType depositResourceType) {
        super(maxDim,  depositResourceType);
    }

    /**
     * It's the function that gives the permission to add or not to the Controller
     * @param inputContainer
     * @return
     * @throws DifferentResourceType
     * @throws NotEnoughResources
     */
    @Override
    public Boolean canAddtoDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToAdd = inputContainer.getQta();

        try {
            if(this.getStorageArea().isTheSameType(inputContainer)) {
                if(quantityInsideTheSlot == 0) {
                    return true;
                }else {
                    return false;
                }
            }else {
                if(canAdd(quantityThatIwantToAdd)){
                    return true;
                }else{
                    throw new NotEnoughResources("Not enough space");
                }
            }
        }catch (DifferentResourceType e){
            throw e;
        }

    }

    public Boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources {
        try{
            this.getStorageArea().canRemove(inputContainer);
            return true;
        }catch (DifferentResourceType | NotEnoughResources e){
            throw  e;
        }
    }


    /**
     * Adds the resources in any case.
     * It needs to be called after CanAddToDepositSlot if the user wants to follow the rules
     * @param inputContainer
     * @return true
     */
    @Override
    public Boolean addToDepositSlot(ResourceContainer inputContainer)  {
        int quantityThatIwantToAdd = inputContainer.getQta();

        this.getStorageArea().setQta(quantityThatIwantToAdd);
        this.getStorageArea().setResourceType(inputContainer.getResourceType());
        return true;

    }

    /**
     * Removes the resources in any case.
     * It needs to be called after CanRemoveFromDepositSlot if the user wants to follow the rules
     * @param inputContainer
     * @return true
     */
    @Override
    public Boolean removeFromDepositSlot(ResourceContainer inputContainer){
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToRemove = inputContainer.getQta();

        this.getStorageArea().setQta(quantityInsideTheSlot- quantityThatIwantToRemove);
        return true;
    }

    /*
    default = new DefaultDeposit(2, pietre)

    -- utente decide di usare il suo turno per comprare
    -- utente seleziona carta
    -- controller riceve la carta (5 pietre)
    -- chiede a utente come vuole pagare (deposito e/o vault)
    -- [1 from DefaultDeposit1 | 3 Vault | 1 LeaderDeposit] -> Deposit
    --
    chiama canRemove(prezzo)
    -- se può chiama RemoveFromDepositSlot
     */


}

