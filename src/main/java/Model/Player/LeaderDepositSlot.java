package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;


public class LeaderDepositSlot extends DepositSlot {

    public LeaderDepositSlot(int maxDim, ResourceType depositResourceType) {
        super(maxDim, depositResourceType);
    }


    @Override
    public void setDepositResourceType(ResourceType depositResourceType) {
        System.out.println("You cannot modify the resource type on a LeaderDeposit");
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
        int quantityThatIwantToAdd = inputContainer.getQta();

        if (!this.getStorageArea().isTheSameType(inputContainer)) {
            throw new DifferentResourceType("Not the same type");
        }else if (canAdd(quantityThatIwantToAdd)) {
            return true;
        }else{
            throw new DepositSlotMaxDimExceeded("Maximum dimension exceeded");
        }
    }

    /**
     * It's the function that gives the permission to remove or not to the Controller
     * @param inputContainer
     * @return true if he can
     * @throws DifferentResourceType when there is a ResourceType mismatch
     * @throws NotEnoughResources when the resources are insufficient
     */
    @Override
    public Boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources {
        int quantityThatIwantToRemove = inputContainer.getQta();

        if (!this.getStorageArea().isTheSameType(inputContainer)) {
            throw new DifferentResourceType("Not the same type");

        }else if (!this.getStorageArea().hasEnough(inputContainer)) {
            throw new NotEnoughResources("Not enough resources");

        }else {
            return true;
        }
    }

    /**
     * adds the quantity from a resourceContainer in any case.
     * It needs to be called after CanAddToDepositSlot if the user wants to follow the rules
     * @param inputContainer
     * @return true if there were no exceptions
     */
    @Override
    public Boolean addToDepositSlot(ResourceContainer inputContainer) {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToAdd = inputContainer.getQta();

        this.getStorageArea().setQta(quantityInsideTheSlot + quantityThatIwantToAdd);
        return true;
    }


    /**
     * removes the quantity from a resourceContainer
     * It needs to be called after CanRemoveFromDepositSlot if the user wants to follow the rules
     * @param inputContainer
     * @return true if there were no exceptions
     */
    @Override
    public Boolean removeFromDepositSlot(ResourceContainer inputContainer) {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToRemove = inputContainer.getQta();

        this.getStorageArea().setQta(quantityInsideTheSlot + quantityThatIwantToRemove);
        return true;

    }

}
