package Model.Player;

import Model.Player.Deposit.Deposit;
import Model.Player.Production.ProductionSite;

public interface PlayerBoard_AbilityAccess {

    Deposit getDeposit();
    ProductionSite getProductionSite();
    ConversionSite getConversionSite();
    DiscountSite getDiscountSite();

}