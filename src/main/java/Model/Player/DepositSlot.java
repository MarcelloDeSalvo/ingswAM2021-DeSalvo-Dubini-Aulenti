package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.io.IOException;

public abstract class DepositSlot {
    private int maxDim;
    private ResourceContainer storageArea;
    private ResourceType depositResourceType;

    public DepositSlot(int maxDim, ResourceType depositResourceType) {
        this.maxDim = maxDim;
        this.depositResourceType = depositResourceType;
    }

    public DepositSlot(int maxDim) {
        this.maxDim = maxDim;
        this.depositResourceType = null;
    }



    public abstract Boolean canAddtoDepositSlot(ResourceContainer inputContainer) throws IOException;
    public abstract Boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws IOException;
    public abstract Boolean addToDepositSlot(ResourceContainer inputContainer) throws IOException;
    public abstract Boolean removeFromDepositSlot(ResourceContainer inputContainer) throws IOException;
    //public abstract Boolean switchFromAnother();



    public boolean canAdd(int input) {
        return (input+storageArea.getQta()>maxDim);
    }

    public boolean canRemove(int input){
        return (storageArea.getQta() - input < 0);
    }

    public boolean sameResType(ResourceType res){
        return (res.equals(depositResourceType));
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
        return depositResourceType;
    }

    public void setDepositResourceType(ResourceType depositResourceType) {
        this.depositResourceType = depositResourceType;
    }
}
