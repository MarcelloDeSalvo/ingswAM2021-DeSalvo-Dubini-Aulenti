package Model.Player.Deposit;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Exceptions.ResourceTypeAlreadyStored;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;


public class LeaderDeposit extends DepositSlot {

    public LeaderDeposit(ResourceType depositResourceType, int maxDim) {
        super(depositResourceType, maxDim);
    }


    //DEPOSIT MANAGEMENT------------------------------------------------------------------------------------------------
    @Override
    public void setDepositResourceType(ResourceType depositResourceType) {
        //System.out.println("You cannot modify the resource type on a LeaderDeposit");
    }

    @Override
    public boolean canAddToDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, DepositSlotMaxDimExceeded {
        int quantityThatIWantToAdd = inputContainer.getQty();

        if (!this.getDepositContainer().isTheSameType(inputContainer))
            throw new DifferentResourceType("Not the same type");

        if (canAdd(quantityThatIWantToAdd))
            return true;
        else
            throw new DepositSlotMaxDimExceeded("Maximum dimension exceeded");

    }

    @Override
    public boolean addToDepositSlot(ResourceContainer inputContainer) {
        int quantityThatIWantToAdd = inputContainer.getQty();

        this.getDepositContainer().addQty(quantityThatIWantToAdd);

        return true;
    }


    @Override
    public boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources {

        if (!this.getDepositContainer().isTheSameType(inputContainer))
            throw new DifferentResourceType("Not the same type");

        if (!this.getDepositContainer().hasEnough(inputContainer))
            throw new NotEnoughResources("Not enough resources");

        addToBuffer(inputContainer);
        return true;
    }

    @Override
    public boolean removeFromDepositSlot(ResourceContainer inputContainer) {
        int quantityThatIWantToRemove = inputContainer.getQty();

        this.getDepositContainer().addQty(-quantityThatIWantToRemove);
        return true;

    }

    /**
     * gives the controller the permission to move a desired quantity from one deposit to another
     * @param destination is the deposit where the user wants the resources to be moved
     * @return true if the LeaderDeposit can transfer his resources with another generic deposit
     * @throws NotEnoughResources if the user wants to move a quantity that's greater than the selected deposit's max dimension
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     */
    @Override
    public boolean canTransferTo(DepositSlot destination, int quantityThatIWantToTransfer) throws DifferentResourceType, NotEnoughResources, DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored {
        if(!this.canRemove(quantityThatIWantToTransfer))
            throw  new NotEnoughResources("Not enough resources");

        if(!destination.isTheSameType(this) && !destination.isEmpty())
            throw new DifferentResourceType("Not the same type");

        ResourceContainer send = new ResourceContainer(this.getDepositResourceType(), quantityThatIWantToTransfer);
        return destination.canAddToDepositSlot(send);
    }

    /**
     * Used when the Deposit's type allows the storage of different ResourceType
     * Gives the controller the permission to switch a desired quantity from one deposit to another
     * @return false because the LeaderDeposit (by default) can only store one ResourceType
     */
    @Override
    public boolean canSwitchWith(DepositSlot destination){
        return false;
    }
    //------------------------------------------------------------------------------------------------------------------


}
