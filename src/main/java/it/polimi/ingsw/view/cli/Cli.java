package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.parser.LeaderCardParser;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.view.ClientView;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cli extends ClientView {
    private ClientSender sender;
    private ArrayList<LeaderCard> leaderCards;
    private String nicknameTemp = null;

    public Cli() throws FileNotFoundException {
        leaderCards = LeaderCardParser.deserializeLeaderList();
    }

    @Override
    public void readUpdates(String mex){
        Gson gson = new Gson();
        Message deserializedMex = gson.fromJson(mex, Message.class);

        Command command = deserializedMex.getCommand();

        switch (command){
            case QUIT:
                printReply(deserializedMex.getInfo());
                break;

            case LOGIN:
                printReply(deserializedMex.getInfo());
                this.setNickname(nicknameTemp);
                break;

            case HELLO:
                printHello();
                break;

            case LOBBY_LIST:
                LobbyListMessage lobbyListMessage = gson.fromJson(mex, LobbyListMessage.class);
                printLobby(lobbyListMessage.getLobbiesInfos());
                break;

            case SHOW_HAND:
                ShowHandMessage showHandMessage = gson.fromJson(mex, ShowHandMessage.class);
                printHand(showHandMessage.getCardsID(), showHandMessage.getSenderNickname());
                break;

            case REPLY:
                printReply(deserializedMex.getInfo());
                break;


            case CHAT_ALL:
                System.out.print(Color.ANSI_MAGENTA.escape() + deserializedMex.getSenderNickname() + " in ALL chat:" + Color.RESET);
                printReply(deserializedMex.getInfo());
                break;

            case CHAT:
                ChatMessage chatMessage = gson.fromJson(mex, ChatMessage.class);
                System.out.print(Color.ANSI_BLUE.escape() + chatMessage.getReceiver() + Color.RESET + " whispers you: ");
                printReply(deserializedMex.getInfo());
                break;

        }
    }

    @Override
    public boolean readInput() {
        String userInput;
        Scanner stdIn = new Scanner(System.in);
        userInput = stdIn.next();
        try {
            switch (userInput.toUpperCase()) {

                //GENERAL-----------------------------------------------------------------------------------------------------
                case "CHAT":
                    sender.send(new ChatMessage(stdIn.next(), stdIn.nextLine(), this.getNickname()));
                    break;

                case "CHAT_ALL":
                    sender.send(new ChatMessage(stdIn.nextLine(),this.getNickname()));
                    break;

                case "HELLO":
                    sender.send(new Message.MessageBuilder().setCommand(Command.HELLO).
                            setInfo("Hello!").setNickname(this.getNickname()).build());
                    break;

                case "HELLO_ALL":
                    sender.send(new Message.MessageBuilder().setCommand(Command.HELLO_ALL).
                            setInfo("Hello all!").setNickname(this.getNickname()).build());
                    break;

                //LOBBY MANAGER PHASE-----------------------------------------------------------------------------------------------------------
                case "LOGIN":
                    nicknameTemp = stdIn.next();
                    Message login;

                    if (this.getNickname() != null){
                         login = new Message.MessageBuilder().setCommand(Command.LOGIN).setInfo(nicknameTemp).setNickname(this.getNickname()).build();
                    }else{
                         login = new Message.MessageBuilder().setCommand(Command.LOGIN).setInfo(nicknameTemp).build();
                    }

                    sender.send(login);
                    break;

                case "CREATE":
                    CreateLobbyMessage createLobbyMessage = new CreateLobbyMessage(stdIn.next(), stdIn.nextInt(), this.getNickname());
                    if(createLobbyMessage.getNumOfPlayers()>4 || createLobbyMessage.getNumOfPlayers()<1){
                        System.out.println("You cannot play with more than 4 people, please select a valid number");
                        break;
                    }
                    sender.send(createLobbyMessage);
                    break;

                case "JOIN":
                    sender.send(new JoinLobbyMessage(stdIn.next(), this.getNickname()));
                    break;

                case "REFRESH":
                    sender.send(new Message.MessageBuilder().setCommand(Command.LOBBY_LIST).setNickname(this.getNickname()).build());
                    break;

                //LOBBY PHASE-----------------------------------------------------------------------------------------------------------
                case "EXIT_LOBBY":
                    sender.send(new Message.MessageBuilder().setCommand(Command.EXIT_LOBBY).setNickname(this.getNickname()).build());
                    break;

                case "START_GAME":
                    sender.send(new Message.MessageBuilder().setCommand(Command.START_GAME).setNickname(this.getNickname()).build());
                    break;

                //GAME PHASE-----------------------------------------------------------------------------------------------------------
                case "SEND":

                    int qty = stdIn.nextInt();
                    if (!positive_not_zero(qty)) break;

                    String resourceType = stdIn.next();

                    //String from = stdIn.next();
                    //if (from(from)) break;

                    String destination = stdIn.next();
                    if (!vault_or_deposit(destination)) break;

                    int destinationID = stdIn.nextInt();
                    if (!positive_not_zero(destinationID)) break;

                    ResourceContainer container = new ResourceContainer(resourceType_validation(resourceType), qty);

                    SendContainer sendContainer = new SendContainer(container, destination, destinationID, this.getNickname());
                    System.out.println(sendContainer);

                    sender.send(sendContainer);
                    break;

                case "DISCARD_LEADER":
                    sender.send(new DiscardLeaderMessage(stdIn.nextInt(), this.getNickname()));
                    break;

                case "QUIT":
                    sender.send(new Message.MessageBuilder().setCommand(Command.QUIT).setNickname(this.getNickname()).build());
                    return false;

                default:
                    default_case();
            }
        }catch (InputMismatchException e){
             System.out.println("The command you submitted isn't valid, please consult " + Color.ANSI_YELLOW.escape() + "HELP" + Color.RESET + " to know more about commands");
        }
        return true;

    }

    private void default_case(){
        System.out.println("Invalid command, type " + Color.ANSI_RED.escape() + "HELP" + Color.RESET + " to see all available commands");
    }

    //INPUT CHECK-------------------------------------------------------------------------------------------------------
    private boolean positive_not_zero(int x){
        if(x<1){
            System.out.println("Please insert a non negative number \n");
            default_case();
            return false;
        }
        return true;
    }

    private boolean vault_or_deposit(String x){
        if(x.toUpperCase().equals("DEPOSIT") || x.toUpperCase().equals("VAULT")){
            return true;
        }

        default_case();
        return false;

    }

    private boolean from(String x){
        if(x.toUpperCase().equals("FROM")){
            default_case();
            return false;
        }

        return true;
    }

    private ResourceType resourceType_validation(String x){

        ResourceType resourceType;

        try {
            resourceType = ResourceType.valueOf(x.toUpperCase());
        }catch (IllegalArgumentException e){
            default_case();
            return null;
        }

        return resourceType;
    }
    //------------------------------------------------------------------------------------------------------------------

    //PRINTS OF THE VIEW -----------------------------------------------------------------------------------------------
    @Override
    public void setSender(ClientSender clientSender) {
        this.sender = clientSender;
    }

    @Override
    public void printHello() {
        System.out.println(Color.ANSI_CYAN.escape() + "Hello!" + Color.RESET);
    }

    @Override
    public void printQuit(String nickaname) {
        System.out.println("Disconnected");
    }

    @Override
    public void printReply(String payload) {
        System.out.println(payload+"\n");
    }

    @Override
    public void printHand(ArrayList<Integer> leaderIDs, String nickname) {
        System.out.println("These are your Leader Cards: \n");
        for (int id: leaderIDs)
            System.out.println(leaderCards.get(id-1).toString());
        System.out.println();
    }

    @Override
    public void printLobby(ArrayList<String> lobbiesInfos) {
        System.out.println("\n"+Color.ANSI_BLUE.escape()+"[LOBBIES]:"+Color.RESET);

        if (lobbiesInfos.isEmpty()){
            System.out.println("There are currently 0 active lobbies"+"\n");
            return;
        }

        for (String info : lobbiesInfos) {
            System.out.println(info);
        }
        System.out.println();
    }
}