package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.ObserverController;
import it.polimi.ingsw.view.ClientView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientReceiver implements ObserverController {

    private final Socket socket;
    private final ClientView view;
    private final ExecutorService commandExecutor;
    private final String ping = new Message.MessageBuilder().setCommand(Command.PONG).build().serialize();

    private PrintWriter out;
    private BufferedReader in;

    private boolean exit = false;

    /**
     * Starts the connection with the socket  <br>
     * Creates a singleThreadExecutor in order to split the network from the view execution that could interrupt
     * the system.
     * @param view the selected view
     * @param hostname the ip address
     * @param portNum the port number
     * @throws Exception when the socket could not be opened
     */
    public ClientReceiver (ClientView view, String hostname, int portNum) throws Exception{

        try {
            socket = new Socket(hostname, portNum);

        } catch (UnknownHostException e) {
            throw new Exception("Don't know about host " + hostname);

        } catch (IOException e) {
            throw new Exception("Couldn't get I/O for the connection to " + portNum);

        }

        this.view = view;
        this.commandExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Starts reading from the socket's streams <br>
     *     If the connection is lost, it closes the socket and the streams
     */
    public void start() {
        Gson gson = new Gson();

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            System.out.println("Error while opening the socket's streams: " + e.getMessage());
            exit();
        }

        String receivedMex;
        while (!exit) {
            try {
                if ((receivedMex = in.readLine()) != null) {
                    Message deserializedMex = gson.fromJson(receivedMex, Message.class);
                    Command command = deserializedMex.getCommand();

                    if (command == Command.PING)
                        update(ping, null, null);
                    else {
                        String finalReceivedMex = receivedMex;
                        commandExecutor.submit(() -> view.readUpdates(finalReceivedMex));
                    }

                }else
                    throw new IOException("connection lost");

            } catch (IOException e) {
                System.out.println("Lost connection to the server ("+e.getMessage()+"): " + socket.getInetAddress());
                view.onDisconnected();
                exit();

            } catch (JsonSyntaxException e) {
                System.out.println("Wrong syntax, the message has been discarded");

            }
        }

        try {
            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            System.err.println("Error while closing the socket: " + e.getMessage());
        }

        commandExecutor.shutdown();
        System.out.println("Master of Renaissance closed");
    }

    @Override
    public synchronized void update(String mex, Command command, String senderNick) {
        out.println(mex);
        out.flush();
    }

    /**
     * Closes the connection when called
     */
    @Override
    public synchronized void exit(){
        exit = true;
    }

}
