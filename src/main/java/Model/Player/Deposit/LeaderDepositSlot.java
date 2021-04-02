package Model.Player.Deposit;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Exceptions.ResourceTypeAlreadyStored;
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


    @Override
    public boolean canAddToDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, DepositSlotMaxDimExceeded {
        int quantityThatIwantToAdd = inputContainer.getQty();

        if (!this.getDepositContainer().isTheSameType(inputContainer))
            throw new DifferentResourceType("Not the same type");

        if (canAdd(quantityThatIwantToAdd))
            return true;
        else
            throw new DepositSlotMaxDimExceeded("Maximum dimension exceeded");

    }


    @Override
    public boolean addToDepositSlot(ResourceContainer inputContainer) {
        int quantityThatIwantToAdd = inputContainer.getQty();

        this.getDepositContainer().addQty(quantityThatIwantToAdd);

        return true;
    }


    @Override
    public boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources {

        if (!this.getDepositContainer().isTheSameType(inputContainer))
            throw new DifferentResourceType("Not the same type");

        if (!this.getDepositContainer().hasEnough(inputContainer))
            throw new NotEnoughResources("Not enough resources");

        return true;
    }



    @Override
    public boolean removeFromDepositSlot(ResourceContainer inputContainer) {
        int quantityThatIwantToRemove = inputContainer.getQty();

        this.getDepositContainer().addQty(-quantityThatIwantToRemove);
        return true;

    }

    /**
     * gives the controller the permission to move a desired quantity from one deposit to another
     * @param destination is the deposit where the user wants the resources to be moved
     * @param quantityThatIwantToTransfer
     * @return true if the LeaderDeposit can transfer his resources with another generic deposit
     * @throws NotEnoughResources if the user wants to move a quantity that's greater than the selected deposit's max dimension
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     */
    @Override
    public boolean canTransferTo(DepositSlot destination, int quantityThatIwantToTransfer) throws DifferentResourceType, NotEnoughResources, DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored {
        if(!this.canRemove(quantityThatIwantToTransfer))
            throw  new NotEnoughResources("Not enough resources");

        if(!destination.isTheSameType(this) && !destination.isEmpty())
            throw new DifferentResourceType("Not the same type");

        ResourceContainer send = new ResourceContainer(this.getDepositResourceType(), quantityThatIwantToTransfer);
        if (destination.canAddToDepositSlot(send))
            return true;

        return false;
    }

    /**
     * @return false because the LeaderDeposit (by default) can only store one ResourceType
     */
    @Override
    public boolean canSwitchWith(DepositSlot destination) throws DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored{
        return false;
    }


}