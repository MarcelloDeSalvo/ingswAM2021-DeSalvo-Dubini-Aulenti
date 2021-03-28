package Model.Player;

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


}


