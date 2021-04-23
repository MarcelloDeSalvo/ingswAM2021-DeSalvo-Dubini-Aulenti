package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.player.deposit.Deposit;
import it.polimi.ingsw.model.player.production.ProductionSite;

public interface PlayerBoard_AbilityAccess {

    Deposit getDeposit();
    ProductionSite getProductionSite();
    ConversionSite getConversionSite();
    DiscountSite getDiscountSite();

}