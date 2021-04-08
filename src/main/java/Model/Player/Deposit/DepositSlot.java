package Model.Player.Deposit;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Exceptions.ResourceTypeAlreadyStored;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

public abstract class  DepositSlot {
    private int maxDim;
    private ResourceContainer depositContainer;
    private ResourceContainer bufferContainer;

    public DepositSlot(ResourceType depositResourceType, int maxDim) {
        this.maxDim = maxDim;
        this.depositContainer = new ResourceContainer(depositResourceType,0);
        this.bufferContainer = new ResourceContainer(null,0);

    }

    public DepositSlot(int maxDim) {
        this.maxDim = maxDim;
        this.depositContainer = new ResourceContainer(null,0);
        this.bufferContainer = new ResourceContainer(null,0);
    }

    /**
     * It's the function that gives the permission to add or not to the Controller
     * @param inputContainer
     * @return true if he can add the resources
     * @throws DifferentResourceType when there is a ResourceType mismatch
     * @throws DepositSlotMaxDimExceeded when it would add too many resources
     */
    public abstract boolean canAddToDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored;

    /**
     * adds the quantity from a resourceContainer in any case.
     * It needs to be called after canAddToDepositSlot if the user wants to follow the rules
     * @param inputContainer
     * @return true if there were no errors
     */
    public abstract boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources;

    /**
     * It's the function that gives the permission to remove or not to the Controller
     * @param inputContainer
     * @return true if he can
     */
    public abstract boolean addToDepositSlot(ResourceContainer inputContainer);

    /**
     * removes the quantity from a resourceContainer
     * It needs to be called after CanRemoveFromDepositSlot if the user wants to follow the rules
     * @param inputContainer
     * @return true if there were no exceptions
     */
    public abstract boolean removeFromDepositSlot(ResourceContainer inputContainer);

    /**
     * gives the controller the permission to move a desired quantity from one deposit to another
     * @param destination is the deposit where the user wants the resources to be moved
     * @param quantityThatIwantToSwitch
     * @return true if the selected deposit can transfer his resources to another generic deposit
     * @throws NotEnoughResources if the user wants to move a quantity that's greater than the selected deposit's max dimension
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     * @throws DifferentResourceType if the selected slot has some Resourcetype restrictions
     */
    public abstract boolean canTransferTo(DepositSlot destination, int quantityThatIwantToSwitch) throws NotEnoughResources, DifferentResourceType, DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored;

    /**
     * Used when the Deposit's type allows the storage of different ResourceType
     * Gives the controller the permission to switch a desired quantity from one deposit to another
     * @param destination is the deposit that will switch resources with the selected one
     * @return true if the Deposit's type can switch his resources with another generic deposit
     * @throws ResourceTypeAlreadyStored if the user wants to move already stored ResourceTypes
     * @throws DepositSlotMaxDimExceeded if in the destination deposit there's not enough space to insert the transferred resources
     */
    public abstract boolean canSwitchWith(DepositSlot destination) throws  DepositSlotMaxDimExceeded, ResourceTypeAlreadyStored;

    /**
     * Transfer a desired quantity from one deposit to another one
     * Sets the target deposit's ResourceType equal as this ResourceType
     * @param destination is the deposit where the user wants the resources to be placed
     * @param quantityThatIwantToTransfer
     * @return true
     */
    public boolean transferTo(DepositSlot destination, int quantityThatIwantToTransfer){
        ResourceContainer send = new ResourceContainer(this.getDepositResourceType(), quantityThatIwantToTransfer);
        this.removeFromDepositSlot(send);
        destination.addToDepositSlot(send);

        destination.setDepositResourceType(this.getDepositResourceType());
        return true;
    }

    /**
     * Switches two deposit's resources if they can store different ResourceType
     * @param destination it's the deposit that will switch the resources with This
     */
    public boolean switchTo(DepositSlot destination){  //works only between defaults deposits
        int myQty = this.getResourceQty();
        int hisQty = destination.getResourceQty();
        ResourceType myResType = this.getDepositResourceType();
        ResourceType hisResType = destination.getDepositResourceType();

        this.getDepositContainer().addQty(-myQty);
        destination.getDepositContainer().addQty(myQty);
        destination.setDepositResourceType(myResType);

        destination.getDepositContainer().addQty(-hisQty);
        this.getDepositContainer().addQty(hisQty);
        this.setDepositResourceType(hisResType);
        return true;
    }

    /**
     * Adds an input to the buffer that handles one transaction
     * It is called when the user decides to buy one card or to produce:
     * it adds the price or the input requirement for the production to the buffer after the canRemove(price/productionInput)
     * @param inputContainer
     * @return
     */
    public boolean addToBuffer(ResourceContainer inputContainer){
        int quantityThatIwantToAdd = inputContainer.getQty();
        ResourceType inputType = inputContainer.getResourceType();

        bufferContainer.addQty(quantityThatIwantToAdd);
        bufferContainer.setResourceType(inputType);
        return true;
    }

    /**
     * Subtracts the quantity value of the buffer from the original container
     * @return
     */
    public boolean removeTheBuffer(){
        this.removeFromDepositSlot(bufferContainer);
        return true;
    }

    /**
     * Clears all the buffers
     * @return
     */
    public boolean clearCurrentBuffer(){
        bufferContainer.setResourceType(null);
        bufferContainer.setQty(0);
        return true;
    }


    public boolean canAdd(int input) {
        return (input+depositContainer.getQty()<=maxDim);
    }
    public boolean canRemove(int input){
        return (depositContainer.getQty() - input >= 0);
    }

    public boolean isNull(){ return this.getDepositResourceType() == null; }
    public boolean isEmpty(){
        return (this.getDepositContainer().getQty() == 0);
    }


    public boolean isTheSameType(DepositSlot depositSlot){
        return this.getDepositResourceType() == depositSlot.getDepositResourceType();
    }

    //Getter and Setter
    public int getMaxDim() {
        return maxDim;
    }

    public void setMaxDim(int maxDim) {
        this.maxDim = maxDim;
    }

    public ResourceContainer getDepositContainer() {
        return depositContainer;
    }

    public void setDepositContainer(ResourceContainer depositContainer) {
        this.depositContainer = depositContainer;
    }

    public int getResourceQty(){ return this.depositContainer.getQty(); }

    public ResourceType getDepositResourceType() {
        return depositContainer.getResourceType();
    }

    public void setDepositResourceType(ResourceType depositResourceType) {
        this.depositContainer.setResourceType(depositResourceType);
    }

    public ResourceContainer getBufferContainer() {
        return bufferContainer;
    }

    public void setBufferContainer(ResourceContainer bufferContainer) {
        this.bufferContainer = bufferContainer;
    }
}
