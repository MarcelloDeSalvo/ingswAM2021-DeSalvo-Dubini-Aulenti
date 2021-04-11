package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Player.Production.LeaderCardProduction;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public class ProductionAbility implements Ability {
    private final ArrayList<ResourceContainer> input;
    private final int questionMarkOnInput;
    private final ArrayList<ResourceContainer> output;
    private final int questionMarkOnOutput;

    public ProductionAbility(ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output, int QMI, int QMO) {
        this.input = new ArrayList<>(input);
        this.output = new ArrayList<>(output);
        this.questionMarkOnInput = QMI;
        this.questionMarkOnOutput = QMO;
    }

    /**
     * Adds ana available production (creating a LeaderProductionSlot) to the current player
     * @param playerBoard is the current Player's playerBoard
     */
    @Override
    public boolean useAbility(PlayerBoard playerBoard) {
        LeaderCardProduction leaderCardProduction = new LeaderCardProduction(input, output, questionMarkOnInput, questionMarkOnOutput);
        playerBoard.getProductionSite().addProductionSlot(leaderCardProduction);
        return true;
    }

    public ArrayList<ResourceContainer> getInput() {
        return input;
    }
    public ArrayList<ResourceContainer> getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return "ProductionAbility{" +
                "input=" + input +
                ", questionMarkOnInput=" + questionMarkOnInput +
                ", output=" + output +
                ", questionMarkOnOutput=" + questionMarkOnOutput +
                '}';
    }
}
