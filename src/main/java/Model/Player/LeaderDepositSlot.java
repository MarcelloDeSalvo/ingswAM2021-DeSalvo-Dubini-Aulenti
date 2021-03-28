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

    /**
     * It's the function that gives the permission to add or not to the Controller
     * @param inputContainer
     * @return true if you can add the Resources
     * @throws IOException when there is a ResourceType mismatch
     * @throws ArithmeticException when the maximum dimension is exceeded
     */
    @Override
    public Boolean canAddtoDepositSlot(ResourceContainer inputContainer) throws IOException, ArithmeticException{
        int quantityThatIwantToAdd = inputContainer.getQta();

        if (!sameResType(inputContainer.getResourceType())) {
            throw new IOException("Not the same type");
        }else if (canAdd(quantityThatIwantToAdd)) {
            return true;
        }else{
            throw new ArithmeticException("Maximum dimension exceeded");
        }
    }

    /**
     * It's the function that gives the permission to remove or not to the Controller
     * @param inputContainer
     * @return true if you can add the Resources
     * @throws IOException when there is a ResourceType mismatch
     * @throws ArithmeticException when there are not enough resources
     */
    @Override
    public Boolean canRemoveFromDepositSlot(ResourceContainer inputContainer) throws IOException, ArithmeticException {
        int quantityThatIwantToRemove = inputContainer.getQta();

        if (!sameResType(inputContainer.getResourceType())) {
            throw new IOException("Not the same type");
        }else if (canRemove(quantityThatIwantToRemove)) {
            return true;
        }else {
            throw new ArithmeticException("Not enough resources");
        }
    }

    /**
     * adds the quantity from a resourceContainer in any case.
     * It needs to be called after CanAddToDepositSlot if the user wants to follow the rules
     * @param inputContainer
     * @return true if there were no exceptions
     */
    @Override
    public Boolean addToDepositSlot(ResourceContainer inputContainer) {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToAdd = inputContainer.getQta();

        this.getStorageArea().setQta(quantityInsideTheSlot + quantityThatIwantToAdd);
        return true;
    }


    /**
     * removes the quantity from a resourceContainer
     * It needs to be called after CanRemoveFromDepositSlot if the user wants to follow the rules
     * @param inputContainer
     * @return true if there were no exceptions
     */
    @Override
    public Boolean removeFromDepositSlot(ResourceContainer inputContainer) {
        int quantityInsideTheSlot = this.getStorageArea().getQta();
        int quantityThatIwantToRemove = inputContainer.getQta();

        this.getStorageArea().setQta(quantityInsideTheSlot + quantityThatIwantToRemove);
        return true;

    }

}
