package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.view.cli.Color;

import java.awt.*;
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
        StringBuilder s = new StringBuilder("\n");
        String divider="\n>< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< ><>< >< >< >< >< >< >< >< >< >< >< >< >< >< ><\n";

        s.append(player).append("'s ").append(Color.ANSI_BLUE.escape()).append("BOARD: \n").append(Color.ANSI_RESET.escape()).append(divider);

        s.append(liteHand.toString(player)).append(divider).
                append(liteProduction.toString()).append(divider).
                append(liteDeposit.toString()).append(divider).
                append(liteVault.toString()).append("\n");

        return s.toString();
    }

}
