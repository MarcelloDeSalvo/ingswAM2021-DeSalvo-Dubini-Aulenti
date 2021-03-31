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


    @Override
    public Boolean addToDepositSlot(ResourceContainer inputContainer) {
        int quantityThatIwantToAdd = inputContainer.getQty();

        this.getDepositContainer().addQty(quantityThatIwantToAdd);

        return true;
    }


    @Override
    public Boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources {

        if (!this.getDepositContainer().isTheSameType(inputContainer))
            throw new DifferentResourceType("Not the same type");

        if (!this.getDepositContainer().hasEnough(inputContainer))
            throw new NotEnoughResources("Not enough resources");

        return true;
    }



    @Override
    public Boolean removeFromDepositSlot(ResourceContainer inputContainer) {
        int quantityThatIwantToRemove = inputContainer.getQty();

        this.getDepositContainer().addQty(-quantityThatIwantToRemove);
        return true;

    }

    /**
     * gives the controller the permission to move a desired quantity from one deposit to another
     * @param destination is the deposit where the user wants the resources to be moved
     * @param quantityThatIwantToSwitch
     * @return true if the LeaderDeposit can transfer his resources with another generic deposit
     * @throws NotEnoughResources if the user wants to move a quantity that's greater than the selected deposit's max dimension
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     */
    @Override
    public boolean canSwitchWith(DepositSlot destination, int quantityThatIwantToSwitch) throws DifferentResourceType, NotEnoughResources, DepositSlotMaxDimExceeded{
        if(!this.canRemove(quantityThatIwantToSwitch))
            throw  new NotEnoughResources("Not enough resources");

        if(!destination.isTheSameType(this) && !destination.isEmpty())
            throw new DifferentResourceType("Not the same type");

        if (quantityThatIwantToSwitch > destination.getMaxDim())
            throw new DepositSlotMaxDimExceeded("Maximum dimension excedeed");

        return true;
    }


}
