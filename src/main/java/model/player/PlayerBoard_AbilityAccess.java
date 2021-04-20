package model.player;

import model.player.deposit.Deposit;
import model.player.production.ProductionSite;

public interface PlayerBoard_AbilityAccess {

    Deposit getDeposit();
    ProductionSite getProductionSite();
    ConversionSite getConversionSite();
    DiscountSite getDiscountSite();

}