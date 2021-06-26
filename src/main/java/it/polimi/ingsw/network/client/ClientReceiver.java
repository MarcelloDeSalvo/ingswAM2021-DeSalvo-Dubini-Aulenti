package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObserverController;
import it.polimi.ingsw.view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientReceiver extends Thread implements ObserverController {
    private final Socket socket;
    private final ClientView view;
    private boolean exit = false;
    private ExecutorService executorService;

    private PrintWriter out;

    public ClientReceiver (Socket socket, ClientView view){
        this.socket = socket;
        this.view = view;
        this.executorService= Executors.newSingleThreadExecutor();
    }

    @Override
    public void run() {

        try {
            //ClientCommandQueue clientCommandQueue = new ClientCommandQueue(view);
            //clientCommandQueue.start();

            Gson gson = new Gson();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String receivedMex;

            while (!exit) {
                if((receivedMex = in.readLine()) != null) {
                    Message deserializedMex = gson.fromJson(receivedMex, Message.class);
                    Command command = deserializedMex.getCommand();

                    if(command==Command.PING)
                        update(new Message.MessageBuilder().setCommand(Command.PONG).build().serialize(),null, null);
                    else {
                        String finalReceivedMex = receivedMex;
                        executorService.submit(()->view.readUpdates(finalReceivedMex));
                        //view.readUpdates(receivedMex);
                        //clientCommandQueue.addCommandToQueue(receivedMex);
                    }
                }
                else
                    throw new IOException();

            }

            //clientCommandQueue.exit();
            in.close();

        } catch (IOException e) {
            view.onDisconnected();
            System.out.println("Disconnected from the server");

        } catch (JsonSyntaxException e) {
            System.out.println("Wrong syntax, the message has been discarded");
        }
    }

    @Override
    public synchronized void update(String mex, Command command, String senderNick) {
        out.println(mex);
        out.flush();

    }

    /**
     * Kills the thread when called
     */
    public void exit(){
        exit= true;
    }

}
