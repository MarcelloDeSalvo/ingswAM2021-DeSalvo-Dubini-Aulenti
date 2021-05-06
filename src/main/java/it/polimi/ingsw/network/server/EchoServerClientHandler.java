package it.polimi.ingsw.network.server;



import com.google.gson.Gson;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.UserManager;

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

            Message askNick = new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Welcome to the server, please select a valid nickname: ").build();
            out.println(askNick.serialize());


            String receivedMex = "";
            while ((receivedMex = in.readLine()) != null) {

                Message nickMex = gson.fromJson(receivedMex, Message.class);
                String nickname = nickMex.getSenderNickname();

                if (nickMex.getCommand() == Command.LOGIN) {

                    if(!lobbyManager.getConnectedPlayers().containsKey(nickname)){
                        out.println(new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("You inserted a valid nickname. Welcome to masters of renaissance").build().serialize());
                        out.flush();

                        serverReceiver = new ServerReceiver(socket, in);
                        serverSender = new ServerSender(socket, out);

                        User user = new User(nickname, serverReceiver, serverSender, Status.IN_LOBBY_MANAGER);
                        user.addLobbyOrView(lobbyManager);

                        UserManager.addPlayer(lobbyManager.getConnectedPlayers(), nickname, user);
                        lobbyManager.sendLobbyList(nickname);

                        serverReceiver.start();
                        serverSender.start();
                        break;

                    }
                    else{
                        out.println(new Message.MessageBuilder().setCommand(Command.REPLY)
                                .setInfo("Sorry, but the nickname is already in use. Try submitting another one again:").build().serialize());
                    }
                }
                else {
                    out.println(new Message.MessageBuilder().setCommand(Command.REPLY)
                            .setInfo("Incorrect command, please use the LOGIN command").build().serialize());
                }
            }


        }catch (IllegalThreadStateException | IOException e){
            e.printStackTrace();
        }

    }

}
