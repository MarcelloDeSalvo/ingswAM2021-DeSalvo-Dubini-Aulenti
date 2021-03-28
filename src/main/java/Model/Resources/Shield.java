package Model.Resources;
import Model.Player.DepositSlot;

public class Shield extends Resource{
    ResourceType resourceType;

    public Shield() {
        super (ResourceType.SHIELD);
    }
    @Override
    public boolean addToDeposit (DepositSlot depositslot) {
        if (depositslot.addToDepositSlot(new ResourceContainer(this.resourceType, 1)))
            return true;
        else
            return false;
    }


}