package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.player.PlayerBoard_AbilityAccess;
import it.polimi.ingsw.model.player.production.LeaderCardProduction;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.view.cli.Color;

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
     * Adds an available production (creating a LeaderProductionSlot) to the current player
     * @param playerBoard is the current Player's playerBoard
     */
    @Override
    public boolean useAbility(PlayerBoard_AbilityAccess playerBoard) {
        LeaderCardProduction leaderCardProduction = new LeaderCardProduction(input, output, questionMarkOnInput, questionMarkOnOutput);
        playerBoard.getProductionSite().addProductionSlot(leaderCardProduction);
        return true;
    }


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public ArrayList<ResourceContainer> getInput() {
        return input;
    }
    public ArrayList<ResourceContainer> getOutput() {
        return output;
    }
    //------------------------------------------------------------------------------------------------------------------


    //JAVA--------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nProduction Ability: ").append(input);

        if(questionMarkOnInput != 0)
            stringBuilder.append(" + ").append(questionMarkOnInput).append(Color.ANSI_WHITE_BOLD_FRAMED.escape()).append(" ? ").append(Color.ANSI_RESET.escape());

        stringBuilder.append(Color.WHITE_BOLD_BRIGHT.escape()).append(" --> ").append(Color.ANSI_RESET.escape()).append(output);

        if(questionMarkOnOutput != 0)
            stringBuilder.append(" + ").append(questionMarkOnOutput).append(" ").append(Color.ANSI_WHITE_BOLD_FRAMED.escape()).append(" ? ").append(Color.ANSI_RESET.escape());

        return stringBuilder.toString();
    }
    //------------------------------------------------------------------------------------------------------------------

}
