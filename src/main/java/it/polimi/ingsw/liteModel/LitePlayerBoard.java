package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;

public class LitePlayerBoard {

    private LiteHand liteHand;
    private LiteDeposit liteDeposit;
    private LiteVault liteVault;
    private LiteProduction liteProduction;

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


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("\n");
        String divider="\n>< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< >< ><>< >< >< >< >< >< >< >< >< >< >< >< >< >< ><\n";
        s.append(liteHand.toString()).append(divider).
                append(liteProduction.toString()).append(divider).
                append(liteDeposit.toString()).append(divider).
                append(liteVault.toString()).append("\n");
        return s.toString();
    }

}
