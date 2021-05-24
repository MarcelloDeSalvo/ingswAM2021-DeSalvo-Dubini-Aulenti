package it.polimi.ingsw;

import it.polimi.ingsw.network.ClientMain;
import it.polimi.ingsw.network.ServerMain;

public class Launcher {

    public static void main(String[] args){

        if (args[0] == null)
            return;

        if(args[0].equalsIgnoreCase("-Server")){
            ServerMain.main(args);
        }
        else if (args[0].equalsIgnoreCase("-Client")){
            ClientMain.main(args);
        }

    }
}
