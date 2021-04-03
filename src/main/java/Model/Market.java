package Model;

import Model.Resources.ResourceContainer;

import java.util.ArrayList;
import java.util.Collections;

public class Market {
    ResourceContainer[][] market= new ResourceContainer[4][3];
    ResourceContainer vacant;

    public Market(ArrayList<ResourceContainer> marblesMarket){
        Collections.shuffle(marblesMarket);
        for(int i=0; i<4;i++){
            for(int j=0;j<3;j++){
                this.market[i][j]=marblesMarket.get(i*3+j);
                //System.out.print(market[i][j].getResourceType()+"      |      ");
            }
            //System.out.println();
        }

        for(int i=0; i<3;i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(market[j][i].getResourceType() + "   |   ");
            }
            System.out.println();
        }
            this.vacant=marblesMarket.get(12);
    }







}
