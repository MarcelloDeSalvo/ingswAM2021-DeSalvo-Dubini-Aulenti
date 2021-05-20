package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.cli.Color;

import java.util.ArrayList;

public class LiteDeposit {

    ArrayList<MiniDeposit> deposits;

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
        private boolean leaderType;

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

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("\nMaxDim: ").append(getMaxDim()).append("\n");

            if(container.getQty() != 0 || leaderType) {
                stringBuilder.append(container).append("\n");
                if(container.getQty() == getMaxDim())
                    stringBuilder.append(Color.ANSI_RED.escape()).append("FULL").append(Color.ANSI_RESET.escape()).append("\n");
            }
            else
                stringBuilder.append(Color.ANSI_GREEN.escape()).append("EMPTY").append(Color.ANSI_RESET.escape()).append("\n");

            return stringBuilder.toString();
        }
    }

    public void addSlot(int maxDim, ResourceType resourceType) {
        if (resourceType==null)
            deposits.add(new MiniDeposit(maxDim));
        else
            deposits.add(new MiniDeposit(maxDim, resourceType));
    }

    public void addRes(ResourceContainer resourceContainer, int id){
        deposits.get(id-1).addToDepositSlot(resourceContainer);
    }

    public void removeRes(ResourceContainer resourceContainer, int id){
        deposits.get(id-1).removeFromDepositSLot(resourceContainer);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        for (MiniDeposit depositSlot : deposits) {
            stringBuilder.append("----------------------------------\n")
                    .append(Color.ANSI_CYAN.escape()).append("ID - ").append(i).append(Color.ANSI_RESET.escape()).append("\n").append(depositSlot.toString());
            i++;
        }

        return Color.ANSI_BLUE.escape() + "DEPOSIT: " + Color.ANSI_RESET.escape() +
                "\n" +
                stringBuilder.append("----------------------------------\n").toString();
    }

}
