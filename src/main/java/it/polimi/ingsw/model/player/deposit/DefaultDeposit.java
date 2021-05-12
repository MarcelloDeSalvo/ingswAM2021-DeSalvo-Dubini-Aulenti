package it.polimi.ingsw.model.player.deposit;

import it.polimi.ingsw.model.exceptions.DepositSlotMaxDimExceeded;
import it.polimi.ingsw.model.exceptions.DifferentResourceType;
import it.polimi.ingsw.model.exceptions.NotEnoughResources;
import it.polimi.ingsw.model.exceptions.ResourceTypeAlreadyStored;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.cli.Color;

import java.util.HashSet;


public class DefaultDeposit extends DepositSlot {

    private HashSet<ResourceType> notAvailableResourceType;

    public DefaultDeposit(int maxDim, HashSet<ResourceType> notAvailableResourceType) {
        super(maxDim);
        this.notAvailableResourceType = notAvailableResourceType;
    }


    //DEPOSIT MANAGEMENT------------------------------------------------------------------------------------------------

    /**
     * It's the function that gives the permission to add or not to the Controller
     * @return true if he can add the resources
     * @throws DifferentResourceType when there is a ResourceType mismatch
     * @throws DepositSlotMaxDimExceeded when it would add too many resources
     * @throws ResourceTypeAlreadyStored if another default deposit is already storing the same resource type
     */
    @Override
    public boolean canAddToDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored {
        int quantityThatIWantToAdd = inputContainer.getQty();

        if(isTheResourceTypeAlreadyTaken(inputContainer.getResourceType()))
            throw new ResourceTypeAlreadyStored("Another deposit is already storing the same resource type, please select another deposit or discard the resource");


        if(this.isEmpty() || inputContainer.isTheSameType(this.getDepositContainer())){
            if(canAdd(quantityThatIWantToAdd))
                return true;
            else
                throw new DepositSlotMaxDimExceeded("This deposit has reached the maximum dimension, please select another one");
        }

        throw new DifferentResourceType("This deposit is already storing a different resourceType, please select another one");
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
    public boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources {
        if(this.isEmpty())
            throw new NotEnoughResources("Not enough resources");

        if(!this.getDepositContainer().isTheSameType(inputContainer) )
            throw new DifferentResourceType("Not the same type");

        if(!this.getDepositContainer().hasEnough(inputContainer, getBufferContainer()))
            throw new NotEnoughResources("Not enough resources");

        addToBuffer(inputContainer);
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
    //------------------------------------------------------------------------------------------------------------------


    //PRIVATE METHODS---------------------------------------------------------------------------------------------------
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
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public void getHashSetData(){
        for (ResourceType rs: notAvailableResourceType) {
            System.out.println(rs.toString());
        }
    }

    public HashSet<ResourceType> getNotAvailableResourceType() {
        return notAvailableResourceType;
    }

    //------------------------------------------------------------------------------------------------------------------


    //JAVA--------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nMaxDim: ").append(getMaxDim()).append("\n");

        if(getDepositContainer().getQty() != 0) {
            stringBuilder.append(getDepositContainer()).append("\n");
            if(getDepositContainer().getQty() == getMaxDim())
               stringBuilder.append(Color.ANSI_RED.escape()).append("FULL").append(Color.ANSI_RESET.escape()).append("\n");
        }
        else
            stringBuilder.append(Color.ANSI_GREEN.escape()).append("EMPTY").append(Color.ANSI_RESET.escape()).append("\n");

        return stringBuilder.toString();
    }

    //------------------------------------------------------------------------------------------------------------------
}

