package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.view.cli.Color;

import java.util.ArrayList;

public class LitePlayerBoard {

    private LiteHand liteHand;
    private final LiteDeposit liteDeposit;
    private final LiteVault liteVault;
    private final LiteProduction liteProduction;

    public LitePlayerBoard(ArrayList<LeaderCard> leaderCards, ArrayList<DevelopmentCard> developmentCards) {
        this.liteHand = new LiteHand(leaderCards);
        this.liteDeposit = new LiteDeposit();
        this.liteVault = new LiteVault();
        this.liteProduction = new LiteProduction(developmentCards);
    }

    public LiteHand getLiteHand() {
        return liteHand;
    }

    public LiteDeposit getLiteDeposit() {
        return liteDeposit;
    }

    public LiteVault getLiteVault() {
        return liteVault;
    }

    public LiteProduction getLiteProduction() {
        return liteProduction;
    }

    public void setLiteHand(LiteHand liteHand) {
        this.liteHand = liteHand;
    }


    public String toString(String player) {
        String divider="\n>< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< ><>< >< >< >< >< >< >< >< >< >< >< >< >< >< ><\n";

        return "\n" + player + "'s " + Color.ANSI_BLUE.escape() + "BOARD: \n" + Color.ANSI_RESET.escape() + divider +
                liteHand.toString(player) + divider +
                liteProduction.toString() + divider +
                liteDeposit.toString() + divider +
                liteVault.toString() + "\n";
    }

}
