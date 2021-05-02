package it.polimi.ingsw.network.server;



import java.net.Socket;


public class EchoServerClientHandler implements Runnable {
    private Socket socket;
    ServerReceiver serverReceiver;
    ServerSender serverSender;
    LobbyManager lobbyManager;

    public EchoServerClientHandler(Socket socket, LobbyManager lobbyManager) {
        this.socket = socket;
        this.lobbyManager = lobbyManager;
    }

    public void run() {
        try {

            serverReceiver = new ServerReceiver(socket);
            serverReceiver.addObserverIO(lobbyManager);
            serverReceiver.start();

            serverSender = new ServerSender(socket);
            lobbyManager.addObserverIO(serverSender);
            serverSender.start();


        }catch (IllegalThreadStateException e){
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ServerReceiver getServerReceiver() {
        return serverReceiver;
    }
    public ServerSender getServerSender() {
        return serverSender;
    }
}
