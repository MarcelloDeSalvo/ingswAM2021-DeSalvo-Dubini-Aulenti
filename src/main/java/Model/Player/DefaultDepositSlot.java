package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.io.IOException;

public class DefaultDepositSlot extends DepositSlot {

    public DefaultDepositSlot(int maxDim, ResourceType depositResourceType) {
        super(maxDim,  depositResourceType);
    }

    /**
     * It's the function that gives the permission to add or not to the Controller
     * @param inputContainer
     * @return true if you can add the Resources
     * @throws IOException when there is a ResourceType mismatch
     * @throws ArithmeticException when there is not enough space
     */
    @Override
    public Boolean canAddtoDepositSlot(ResourceContainer inputContainer) throws IOException, ArithmeticException {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToAdd = inputContainer.getQta();

        if(!sameResType(inputContainer.getResourceType())) {
            if(quantityInsideTheSlot == 0) {
                return true;
            }else {
                throw new IOException("Not the same type");
            }
        }else {
            if(canAdd(quantityThatIwantToAdd)){
                return true;
            }else{
                throw new ArithmeticException("Not enough space");
            }
        }
    }

    /**
     * It's the function that gives the permission to remove or not to the Controller
     * @param inputContainer
     * @return true if can you remove the resources
     * @throws IOException when there is a ResourceType mismatch
     * @throws ArithmeticException when there are not enough resources
     */
    @Override
    public Boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws IOException, ArithmeticException {
        int quantityThatIwantToRemove = inputContainer.getQta();

        if(!sameResType(inputContainer.getResourceType())) {
            throw new IOException("Not the same type");

        }else {
            if(canRemove(quantityThatIwantToRemove)){
                return true;
            }else{
                throw new ArithmeticException("Not enough resources");
            }
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
}

