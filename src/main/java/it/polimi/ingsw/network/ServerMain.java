package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.network.server.EchoServerClientHandler;
import it.polimi.ingsw.network.server.LobbyManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    
    private int port = 50623;

    public static void main(String[] args) {
        try{
            /*
            String jsonPath = "src/main/resources/ConfigurationFiles/ServerConfig.json";
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(ServerMain.class.getResourceAsStream(jsonPath), StandardCharsets.UTF_8);
            //Reader reader = new FileReader(ServerMain.class.getResourceAsStream(jsonPath).toString());
            ServerMain serverMain = gson.fromJson(reader, ServerMain.class);+/
             */

            ServerMain serverMain = new ServerMain();
            serverMain.startServer();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("There was an issue with starting the server.");
            System.exit(1);
        }
    }

    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
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
