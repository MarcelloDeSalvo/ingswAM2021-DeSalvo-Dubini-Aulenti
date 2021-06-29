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
import java.net.SocketException;

public class EchoServerClientHandler implements Runnable {

    private boolean logged = false;
    private boolean exit = false;

    private final Gson gson;

    private final LobbyManager lobbyManager;

    private User user;
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private final String askNick = new Message.MessageBuilder().setCommand(Command.REPLY)
            .setInfo("Welcome to the server, please select a valid nickname: ").build().serialize();

    public EchoServerClientHandler(Socket socket, LobbyManager lobbyManager) {
        this.socket = socket;
        this.lobbyManager = lobbyManager;
        this.gson = new Gson();
    }

    public void run() {

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            System.out.println("Error while opening the socket's streams for " + user.getNickname() + " - " + e.getMessage());

        }

        out.println(askNick);

        String receivedMex;
        while (!exit) {
            try {
                if ((receivedMex = in.readLine()) != null)
                    messageHandler(receivedMex);

            } catch (IOException e) {
                String nick = logged ? user.getNickname() : "IP " + socket.getInetAddress().toString();
                System.out.println("! "+nick+": "+  e.getMessage());

                if (!logged)
                    exit = true;
                else if (!user.isConnected())
                    exit = true;

                try{
                    Thread.sleep(1000);
                }catch (InterruptedException i){
                    System.out.println(i.getMessage());
                }
            }
        }

        if (!logged)
            System.out.println("# IP: " + socket.getInetAddress() + " logged out before entering a valid nickname");
        else
            System.out.println("# " +user.getNickname() + " has disconnected");

        try {
            in.close();
            out.close();
            socket.close();
        }catch (IOException e){
            System.out.println("Error while closing the socket's streams: " + e.getMessage());

        }

    }

    /**
     * Deserializes and manages the login and quit message. <br>
     * It also responds to the Pongs and forwards the other messages to the other server areas
     * @param receivedMex the received message
     */
    private void messageHandler(String receivedMex){
        Message nickMex = gson.fromJson(receivedMex, Message.class);
        Command command = nickMex.getCommand();
        String nickname = nickMex.getInfo();

        switch (command) {
            case PONG:
                user.pongReceived();
                System.out.println(user.getNickname() + " PONG RECEIVED " + user.getCounter());
                break;

            case LOGIN:
                if (logged)
                    out.println(new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Incorrect command, you are already logged in the server!").build().serialize());
                else
                    authenticationPhase(out, nickname);
                break;

            case QUIT:
                out.println(new Message.MessageBuilder().setCommand(Command.REPLY).setInfo("Bye bye!").build().serialize());
                if (logged)
                    user.disconnect();
                exit = true;
                break;

            default:
                if (user == null) break;
                user.pongReceived();
                user.notifyServerAreas(command, receivedMex);
        }
    }

    /**
     * Manages every possible option regarding the "Authentication Phase":
     * - Blocks the players from selecting the nickname "LORENZO".
     * - If the selected nickname is not present the login is correctly executed.
     * - If the selected name is already present and the player is "connected", an error message is sent to the user.
     * - If the selected name is already present but the player is NOT "connected", reconnection is executed.
     */
    private void authenticationPhase(PrintWriter out, String nickname) {

        if(nickname.equalsIgnoreCase("lorenzo")) {
            out.println(new Message.MessageBuilder().setCommand(Command.REPLY)
                    .setInfo("Sorry but you cannot use the nickname '" + nickname + "'").build().serialize());
        }
        else if (!lobbyManager.getConnectedPlayers().containsKey(nickname)) {
            correctLogin(out, nickname);
        }
        else {
            if (lobbyManager.getConnectedPlayers().get(nickname).isConnected())
                out.println(new Message.MessageBuilder().setCommand(Command.REPLY)
                        .setInfo("Sorry, but the nickname is already in use. Try submitting another one").build().serialize());
            else {
                reconnection(out, nickname);
            }
        }
    }

    /**
     * Called when the Login can be done correctly.
     * Creates a new "User" and sets it "IN_LOBBY_MANAGER"
     */
    private void correctLogin(PrintWriter out, String nickname){
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
    }

    /**
     * Called when a Reconnection needs to be executed
     */
    private void reconnection(PrintWriter out, String nickname) {
        out.println(new Message.MessageBuilder().setCommand(Command.RECONNECTED)
                .setInfo("It looks like you had disconnected. Welcome back!").build().serialize());

        lobbyManager.getConnectedPlayers().get(nickname).reconnect();

        System.out.println("# " + nickname + " has reconnected into the server \n");

        logged = true;
    }

}
