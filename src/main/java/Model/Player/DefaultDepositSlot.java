package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;


public class DefaultDepositSlot extends DepositSlot {

    public DefaultDepositSlot(ResourceType depositResourceType, int maxDim) {
        super(depositResourceType, maxDim);
    }

    /**
     * It's the function that gives the permission to add or not to the Controller
     * @param inputContainer
     * @return true if he can add the resources
     * @throws DifferentResourceType when there is a ResourceType mismatch
     * @throws DepositSlotMaxDimExceeded when it would add too many resources
     */
    @Override
    public Boolean canAddtoDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, DepositSlotMaxDimExceeded {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToAdd = inputContainer.getQta();

        if(!inputContainer.isTheSameType(this.getStorageArea())) {
            if(quantityInsideTheSlot == 0) {
                if(canAdd(quantityThatIwantToAdd)) {
                    return true;
                }else{
                    throw new DepositSlotMaxDimExceeded("Not enough space");
                }
            }else {
                throw new DifferentResourceType("Not the same type");
            }

        }else if(canAdd(quantityThatIwantToAdd)){
            return true;
        }else {
            throw new DepositSlotMaxDimExceeded("Not enough space");
        }
    }

    /**
     * It's the function that gives the permission to remove or not to the Controller
     * @param inputContainer
     * @return true if he can
     * @throws DifferentResourceType when there is a ResourceType mismatch
     * @throws NotEnoughResources when the resources are insufficient
     */
    public Boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources {
        if(!this.getStorageArea().isTheSameType(inputContainer) ) {
            throw new DifferentResourceType("Not the same type");

        }else if (!this.getStorageArea().hasEnough(inputContainer)) {
            throw new NotEnoughResources("Not enough resources");

        }else {
            return true;
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

        this.getStorageArea().addQta(quantityThatIwantToAdd);
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
        int quantityThatIwantToRemove = inputContainer.getQta();

        this.getStorageArea().addQta(-quantityThatIwantToRemove);
        return true;
    }

}

