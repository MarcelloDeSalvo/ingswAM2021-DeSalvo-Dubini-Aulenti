package it.polimi.ingsw.network.client;

import it.polimi.ingsw.view.ClientView;

import java.util.ArrayDeque;
import java.util.Queue;

public class ClientCommandQueue extends Thread {
    private ClientView view;
    private ArrayDeque<String> commands;

    public ClientCommandQueue( ClientView view) {
        this.view=view;
        commands=new ArrayDeque<>();
    }

    @Override
    public void run() {
        String topOfTheQueue = " ";
            while (true) {
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
}
