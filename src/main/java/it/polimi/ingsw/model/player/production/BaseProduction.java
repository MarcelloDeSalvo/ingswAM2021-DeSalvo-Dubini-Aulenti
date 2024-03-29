package it.polimi.ingsw.model.player.production;

import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.ProductionAbility;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.cli.Color;

import java.util.ArrayList;

public class BaseProduction implements ProductionSlot {

    private final ArrayList<ResourceContainer> input;
    private final ArrayList<ResourceContainer> output;
    private final int QMI;
    private final int QMO;

    private int QMI_buff = 0;
    private int QMO_buff = 0;


    public BaseProduction() {
        this.input = new ArrayList<>();
        this.output = new ArrayList<>();
        this.QMI = 2;
        this.QMO = 1;
    }

    public BaseProduction(int defaultQtyIn , int defaultQtyOut) {
        this.input = new ArrayList<>();
        this.output = new ArrayList<>();
        this.QMI = defaultQtyIn;
        this.QMO = defaultQtyOut;
    }


    //SLOT MANAGEMENT---------------------------------------------------------------------------------------------------
    @Override
    public boolean hasQuestionMarks() {
        return true;
    }

    @Override
    public boolean hasStillQuestionMarks() {
        return QMI_buff < QMI || QMO_buff < QMO;
    }

    @Override
    public boolean fillQuestionMarkInput(ResourceType resourceType) throws NullPointerException,IllegalArgumentException{
        if(QMI_buff < QMI && resourceType != null && input.add(new ResourceContainer(resourceType, 1))) {
            QMI_buff++;
            return true;
        }

        return false;
    }

    @Override
    public boolean fillQuestionMarkOutput(ResourceType resourceType) throws NullPointerException,IllegalArgumentException{
        if(QMO_buff < QMO && resourceType != null && output.add(new ResourceContainer(resourceType, 1))){
            QMO_buff++;
            return true;
        }

        return false;
    }

    @Override
    public boolean clearCurrentBuffer() {
        input.clear();
        output.clear();

        QMI_buff = 0;
        QMO_buff = 0;

        return true;
    }

    @Override
    public boolean insertOnTop(DevelopmentCard newDevelopmentCard) {
        return false;
    }

    @Override
    public boolean canInsertOnTop(DevelopmentCard newDevelopmentCard) {
        return false;
    }

    @Override
    public int countCardsWith(int level, Colour c) {
        return 0;
    }
    //------------------------------------------------------------------------------------------------------------------


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    @Override
    public ArrayList<ResourceContainer> getProductionInput() {
        return input;
    }

    @Override
    public ArrayList<ResourceContainer> getProductionOutput() {
        return output;
    }

    public int getQMI() {
        return QMI;
    }

    public int getQMO() {
        return QMO;
    }

    @Override
    public int getVictoryPoints() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ProductionAbility getProductionAbility() {
        return null;
    }
    //------------------------------------------------------------------------------------------------------------------

    //TO-STRING---------------------------------------------------------------------------------------------------------
    @Override
    public String toString(int id) {
        StringBuilder prodSlots = new StringBuilder();
        prodSlots.append(Color.ANSI_WHITE.escape()).append("# BASIC PRODUCTION ID: ").append(id).append(" ----------------- #").append(Color.ANSI_RESET.escape()).append("\n").append("\n");

        for (int i=0; i<QMI; i++){
            if (i!=0)
                prodSlots.append(" + ");
            prodSlots.append(Color.ANSI_WHITE_BOLD_FRAMED.escape()).append(" ? ").append(Color.ANSI_RESET.escape());
        }

        prodSlots.append(Color.ANSI_WHITE.escape()).append(" --> ").append(Color.ANSI_RESET.escape());

        for (int i=0; i<QMO; i++){
            if (i!=0)
                prodSlots.append(" + ");
            prodSlots.append(Color.ANSI_WHITE_BOLD_FRAMED.escape()).append(" ? ").append(Color.ANSI_RESET.escape());
        }

        prodSlots.append("\n");
        return prodSlots.toString();
    }
    //------------------------------------------------------------------------------------------------------------------
}

