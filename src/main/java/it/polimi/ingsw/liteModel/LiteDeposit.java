package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.cli.Color;
import it.polimi.ingsw.view.gui.panels.DepositPanel;

import java.awt.*;
import java.util.ArrayList;

public class LiteDeposit {

    private final ArrayList<MiniDeposit> deposits;
    private DepositPanel depositPanel;

    public LiteDeposit() {
        this.deposits = new ArrayList<>();

        for(int i = 0; i<3; i++)
            deposits.add(new MiniDeposit(i+1));
    }

    private class MiniDeposit  {
        private boolean isFull;
        private boolean isEmpty ;
        private final int maxDim;
        private final ResourceContainer container;
        private final boolean leaderType;

        public MiniDeposit(int maxDim) {
            this.isFull = false;
            this.isEmpty = false;
            this.maxDim = maxDim;
            this.leaderType = false;
            this.container = new ResourceContainer(null,0);
        }

        public MiniDeposit(int maxDim, ResourceType resourceType) {
            this.isFull = false;
            this.isEmpty = false;
            this.maxDim = maxDim;
            this.leaderType = true;
            this.container = new ResourceContainer(resourceType,0);
            container.setResourceType(resourceType);
        }

        private void addToDepositSlot(ResourceContainer cont){
            container.setQty(container.getQty()+cont.getQty());

            if (!leaderType)
                container.setResourceType(cont.getResourceType());

            if(container.getQty()==maxDim){
                isFull = true;
            }
        }

        private void removeFromDepositSLot(ResourceContainer cont){
            container.setQty(container.getQty()-cont.getQty());

            if (container.getQty()<maxDim)
                isFull = false;
            if (container.getQty()==0)
                isEmpty = true;
        }

        public boolean isFull() {
            return isFull;
        }

        public boolean isEmpty() {
            return isEmpty;
        }

        public int getMaxDim() {
            return maxDim;
        }

        public ResourceContainer getContainer() {
            return container;
        }


        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(" MaxDim: ").append(getMaxDim()).append("\n").append("\n");

            int qty = container.getQty();

            if(qty != 0) {
                for(int i = 0; i < qty; i++)
                    stringBuilder.append(" ").append(container.getResourceType());
                if(qty == getMaxDim())
                    stringBuilder.append("\n").append("\n").append(Color.ANSI_RED.escape()).append(" FULL").append(Color.ANSI_RESET.escape());
            }
            else
                stringBuilder.append(Color.ANSI_GREEN.escape()).append(" EMPTY").append(Color.ANSI_RESET.escape());

            return stringBuilder.toString();
        }
    }


    public void addSlot(int maxDim, ResourceType resourceType, Point pos) {
        if (resourceType==null)
            deposits.add(new MiniDeposit(maxDim));
        else{
            deposits.add(new MiniDeposit(maxDim, resourceType));
            if (depositPanel!= null)
                depositPanel.addExtraSlot(pos);
        }

    }

    public void addRes(ResourceContainer resourceContainer, int id){
        deposits.get(id-1).addToDepositSlot(resourceContainer);
        if (depositPanel!=null){
            depositPanel.setImage(deposits.get(id-1).getContainer(), id);
            depositPanel.repaint();
        }

    }

    public void removeRes(ResourceContainer resourceContainer, int id){
        deposits.get(id-1).removeFromDepositSLot(resourceContainer);
        if (depositPanel!=null){
            depositPanel.setImage(deposits.get(id-1).getContainer(), id);
            depositPanel.repaint();
        }

    }


    public void setDepositPanel(DepositPanel depositPanel) {
        if (this.depositPanel != null)
            this.depositPanel.copy(depositPanel.getDepositButtons());
        this.depositPanel = depositPanel;
    }

    public ArrayList<MiniDeposit> getDeposits() {
        return deposits;
    }

    public ResourceType getType(int id){
        return deposits.get(id).container.getResourceType();
    }

    public boolean isLeaderType(int id){
        return deposits.get(id).leaderType;
    }

    public DepositPanel getDepositPanel() { return depositPanel; }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        for (MiniDeposit depositSlot : deposits) {
            stringBuilder.append("\n----------------------------------\n")
                    .append(Color.ANSI_CYAN.escape()).append(" ID - ").append(i).append(Color.ANSI_RESET.escape());

                    if(depositSlot.leaderType)
                        stringBuilder.append("\t\t\tOnly: ").append(depositSlot.getContainer().getResourceType());

                    stringBuilder.append("\n").append(depositSlot.toString());
            i++;
        }

        return "\n" + Color.ANSI_BLUE.escape() + "DEPOSIT: " + Color.ANSI_RESET.escape() +
                "\n" +
                stringBuilder.append("\n----------------------------------\n").toString();
    }

}
