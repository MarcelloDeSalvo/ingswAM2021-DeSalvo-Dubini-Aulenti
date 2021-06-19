package it.polimi.ingsw;

import it.polimi.ingsw.network.ClientMain;
import it.polimi.ingsw.network.ServerMain;

import java.util.Arrays;

public class Launcher {

    public static void main(String[] args){

        if (args.length==0)
            return;

        if(args[0].equalsIgnoreCase("-Server")){
            ServerMain.main(Arrays.stream(args).skip(1).toArray(String[]::new));
        }
        else if (args[0].equalsIgnoreCase("-Client")){
            ClientMain.main(Arrays.stream(args).skip(1).toArray(String[]::new));
        }

    }
}
