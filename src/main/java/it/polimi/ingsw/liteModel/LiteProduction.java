package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.ProductionAbility;
import it.polimi.ingsw.view.cli.Color;
import java.util.ArrayList;
import java.util.List;

public class LiteProduction {

    private final List<MiniSlot> productionSlots;
    private final List<DevelopmentCard> developmentCards;

    public LiteProduction(List<DevelopmentCard> developmentCards) {
        this.productionSlots = new ArrayList<>();
        this.developmentCards = new ArrayList<>(developmentCards);

        for(int i = 0; i < 3; i++)
            productionSlots.add(new MiniSlot());
    }

    private static class MiniSlot {
        private final ArrayList <Integer> cardIDs;
        private final ProductionAbility productionAbility;

        public MiniSlot() {
            this.cardIDs = new ArrayList<>();
            this.productionAbility = null;
        }

        public MiniSlot(ProductionAbility productionAbility) {
            this.cardIDs = null;
            this.productionAbility = productionAbility;
        }

        private void addCard(int cardID) {
            if(cardIDs == null)
                return;

            cardIDs.add(cardID);
        }

        private boolean isEmpty() {
            if(cardIDs == null) return false;

            return cardIDs.isEmpty();
        }

        public String toString(int id, List<DevelopmentCard> developmentCards) {
            StringBuilder prodSlots = new StringBuilder();

            prodSlots.append(Color.ANSI_WHITE.escape()).append("\n# DEVELOPMENT SLOT ID: ").append(id).append(" ---------------- # \n").append(Color.ANSI_RESET.escape());

            if(cardIDs != null) {
                prodSlots.append(Color.ANSI_CYAN.escape()).append("\nON TOP:").append(Color.ANSI_RESET.escape());
                if(isEmpty()) {
                    prodSlots.append("\n").append(Color.ANSI_GREEN.escape()).append(" EMPTY").append(Color.ANSI_RESET.escape()).append("\n");
                    return prodSlots.toString();
                }

                for (int cardID = cardIDs.size()-1; cardID >= 0; cardID --) {
                    prodSlots.append(developmentCards.get(cardIDs.get(cardID)).toString()).append("\n");
                }
            }
            else if (productionAbility != null) {
                prodSlots.append(productionAbility.toString()).append("\n");
            }
            return prodSlots.toString();
        }
    }

    public void addCardToSlot (int slotID, int cardID) {
        productionSlots.get(slotID-1).addCard(cardID-1);
    }

    public void addProductionSlot (ProductionAbility productionAbility) {
        productionSlots.add(new MiniSlot(productionAbility));
    }

    @Override
    public String toString() {
        StringBuilder prodSlots = new StringBuilder();

        prodSlots.append("\n").append(Color.ANSI_BLUE.escape()).append("PRODUCTION SITE:").append(Color.ANSI_RESET.escape()).append("\n");

        prodSlots.append(Color.ANSI_WHITE.escape()).append("\n# BASIC PRODUCTION ID: ").append(0).append(" ----------------- #").append(Color.ANSI_RESET.escape()).append("\n").append("\n").append(" ");

        for (int i = 0; i < 2; i++){
            if (i != 0)
                prodSlots.append(" + ");
            prodSlots.append(Color.ANSI_WHITE_BOLD_FRAMED.escape()).append(" ? ").append(Color.ANSI_RESET.escape());
        }

        prodSlots.append(Color.ANSI_WHITE.escape()).append(" --> ").append(Color.ANSI_RESET.escape());

        prodSlots.append(Color.ANSI_WHITE_BOLD_FRAMED.escape()).append(" ? ").append(Color.ANSI_RESET.escape());

        prodSlots.append("\n");

        int id = 1;
        for (MiniSlot slot: productionSlots) {
            prodSlots.append(slot.toString(id, developmentCards));
            id++;
        }

        return prodSlots.toString();
    }
}
