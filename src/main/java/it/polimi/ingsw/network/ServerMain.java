package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.network.server.EchoServerClientHandler;
import it.polimi.ingsw.network.server.LobbyManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    private final int port;

    public ServerMain(int port) {
        this.port = port;
    }

    /**
     * Tries to read the command line arguments or the configuration file if there aren't any, then it creates a Server Main and starts it
     * @param args the arguments passed through the command line
     */
    public static void main(String[] args) {
        if(args.length==2){
            if (args[0].equalsIgnoreCase("-PORT"))
                try {
                    int portNumber = Integer.parseInt(args[1]);
                    ServerMain serverMain = new ServerMain(portNumber);
                    serverMain.startServer();

                }catch (NumberFormatException e){
                    System.err.println("The port is not a string");
                    System.exit(1);
                }

        }else{
            try{
                String jsonPath = "/ConfigurationFiles/ServerConfig.json";
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(ServerMain.class.getResourceAsStream(jsonPath), StandardCharsets.UTF_8);
                ServerMain serverMain = gson.fromJson(reader, ServerMain.class);
                serverMain.startServer();
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("There was an issue with starting the server.");
                System.exit(1);
            }
        }
    }

    /**
     * Creates a new socket that waits for the connections and a Thread Pool
     */
    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("PORT: " + port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Port not available
            return;
        }
        System.out.println("Server ready"+'\n');

        LobbyManager lobbyManager = new LobbyManager();

        while (true) {
            try {
                Socket socket = serverSocket.accept();

                EchoServerClientHandler echoClient = new EchoServerClientHandler(socket, lobbyManager);
                executor.submit(echoClient);

            } catch(IOException e) {

                System.out.println("There was an issue with accepting the socket.");
                break;
            }
        }

        executor.shutdown();
    }

}
