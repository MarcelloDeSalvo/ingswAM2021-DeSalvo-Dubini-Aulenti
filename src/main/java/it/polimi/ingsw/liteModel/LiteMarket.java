package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.view.cli.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiteMarket {
    private final int columns=4;
    private final int rows=3;

    private final ResourceContainer[][] market= new ResourceContainer[columns][rows];
    private ResourceContainer vacant;

    public LiteMarket(List<ResourceContainer> marblesMarket){
        for(int i=0; i<columns;i++){
            for(int j=0;j<rows;j++){
                this.market[i][j]=marblesMarket.get(i*rows+j);
            }
        }
        this.vacant=marblesMarket.get(rows*columns);
    }

    public void liteMarketUpdate(String rowOrColumn, int selected){

        ResourceContainer vacantCopy=new ResourceContainer(vacant.getResourceType(),1);

        if(rowOrColumn.equalsIgnoreCase("ROW")){
            vacant=market[0][selected-1];
            for(int i=0;i<columns-1;i++){
                market[i][selected-1]=market[i+1][selected-1];
            }
            market[columns-1][selected-1]=vacantCopy;
        }
        else if(rowOrColumn.equalsIgnoreCase("COLUMN")){
            vacant=market[selected-1][0];
            System.arraycopy(market[selected - 1], 1, market[selected - 1], 0, rows - 1);
            market[selected-1][rows-1]=vacantCopy;
        }
    }


    public List<ResourceContainer> getMarketArray(){
        ArrayList<ResourceContainer> myMarket=new ArrayList<>();
        for(int i=0; i<columns;i++){
            myMarket.addAll(Arrays.asList(market[i]).subList(0, rows));
        }
        return myMarket;
    }

    public List<ResourceContainer> getGuiMarketArray(){
        ArrayList<ResourceContainer> myGuiMarket=new ArrayList<>();
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                myGuiMarket.add(market[j][i]);
            }
        }
        return myGuiMarket;
    }

    public ResourceContainer getVacant() { return vacant; }

    @Override
    public String toString() {
        StringBuilder liteMarketToString = new StringBuilder();
        liteMarketToString.append(Color.ANSI_CYAN.escape()).append("\nMARKET:").append(Color.ANSI_RESET.escape()).append("\n");

        for(int i=-1; i<rows+1; i++) {
            for (int j = 0; j < columns+1; j++) {
                if(i == -1&& j!=columns)
                    liteMarketToString.append("\t").append("\t").append("\t").append("  ").append(j+1).append("\t");
                else if(i == rows){
                    if(j == 0 )
                        liteMarketToString.append(" ").append("\t").append(" ").append("\t").append("\t");
                    else
                    liteMarketToString.append("  ↑ ").append("\t").append("\t").append(" ").append("\t").append("\t");
                }
                else{
                    if(j == 0 ) {
                        liteMarketToString.append(i + 1).append("\t").append("|").append("\t").append("\t");
                    }
                    if(j== columns) {
                        if (i != -1)
                            liteMarketToString.append("\u2190");
                    }
                    else
                    liteMarketToString.append(market[j][i].getResourceType()).append("\t").append("\t").append("|").append("\t").append("\t");
                }
            }
            liteMarketToString.append("\n");
        }
        liteMarketToString.append("\nThe vacant is: ").append(vacant.getResourceType()).append("\n");

        return liteMarketToString.toString();
    }

}
