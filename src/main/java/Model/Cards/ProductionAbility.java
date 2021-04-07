package Model.Cards;

import Model.Player.PlayerBoard;
import Model.Player.Production.LeaderCardProduction;
import Model.Player.Production.ProductionSlot;
import Model.Resources.ResourceContainer;

import java.util.ArrayList;

public class ProductionAbility implements Ability {
    private ArrayList<ResourceContainer> input;
    private int questionMarkOnInput;
    private ArrayList<ResourceContainer> output;
    private int questionMarkOnOutput;

    public ProductionAbility(ArrayList<ResourceContainer> input, ArrayList<ResourceContainer> output, int QMI, int QMO) {
        this.input = new ArrayList<>(input);
        this.output = new ArrayList<>(output);
        this.questionMarkOnInput = QMI;
        this.questionMarkOnOutput = QMO;
    }

    /**
     * Adds ana available production (creating a LeaderProductionSlot) to the current player
     * @param playerBoard is the current Player's playerBoard
     * @return
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

    public void setInput(ArrayList<ResourceContainer> input) {
        this.input = input;
    }

    public ArrayList<ResourceContainer> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<ResourceContainer> output) {
        this.output = output;
    }
}
