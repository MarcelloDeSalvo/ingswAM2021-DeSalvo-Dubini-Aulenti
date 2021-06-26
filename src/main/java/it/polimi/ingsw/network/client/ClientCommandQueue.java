package it.polimi.ingsw.network.client;

import it.polimi.ingsw.view.ClientView;
import java.util.ArrayDeque;

public class ClientCommandQueue extends Thread {
    private final ClientView view;
    private final ArrayDeque<String> commands;
    private boolean exit = false;

    /**
     * A queue of commands received from server that run on a separate thread
     */
    public ClientCommandQueue( ClientView view) {
        this.view=view;
        commands=new ArrayDeque<>();
    }

    @Override
    public void run() {
        String topOfTheQueue;

        while (!exit) {
            synchronized (commands) {
                while(commands.size()==0) {
                    try {
                        commands.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if ((topOfTheQueue = commands.peek())!=null){
                    commands.remove(topOfTheQueue);
                    view.readUpdates(topOfTheQueue);
                }

            }
        }
    }

    /**
     * Adds a command to the queue, then notifies the lock on command to wake up the queue thread to execute it
     */
    public void addCommandToQueue(String command){
        synchronized (commands) {
            commands.addLast(command);
            commands.notify();
        }
    }

    /**
     * Kills the thread when called
     */
    public void exit(){
        exit= true;
    }
}
