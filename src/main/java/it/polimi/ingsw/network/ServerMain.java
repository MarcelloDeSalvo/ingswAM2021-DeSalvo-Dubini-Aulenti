package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.EchoServerClientHandler;
import it.polimi.ingsw.view.VirtualView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    private int port;

    public ServerMain()throws FileNotFoundException{
        File file = new File("src/main/resources/ServerConfig");
        Scanner sc = new Scanner(file);
        int i = 0;
        while (sc.hasNextLine()) {
            String data=sc.next();
            i++;
            if(i==2) {
                try{
                    port=Integer.parseInt(data);
                }
                catch (Exception e){
                    System.out.println("There was an issue with reading port number from the file, shutting down.");
                    System.exit(1);
                }
            }
        }
    }

    public static void main(String[] args) {
        try{
            ServerMain serverMain = new ServerMain();
            serverMain.startServer();
        }
        catch (Exception e){
            System.out.println("There was an issue with reading port number from the file, shutting down.");
            System.exit(1);
        }
    }

    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            return;
        }
        System.out.println("Server ready"+'\n');
        VirtualView virtualView= new VirtualView();
        Controller controller = new Controller(virtualView);
        virtualView.addObserverController(controller);

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new EchoServerClientHandler(socket,virtualView));
            } catch(IOException e) {
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }

}
