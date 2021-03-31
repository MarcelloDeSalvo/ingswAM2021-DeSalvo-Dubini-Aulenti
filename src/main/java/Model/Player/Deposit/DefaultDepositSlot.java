package Model.Player.Deposit;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Exceptions.ResourceTypeAlreadyStored;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;
import java.util.HashSet;


public class DefaultDepositSlot extends DepositSlot {

    private static HashSet<ResourceType> notAvailableResourceType = new HashSet<>();

    public DefaultDepositSlot(int maxDim) {
        super(maxDim);
    }


    @Override
    public boolean canAddToDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored {
        int quantityThatIwantToAdd = inputContainer.getQty();

        if(isTheResourceTypeAlreadyTaken(inputContainer.getResourceType()))
            throw new ResourceTypeAlreadyStored("Another deposit is already storing the same resource type");


        if(this.isEmpty() || inputContainer.isTheSameType(this.getDepositContainer())){
            if(canAdd(quantityThatIwantToAdd))
                return true;
            else
                throw new DepositSlotMaxDimExceeded("Max dimension Exceeded");
        }

        throw new DifferentResourceType("Not the same type");
    }

    public boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources {
        if(this.isEmpty())
            throw new NotEnoughResources("Not enough resources");

        if(!this.getDepositContainer().isTheSameType(inputContainer) )
            throw new DifferentResourceType("Not the same type");

        if(!this.getDepositContainer().hasEnough(inputContainer))
            throw new NotEnoughResources("Not enough resources");

        return true;
    }

    @Override
    public boolean addToDepositSlot(ResourceContainer inputContainer)  {
        int quantityThatIwantToAdd = inputContainer.getQty();
        ResourceType inputType = inputContainer.getResourceType();

        this.getDepositContainer().addQty(quantityThatIwantToAdd);
        this.getDepositContainer().setResourceType(inputType);
        getNotAvailableResourceType().add(inputType);
        return true;
    }

    @Override
    public boolean removeFromDepositSlot(ResourceContainer inputContainer){
        int quantityThatIwantToRemove = inputContainer.getQty();
        ResourceType inputType = inputContainer.getResourceType();

        this.getDepositContainer().addQty(-quantityThatIwantToRemove);

        remakeTypeAvailableIfEmpty();

        return true;
    }

    /**
     * gives the controller the permission to move a desired quantity from one deposit to another
     * @param destination is the deposit where the user wants the resources to be moved
     * @param quantityThatIwantToTransfer
     * @return true if the Default Deposit can transfer his resources with another generic deposit
     * @throws NotEnoughResources if the user wants to move a quantity that's greater than the selected deposit's max dimension
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     */
    @Override
    public boolean canTransferTo(DepositSlot destination, int quantityThatIwantToTransfer) throws DifferentResourceType, DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored, NotEnoughResources{
        if(!this.canRemove(quantityThatIwantToTransfer))
            throw  new NotEnoughResources("Not enough resources");

        ResourceContainer send = new ResourceContainer(this.getDepositResourceType(), quantityThatIwantToTransfer);
        if(destination.canAddToDepositSlot(send))
            return true;

        return false;
    }

    @Override
    public boolean transferTo(DepositSlot destination, int quantityThatIwantToSwitch) {
        super.transferTo(destination, quantityThatIwantToSwitch);
        remakeTypeAvailableIfEmpty();
        return true;
    }

    /**
     * Used when the Deposit's type allows the storage of different ResourceType
     * Gives the controller the permission to switch a desired quantity from one deposit to another
     * @param destination is the deposit that will switch resources with the selected one
     * @return true if the Default Deposit can switch his resources with another generic deposit
     * @throws ResourceTypeAlreadyStored if the user wants to move already stored ResourceTypes
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     */
    @Override
    public boolean canSwitchWith(DepositSlot destination) throws DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored{
        int allResources = this.getResourceQty();

        if(allResources> destination.getMaxDim())
            throw new DepositSlotMaxDimExceeded("They don't have enough available space");

        if(isTheResourceTypeAlreadyTaken(destination.getDepositResourceType()))
            throw new ResourceTypeAlreadyStored("Another deposit is already storing the same resource type");


        return true;
    }


    private boolean isTheResourceTypeAlreadyTaken(ResourceType inputResType){

        if(this.getDepositResourceType() == null)
            return false;

        if(inputResType == this.getDepositResourceType())
            return false;

        if(!getNotAvailableResourceType().contains(inputResType))
            return false;

        return true;
    }

    //getter and setter
    public static HashSet<ResourceType> getNotAvailableResourceType() {
        return notAvailableResourceType;
    }

    public static void setNotAvailableResourceType(HashSet<ResourceType> notAvailableResourceType) {
        DefaultDepositSlot.notAvailableResourceType = notAvailableResourceType;
    }

    //private methods
    private void remakeTypeAvailableIfEmpty(){
        if(this.isEmpty())
            getNotAvailableResourceType().remove(this.getDepositResourceType());
    }

}

