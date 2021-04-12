package Model.Player.Deposit;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Exceptions.ResourceTypeAlreadyStored;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.HashSet;


public class DefaultDeposit extends DepositSlot {

    private static HashSet<ResourceType> notAvailableResourceType = new HashSet<>();

    public DefaultDeposit(int maxDim) {
        super(maxDim);
    }


    @Override
    public boolean canAddToDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored {
        int quantityThatIWantToAdd = inputContainer.getQty();

        if(isTheResourceTypeAlreadyTaken(inputContainer.getResourceType()))
            throw new ResourceTypeAlreadyStored("Another deposit is already storing the same resource type");


        if(this.isEmpty() || inputContainer.isTheSameType(this.getDepositContainer())){
            if(canAdd(quantityThatIWantToAdd))
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

        addToBuffer(inputContainer);
        return true;
    }


    @Override
    public boolean addToDepositSlot(ResourceContainer inputContainer)  {
        int quantityThatIWantToAdd = inputContainer.getQty();
        ResourceType inputType = inputContainer.getResourceType();

        this.getDepositContainer().addQty(quantityThatIWantToAdd);
        this.getDepositContainer().setResourceType(inputType);
        getNotAvailableResourceType().add(inputType);
        return true;
    }


    @Override
    public boolean removeFromDepositSlot(ResourceContainer inputContainer){
        int quantityThatIWantToRemove = inputContainer.getQty();

        this.getDepositContainer().addQty(-quantityThatIWantToRemove);
        remakeTypeAvailableIfEmpty();

        return true;
    }

    /**
     * gives the controller the permission to move a desired quantity from one deposit to another
     * @param destination is the deposit where the user wants the resources to be moved
     * @return true if the Default Deposit can transfer his resources with another generic deposit
     * @throws NotEnoughResources if the user wants to move a quantity that's greater than the selected deposit's max dimension
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     */
    @Override
    public boolean canTransferTo(DepositSlot destination, int quantityThatIWantToTransfer) throws DifferentResourceType, DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored, NotEnoughResources{
        if(!this.canRemove(quantityThatIWantToTransfer))
            throw  new NotEnoughResources("Not enough resources");

        ResourceContainer send = new ResourceContainer(this.getDepositResourceType(), quantityThatIWantToTransfer);
        return destination.canAddToDepositSlot(send);
    }

    @Override
    public boolean transferTo(DepositSlot destination, int quantityThatIWantToSwitch) {
        remakeTypeAvailableIfEmpty();

        super.transferTo(destination, quantityThatIWantToSwitch);
        return true;
    }


    /**
     * Used when the Deposit's type allows the storage of different ResourceType
     * Gives the controller the permission to switch a desired quantity from one deposit to another
     * @param destination is the deposit that will switch resources with the selected one
     * @return true if the Default Deposit can switch his resources with another generic deposit
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     */
    @Override
    public boolean canSwitchWith(DepositSlot destination) throws DepositSlotMaxDimExceeded{
        int allResources = this.getResourceQty();

        if(allResources> destination.getMaxDim())
            throw new DepositSlotMaxDimExceeded("They don't have enough available space");

        //if(isTheResourceTypeAlreadyTaken(destination.getDepositResourceType()))   Pensare a come farlo bene tra due default
            //throw new ResourceTypeAlreadyStored("Another deposit is already storing the same resource type");


        return true;
    }

    public void clearSet(){
        notAvailableResourceType.clear();
    }



    //getter and setter
    public void getHashSetData(){
        for (ResourceType rs: notAvailableResourceType) {
            System.out.println(rs.toString());
        }
    }

    public static HashSet<ResourceType> getNotAvailableResourceType() {
        return notAvailableResourceType;
    }

    public static void setNotAvailableResourceType(HashSet<ResourceType> notAvailableResourceType) {
        DefaultDeposit.notAvailableResourceType = notAvailableResourceType;
    }


    //private methods
    /**
     * Checks if the input ResourceType is already stored inside some other Default Deposit
     * @return true if it is
     */
    private boolean isTheResourceTypeAlreadyTaken(ResourceType inputResType){

        if(inputResType == this.getDepositResourceType())
            return false;

        return getNotAvailableResourceType().contains(inputResType);

        //getHashSetData(); // -------------> Da rimuovere, solo per testing
    }


    /**
     * Removes the deposit's ResourceType from the notAvailable Set
     * Called when the deposit's quantity reaches zero
     */
    private void remakeTypeAvailableIfEmpty(){
        if(this.isEmpty())
            getNotAvailableResourceType().remove(this.getDepositResourceType());
        //getHashSetData(); // -------------> Da rimuovere, solo per testing

    }

}
