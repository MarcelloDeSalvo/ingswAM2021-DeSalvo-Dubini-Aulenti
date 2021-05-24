package it.polimi.ingsw;

import it.polimi.ingsw.network.ClientMain;
import it.polimi.ingsw.network.ServerMain;

public class Launcher {

    public static void main(String[] args){

            if(args[0].equals("-Server")){
                ServerMain.main(args);
            }
            else if (args[0].equals("-Client")){
                ClientMain.main(args);
            }

    }
}
