package Model.Player.Deposit;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Player.Deposit.DepositSlot;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;


public class LeaderDepositSlot extends DepositSlot {

    public LeaderDepositSlot(ResourceType depositResourceType, int maxDim) {
        super(depositResourceType, maxDim);
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
    public Boolean canAddToDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, DepositSlotMaxDimExceeded {
        int quantityThatIwantToAdd = inputContainer.getQty();

        if (!this.getDepositContainer().isTheSameType(inputContainer))
            throw new DifferentResourceType("Not the same type");

        if (canAdd(quantityThatIwantToAdd))
            return true;
        else
            throw new DepositSlotMaxDimExceeded("Maximum dimension exceeded");

    }

    /**
     * adds the quantity from a resourceContainer in any case.
     * It needs to be called after canAddToDepositSlot if the user wants to follow the rules
     * @param inputContainer
     * @return true if there were no errors
     */
    @Override
    public Boolean addToDepositSlot(ResourceContainer inputContainer) {
        int quantityThatIwantToAdd = inputContainer.getQty();

        this.getDepositContainer().addQty(quantityThatIwantToAdd);

        return true;
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

        if (!this.getDepositContainer().isTheSameType(inputContainer))
            throw new DifferentResourceType("Not the same type");

        if (!this.getDepositContainer().hasEnough(inputContainer))
            throw new NotEnoughResources("Not enough resources");

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
        int quantityThatIwantToRemove = inputContainer.getQty();

        this.getDepositContainer().addQty(-quantityThatIwantToRemove);
        return true;

    }

}
