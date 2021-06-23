package it.polimi.ingsw.network.client;

import it.polimi.ingsw.view.ClientView;
import java.util.ArrayDeque;

public class ClientCommandQueue extends Thread {
    private final ClientView view;
    private final ArrayDeque<String> commands;
    private boolean exit = false;

    public ClientCommandQueue( ClientView view) {
        this.view=view;
        commands=new ArrayDeque<>();
    }

    @Override
    public void run() {
        String topOfTheQueue;

        while (!exit) {
                if ((topOfTheQueue = commands.peek()) != null) {
                    view.readUpdates(topOfTheQueue);
                    commands.remove(topOfTheQueue);
                }

            Thread.yield();
        }
    }

    public void addCommandToQueue(String command){
        commands.addLast(command);
    }

    /**
     * Kills the thread when called
     */
    public void exit(){
        exit= true;
    }
}
