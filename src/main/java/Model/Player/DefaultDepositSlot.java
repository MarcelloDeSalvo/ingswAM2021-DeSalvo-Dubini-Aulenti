package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.io.IOException;

public class DefaultDepositSlot extends DepositSlot {

    public DefaultDepositSlot(int maxDim, ResourceType depositResourceType) {
        super(maxDim,  depositResourceType);
    }

    @Override
    public Boolean canAddtoDepositSlot(ResourceContainer inputContainer) throws IOException {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToAdd = inputContainer.getQta();

        if(!sameResType(inputContainer.getResourceType())) {
            if(quantityInsideTheSlot == 0) {
                return true;
            }else {
                throw new IOException("Not the same type");
            }
        }else {
            if(canAdd(quantityThatIwantToAdd)){
                return true;
            }else{
                throw new IOException("Not enough space");
            }
        }
    }

    @Override
    public Boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws IOException {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToRemove = inputContainer.getQta();

        if(!sameResType(inputContainer.getResourceType())) {
            throw new IOException("Not the same type");

        }else {
            if(canRemove(quantityThatIwantToRemove)){
                return true;
            }else{
                throw new IOException("Not enough resources");
            }
        }
    }

    @Override
    public Boolean addToDepositSlot(ResourceContainer inputContainer) throws IOException {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToAdd = inputContainer.getQta();

        if(!sameResType(inputContainer.getResourceType())) {
            if(this.getStorageArea().getQta() == 0) {
                this.getStorageArea().setQta(quantityThatIwantToAdd);
                this.getStorageArea().setResourceType(inputContainer.getResourceType());
                return true;
            }else {
                throw new IOException("Not the same type");
            }
        }else {
            if(canAdd(quantityThatIwantToAdd)){
                this.getStorageArea().setQta(quantityThatIwantToAdd+quantityInsideTheSlot);
                return true;
            }else{
                throw new IOException("Not enough space");
            }
        }
    }

    @Override
    public Boolean removeFromDepositSlot(ResourceContainer inputContainer) throws IOException {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToRemove = inputContainer.getQta();

        if(!sameResType(inputContainer.getResourceType())) {
                throw new IOException("Not the same type");

        }else {
            if(canRemove(quantityThatIwantToRemove)){
                this.getStorageArea().setQta(quantityInsideTheSlot- quantityThatIwantToRemove);
                return true;
            }else{
                throw new IOException("Not enough resources");
            }
        }
    }
}

