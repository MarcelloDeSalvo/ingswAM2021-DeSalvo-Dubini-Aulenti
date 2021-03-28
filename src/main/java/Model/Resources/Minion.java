package Model.Resources;
import Model.Player.DepositSlot;

public class Minion extends Resource{
    ResourceType resourceType;

    public Minion() {
        super (ResourceType.MINION);
    }
    @Override
    public boolean addToDeposit (DepositSlot depositslot) {
        if (depositslot.addToDepositSlot(new ResourceContainer(this.resourceType, 1)))
            return true;
        else
            return false;
    }

}