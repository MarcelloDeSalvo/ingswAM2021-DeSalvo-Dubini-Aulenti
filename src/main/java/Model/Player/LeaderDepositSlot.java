package Model.Player;

import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.io.IOException;

public class LeaderDepositSlot extends DepositSlot {

    public LeaderDepositSlot(int maxDim, ResourceType depositResourceType) {
        super(maxDim, depositResourceType);
    }


    @Override
    public void setDepositResourceType(ResourceType depositResourceType) {
        System.out.println("You cannot modify the resource type on a LeaderDeposit");
    }


    @Override
    public Boolean canAddtoDepositSlot(ResourceContainer inputContainer) throws IOException {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToAdd = inputContainer.getQta();

        if (!sameResType(inputContainer.getResourceType())) {
            throw new IOException("Not the same type");
        }else if (canAdd(quantityThatIwantToAdd)) {
            return true;
        }else{
            throw new ArithmeticException("Maximum dimension exceeded");
        }
    }

    @Override
    public Boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws IOException {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToRemove = inputContainer.getQta();

        if (!sameResType(inputContainer.getResourceType())) {
            throw new IOException("Not the same type");
        }else if (canRemove(quantityThatIwantToRemove)) {
            return true;
        }else {
            throw new ArithmeticException("Negative result");
        }
    }

    /**
     * adds the quantity from a resourceContainer
     * @param inputContainer
     * @return true if there were no exceptions
     * @throws ArithmeticException when the sum exceeds the max. dimension
     * @throws IOException  when there is a ResourceType mismatch
     */
    @Override
    public Boolean addToDepositSlot(ResourceContainer inputContainer) throws ArithmeticException, IOException {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToAdd = inputContainer.getQta();

        if (!sameResType(inputContainer.getResourceType())) {
            throw new IOException("Not the same type");
        }else if (canAdd(quantityThatIwantToAdd)) {
                this.getStorageArea().setQta(quantityInsideTheSlot + quantityThatIwantToAdd);
                return true;
        }else{
            throw new ArithmeticException("Maximum dimension exceeded");
        }
    }


    /**
     * removes the quantity from a resourceContainer
     * @param inputContainer
     * @return true if there were no exceptions
     * @throws ArithmeticException when the subtraction returns a negative value
     * @throws IOException when there is a ResourceType mismatch
     */
    @Override
    public Boolean removeFromDepositSlot(ResourceContainer inputContainer) throws ArithmeticException, IOException{
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToRemove = inputContainer.getQta();

        if (!sameResType(inputContainer.getResourceType())) {
            throw new IOException("Not the same type");
        }else if (canRemove(quantityThatIwantToRemove)) {
            this.getStorageArea().setQta(quantityInsideTheSlot + quantityThatIwantToRemove);
            return true;
        }else {
            throw new ArithmeticException("Negative result");
        }
    }
}
