package Model.Player;

import Model.Player.Deposit.Deposit;
import Model.Player.Production.ProductionSite;

public class PlayerBoard {

    private Deposit deposit;
    private Vault vault;
    private ProductionSite productionSite;
    private ConvertionSite convertionSite;
    private DiscountSite discountSite;

    public PlayerBoard(int PyramidNum, int ProdSlotNum) {
        this.deposit = new Deposit(PyramidNum);
        this.vault = new Vault();
        this.productionSite = new ProductionSite(ProdSlotNum);
        this.convertionSite = new ConvertionSite();
        this.discountSite = new DiscountSite();
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

    public ConvertionSite getConvertionSite() {
        return convertionSite;
    }

    public DiscountSite getDiscountSite() {
        return discountSite;
    }
}


