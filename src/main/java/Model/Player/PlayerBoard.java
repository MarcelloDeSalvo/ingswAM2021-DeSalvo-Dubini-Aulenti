package Model.Player;

import Model.Player.Deposit.Deposit;
import Model.Player.Production.ProductionSite;
import Model.Resources.ResourceType;

public class PlayerBoard {

    private Deposit deposit;
    private Vault vault;
    private ProductionSite productionSite;
    private ConversionSite conversionSite;
    private DiscountSite discountSite;

    public PlayerBoard(int PyramidNum, int ProdSlotNum) {
        this.deposit = new Deposit(PyramidNum);
        this.vault = new Vault();
        this.productionSite = new ProductionSite(ProdSlotNum);
        this.conversionSite = new ConversionSite();
        this.discountSite = new DiscountSite();
    }

    /**
     * Returns the  current quantity of the requested ResourceType ( sum of deposit and vault)
     * @return
     */
    public int checkResources(ResourceType requested){
        return(deposit.checkDeposit(requested)+vault.getResourceQuantity(requested));
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public Vault getVault() {
        return vault;
    }

    public ProductionSite getProductionSite() {
        return productionSite;
    }

    public ConversionSite getConvertionSite() {
        return conversionSite;
    }

    public DiscountSite getDiscountSite() {
        return discountSite;
    }
}


