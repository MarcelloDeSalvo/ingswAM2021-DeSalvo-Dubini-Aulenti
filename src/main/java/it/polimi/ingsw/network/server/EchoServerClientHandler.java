package it.polimi.ingsw.network.server;



import com.google.gson.Gson;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.observers.UserManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Gson gson=new Gson();

            Message askNick = new Message(Command.REPLY, "Welcome to the server, please select a valid nickname: ");
            out.println(askNick.serialize());


            String receivedMex = "";
            while ((receivedMex = in.readLine()) != null) {

                Message nick = gson.fromJson(receivedMex, Message.class);

                if (nick.getCommand() == Command.LOGIN) {

                    if(!lobbyManager.getConnectedPlayers().containsKey(nick.getInfo())){
                        out.println(new Message(Command.REPLY,"You inserted a valid nickname. Welcome to masters of renaissance").serialize());
                        out.flush();

                        serverReceiver = new ServerReceiver(socket, in);
                        serverSender = new ServerSender(socket, out);

                        User user = new User(nick.getInfo(), serverReceiver, serverSender, Status.IN_LOBBY_MANAGER);
                        user.addLobbyOrView(lobbyManager);

                        UserManager.addPlayer(lobbyManager.getConnectedPlayers(), nick.getInfo(), user);

                        serverReceiver.start();
                        serverSender.start();
                        break;

                    }
                    else{
                        out.println(new Message(Command.REPLY,"Sorry, but the nickname is already in use. Try submitting another one again:").serialize());
                    }
                }
                else {
                    out.println(new Message(Command.REPLY,"Incorrect command, please use the LOGIN command").serialize());
                }
            }





        }catch (IllegalThreadStateException | IOException e){
            e.printStackTrace();
        }

    }

}