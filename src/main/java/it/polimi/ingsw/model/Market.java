package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.InvalidColumnNumber;
import it.polimi.ingsw.model.exceptions.InvalidRowNumber;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.view.cli.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Market {
    private final int columns=4;
    private final int rows=3;

    private final ResourceContainer[][] market= new ResourceContainer[columns][rows];
    private ResourceContainer vacant;

    public Market(ArrayList<ResourceContainer> marblesMarket){
        Collections.shuffle(marblesMarket);
        for(int i=0; i<columns;i++){
            for(int j=0;j<rows;j++){
                this.market[i][j]=marblesMarket.get(i*rows+j);
            }
        }

        this.vacant=marblesMarket.get(rows*columns);
    }


    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    /**
     * This method returns a chosen row from the market grid.
     * @param selectedRow is the row you want to obtain
     * @return an array list containing the resources of the row
     * @throws InvalidRowNumber if the chosen row isn't valid (e.g. asking for a negative row)
     */
    public ArrayList<ResourceContainer> getRow(int selectedRow) throws InvalidRowNumber {
        if(selectedRow<=0 || selectedRow>rows)
            throw new InvalidRowNumber ("Selected row isn't valid");

        ArrayList<ResourceContainer> outputRow= new ArrayList<>();
        for(int i=0; i<columns; i++){
            outputRow.add(new ResourceContainer(market[i][selectedRow-1].getResourceType(),1));
        }

        ResourceContainer vacantCopy=new ResourceContainer(vacant.getResourceType(),1);
        vacant=market[0][selectedRow-1];
        for(int i=0;i<columns-1;i++){
            market[i][selectedRow-1]=market[i+1][selectedRow-1];
        }
        market[columns-1][selectedRow-1]=vacantCopy;

        return outputRow;
    }

    /**
     * This method returns a chosen column from the market grid.
     * @throws InvalidColumnNumber when an invalid column number is inserted
     */
    public ArrayList<ResourceContainer> getColumn(int selectedColumn) throws InvalidColumnNumber {
        if(selectedColumn<=0 || selectedColumn>columns)
            throw new InvalidColumnNumber ("Selected column isn't valid");

        ArrayList<ResourceContainer> outputColumn= new ArrayList<>();
        for(int i=0; i<rows; i++){
            outputColumn.add(new ResourceContainer(market[selectedColumn-1][i].getResourceType(),1));
        }

        ResourceContainer vacantCopy=new ResourceContainer(vacant.getResourceType(),1);
        vacant=market[selectedColumn-1][0];
        System.arraycopy(market[selectedColumn - 1], 1, market[selectedColumn - 1], 0, rows - 1);
        market[selectedColumn-1][rows-1]=vacantCopy;

        return outputColumn;
    }

    public ArrayList<ResourceContainer> getRowOrColumn(String selection, int num) throws InvalidColumnNumber, InvalidRowNumber {
        if (selection.toUpperCase().equals("COLUMN"))
            return getColumn(num);

        if (selection.toUpperCase().equals("ROW"))
            return getRow(num);

        return null;
    }

    /**
     * Returns the market as an arrayList of Resources
     */
    public ArrayList<ResourceContainer> getMarketSetUp(){
        ArrayList<ResourceContainer> myMarket=new ArrayList<>();
        for(int i=0; i<columns;i++){
            for(int j=0;j<rows;j++){
                myMarket.add(market[i][j]);
            }
        }
        myMarket.add(vacant);
        return myMarket;
    }

    public ResourceContainer getMarketCell (int rowNum,int columnNum){
      return market[columnNum][rowNum];
    }

    public ResourceContainer getVacant (){
        return vacant;
    }
    //------------------------------------------------------------------------------------------------------------------


    //PRINT METHODS----------(Only for testing purposes)----------------------------------------------------------------
    public void printMarket(){
        for(int i=0; i<rows;i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(market[j][i].getResourceType() + "   |   ");
            }
            System.out.println();
        }
        System.out.println("Il vacant è: "+vacant.getResourceType());
    }

    @Override
    public String toString() {
        StringBuilder markeToString = new StringBuilder();
        markeToString.append(Color.ANSI_CYAN.escape()).append("MARKET:").append(Color.ANSI_RESET.escape()).append("\n");

        for(int i=-1; i<rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(i == -1)
                    markeToString.append("\t").append("\t").append("\t").append("  ").append(j+1).append("\t").append("\t");
                else{
                    if(j == 0)
                        markeToString.append(i+1).append("\t").append("|").append("\t").append("\t");
                    markeToString.append(market[j][i].getResourceType()).append("\t").append("\t").append("|").append("\t").append("\t");
                }
            }
            markeToString.append("\n");
        }
        markeToString.append("\nThe vacant is: ").append(vacant.getResourceType());

        return markeToString.toString();
    }
    //------------------------------------------------------------------------------------------------------------------


}

/* Easter update
 CONGRATS! YOU FOUND THE SECOND EASTER EGG!
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣧⠀⠀⠀⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣿⣧⠀⠀⠀⢰⡿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⡟⡆⠀⠀⣿⡇⢻⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⠀⣿⠀⢰⣿⡇⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⡄⢸⠀⢸⣿⡇⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣿⡇⢸⡄⠸⣿⡇⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⣿⢸⡅⠀⣿⢠⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣿⣥⣾⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣆⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣿⡿⡿⣿⣿⡿⡅⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠉⠀⠉⡙⢔⠛⣟⢋⠦⢵⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⣄⠀⠀⠁⣿⣯⡥⠃⠀⢳⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⡇⠀⠀⠀⠐⠠⠊⢀⠀⢸⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⣿⣿⡿⠀⠀⠀⠀⠀⠈⠁⠀⠀⠘⣿⣄⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⣠⣿⣿⣿⣿⣿⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣷⡀⠀⠀⠀
⠀⠀⠀⠀⣾⣿⣿⣿⣿⣿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣿⣧⠀⠀
⠀⠀⠀⡜⣭⠤⢍⣿⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⢛⢭⣗⠀
⠀⠀⠀⠁⠈⠀⠀⣀⠝⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠠⠀⠀⠰⡅
⠀⠀⠀⢀⠀⠀⡀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠔⠠⡕⠀
⠀⠀⠀⠀⣿⣷⣶⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠀⠀⠀⠀
⠀⠀⠀⠀⠘⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠰⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠈⢿⣿⣦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠊⠉⢆⠀⠀⠀⠀
⠀⢀⠤⠀⠀⢤⣤⣽⣿⣿⣦⣀⢀⡠⢤⡤⠄⠀⠒⠀⠁⠀⠀⠀⢘⠔⠀⠀⠀⠀
⠀⠀⠀⡐⠈⠁⠈⠛⣛⠿⠟⠑⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠉⠑⠒⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀


*/