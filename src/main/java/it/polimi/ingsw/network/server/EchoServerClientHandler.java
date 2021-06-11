package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.UserManager;
import it.polimi.ingsw.view.cli.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class EchoServerClientHandler implements Runnable {

    private boolean logged = false;
    private final Socket socket;
    private final LobbyManager lobbyManager;

    private User user;

    public EchoServerClientHandler(Socket socket, LobbyManager lobbyManager) {
        this.socket = socket;
        this.lobbyManager = lobbyManager;
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Gson gson = new Gson();

            Message askNick = new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Welcome to the server, please select a valid nickname: ").build();
            out.println(askNick.serialize());

            String receivedMex = "";

            while ((receivedMex = in.readLine()) != null) {

                Message nickMex = gson.fromJson(receivedMex, Message.class);
                Command command = nickMex.getCommand();
                String nickname = nickMex.getInfo();

                if (command == Command.LOGIN) {

                    if (logged){
                        out.println(new Message.MessageBuilder().setCommand(Command.REPLY)
                                .setInfo("Incorrect command, please use the LOGIN command").build().serialize());

                    }else{

                        if (!lobbyManager.getConnectedPlayers().containsKey(nickname)) {
                            out.println(new Message.MessageBuilder().setCommand(Command.LOGIN).
                                    setInfo("You inserted a valid nickname.\n" + Color.ANSI_WHITE_BOLD_FRAMED.escape() + "---\t Welcome to masters of renaissance " + nickname + "! Here's a list of all available lobbies: \t---" + Color.ANSI_RESET.escape()).
                                    build().serialize());

                            out.flush();

                            user = new User(nickname, out, Status.IN_LOBBY_MANAGER);

                            user.addServerArea(lobbyManager);

                            UserManager.addPlayer(lobbyManager.getConnectedPlayers(), nickname, user);
                            lobbyManager.sendLobbyList(nickname);

                            System.out.println("# " + nickname + " has logged into the server \n");

                            logged = true;

                        } else {

                            if (lobbyManager.getConnectedPlayers().get(nickname).isActive())
                                out.println(new Message.MessageBuilder().setCommand(Command.REPLY)
                                        .setInfo("Sorry, but the nickname is already in use. Try submitting another one again").build().serialize());
                            else {
                                out.println(new Message.MessageBuilder().setCommand(Command.RECONNECTED)
                                        .setInfo("It looks like you had disconnected. Welcome back!").build().serialize());

                                lobbyManager.getConnectedPlayers().get(nickname).reconnect();

                                System.out.println("# " + nickname + " has reconnected into the server \n");

                                logged = true;
                            }
                        }
                    }
                }
                else {
                    if (user!=null)
                        user.notifyServerAreas(command, receivedMex);
                }
            }


        }catch (IllegalThreadStateException | IOException e){
            System.out.println("# The user logged out before entering a nickname -> " + "IP: " + socket.getInetAddress());
            //e.printStackTrace();
        }
    }

}
