package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

public abstract class  DepositSlot {
    private int maxDim;
    private ResourceContainer storageArea;

    public DepositSlot(ResourceType depositResourceType, int maxDim) {
        this.maxDim = maxDim;
        this.storageArea = new ResourceContainer(depositResourceType,0);
    }

    public DepositSlot(int maxDim) {
        this.maxDim = maxDim;
        this.storageArea = new ResourceContainer(null,0);
    }


    public abstract Boolean canAddtoDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, DepositSlotMaxDimExceeded;
    public abstract Boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources;
    public abstract Boolean addToDepositSlot(ResourceContainer inputContainer);
    public abstract Boolean removeFromDepositSlot(ResourceContainer inputContainer);
    //public abstract Boolean switchFromAnother();



    public boolean canAdd(int input) {
        return (input+storageArea.getQta()<=maxDim);
    }
    public boolean canRemove(int input){
        return (storageArea.getQta() - input < 0);
    }

    public boolean isNullAndEmpty(){
        return (this.getDepositResourceType() == null && this.getStorageArea().getQta() == 0);
    }

    public boolean isEmpty(){
        return (this.getStorageArea().getQta() == 0);
    }


    //Getter and Setter
    public int getMaxDim() {
        return maxDim;
    }

    public void setMaxDim(int maxDim) {
        this.maxDim = maxDim;
    }

    public ResourceContainer getStorageArea() {
        return storageArea;
    }

    public void setStorageArea(ResourceContainer storageArea) {
        this.storageArea = storageArea;
    }

    public ResourceType getDepositResourceType() {
        return storageArea.getResourceType();
    }

    public void setDepositResourceType(ResourceType depositResourceType) {
        this.storageArea.setResourceType(depositResourceType);
    }

}
