package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

public abstract class  DepositSlot {
    private int maxDim;
    private ResourceContainer depositContainer;

    public DepositSlot(ResourceType depositResourceType, int maxDim) {
        this.maxDim = maxDim;
        this.depositContainer = new ResourceContainer(depositResourceType,0);
    }

    public DepositSlot(int maxDim) {
        this.maxDim = maxDim;
        this.depositContainer = new ResourceContainer(null,0);
    }


    public abstract Boolean canAddToDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, DepositSlotMaxDimExceeded;
    public abstract Boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws DifferentResourceType, NotEnoughResources;
    public abstract Boolean addToDepositSlot(ResourceContainer inputContainer);
    public abstract Boolean removeFromDepositSlot(ResourceContainer inputContainer);


    public boolean canAdd(int input) {
        return (input+depositContainer.getQta()<=maxDim);
    }
    public boolean canRemove(int input){
        return (depositContainer.getQta() - input >= 0);
    }

    public boolean isNull(){ return this.getDepositResourceType() == null; }
    public boolean isEmpty(){
        return (this.getDepositContainer().getQta() == 0);
    }


    public boolean hasSameTypeAs(DepositSlot depositSlot){
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

    public int getResourceQta(){ return this.depositContainer.getQta(); }

    public ResourceType getDepositResourceType() {
        return depositContainer.getResourceType();
    }

    public void setDepositResourceType(ResourceType depositResourceType) {
        this.depositContainer.setResourceType(depositResourceType);
    }

}
