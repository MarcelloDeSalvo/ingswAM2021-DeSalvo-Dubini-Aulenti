package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.parser.LeaderCardParser;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.view.ClientView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cli extends ClientView {

    private final ArrayList<LeaderCard> leaderCards;
    private String nicknameTemp = null;
    private final Scanner stdIn;

    public Cli() throws FileNotFoundException {
        leaderCards = LeaderCardParser.deserializeLeaderList();
        stdIn = new Scanner(System.in);
    }

    //USER INPUT AND UPDATES--------------------------------------------------------------------------------------------
    @Override
    public void readUpdates(String mex){
        Gson gson = new Gson();
        Message deserializedMex = gson.fromJson(mex, Message.class);

        Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        System.out.println();

        switch (command){

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

            case SHOW_TURN_HELP:
                printItsYourTurn(senderNick);
                break;


            case SHOW_HAND:
                ShowHandMessage showHandMessage = gson.fromJson(mex, ShowHandMessage.class);
                printHand(showHandMessage.getCardsID(), senderNick);
                break;

            case REPLY:
                printReply(deserializedMex.getInfo());
                break;

            case CHAT_ALL:
                System.out.print(Color.ANSI_PURPLE.escape() + senderNick + " in ALL chat:" + Color.RESET);
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

        userInput = stdIn.next();

        try {
            switch (userInput.toUpperCase()) {

                //GENERAL-----------------------------------------------------------------------------------------------
                case "CHAT":
                    send(new ChatMessage(stdIn.next(), stdIn.nextLine(), this.getNickname()));
                    break;

                case "CHAT_ALL":
                    send(new ChatMessage(stdIn.nextLine(),this.getNickname()));
                    break;

                case "HELLO":
                    send(new Message.MessageBuilder().setCommand(Command.HELLO).
                            setInfo("Hello!").setNickname(this.getNickname()).build());
                    break;

                case "HELLO_ALL":
                    send(new Message.MessageBuilder().setCommand(Command.HELLO_ALL).
                            setInfo("Hello all!").setNickname(this.getNickname()).build());
                    break;


                //LOBBY MANAGER PHASE-----------------------------------------------------------------------------------
                case "LOGIN":
                    nicknameTemp = stdIn.next();
                    stdIn.nextLine();
                    Message login;

                    if (this.getNickname() != null)
                        login = new Message.MessageBuilder().setCommand(Command.LOGIN).setInfo(nicknameTemp).setNickname(this.getNickname()).build();
                    else
                        login = new Message.MessageBuilder().setCommand(Command.LOGIN).setInfo(nicknameTemp).build();


                    send(login);
                    break;

                case "CREATE":
                    CreateLobbyMessage createLobbyMessage = new CreateLobbyMessage(stdIn.next(), stdIn.nextInt(), this.getNickname());
                    if(createLobbyMessage.getNumOfPlayers()>4 || createLobbyMessage.getNumOfPlayers()<1){
                        System.out.println("You cannot play with more than 4 people, please select a valid number");
                        break;
                    }
                    send(createLobbyMessage);
                    break;

                case "JOIN":
                    send(new JoinLobbyMessage(stdIn.next(), this.getNickname()));
                    break;

                case "REFRESH":
                    send(new Message.MessageBuilder().setCommand(Command.LOBBY_LIST).setNickname(this.getNickname()).build());
                    break;


                //LOBBY PHASE-------------------------------------------------------------------------------------------
                case "EXIT_LOBBY":
                    send(new Message.MessageBuilder().setCommand(Command.EXIT_LOBBY).setNickname(this.getNickname()).build());
                    break;

                case "START_GAME":
                    send(new Message.MessageBuilder().setCommand(Command.START_GAME).setNickname(this.getNickname()).build());
                    break;

                case "SHOW_PLAYERS":
                    send(new Message.MessageBuilder().setCommand(Command.PLAYER_LIST).setNickname(this.getNickname()).build());
                    break;


                //GAME PHASE--------------------------------------------------------------------------------------------
                case "SELECT":
                    if (!set_up_Container(stdIn))
                        default_case();

                    break;

                case "GIVE":
                    if (!giveContainer(stdIn))
                        default_case();

                    break;

                case "BUY":
                    BuyMessage buyMessage = new BuyMessage(stdIn.nextInt(), stdIn.nextInt(), stdIn.nextInt(), this.getNickname());
                    send(buyMessage);
                    break;

                case "PRODUCE":
                    ProduceMessage produceMessage = new ProduceMessage(stdIn.nextInt(), this.getNickname());
                    send(produceMessage);
                    break;

                case "FILL":
                    ResourceType questionMarkType = InputCheck.resourceType_null(stdIn.next());
                    if (questionMarkType == null) return false;

                    ResourceTypeSend resourceTypeSend = new ResourceTypeSend(Command.FILL_QM, questionMarkType, this.getNickname());
                    send(resourceTypeSend);
                    break;

                case "MARKET":
                    String selection = stdIn.next(); //MUST BE ROW OR COLUMN
                    int num = stdIn.nextInt();

                    if(InputCheck.not_row_or_column(selection)) {
                        default_case();
                        break;
                    }

                    MarketMessage marketMessage = new MarketMessage(selection, num, this.getNickname());
                    send(marketMessage);
                    break;

                case "CONVERSION":
                    ResourceType conversionType = InputCheck.resourceType_null(stdIn.next());
                    if (conversionType == null) return false;

                    ResourceTypeSend convTypeSend = new ResourceTypeSend(Command.CONVERSION, conversionType, this.getNickname());
                    send(convTypeSend);
                    break;

                case "DISCARD":
                    send(new LeaderIdMessage(Command.DISCARD_LEADER, stdIn.nextInt(), this.getNickname()));
                    break;

                case "ACTIVATE":
                    send(new LeaderIdMessage(Command.ACTIVATE_LEADER, stdIn.nextInt(), this.getNickname()));
                    break;

                case "MOVE":
                    int qty = stdIn.nextInt();
                    int sourceId =stdIn.nextInt();
                    String to = stdIn.next();
                    if (InputCheck.not_to(to)){
                        default_case();
                        break;
                    }
                    int destId = stdIn.nextInt();

                    send(new ManageDepositMessage(qty,sourceId, destId, this.getNickname()));
                    break;

                case "SHOW_DEPOSIT":
                    send(new Message.MessageBuilder().setCommand(Command.SHOW_DEPOSIT).setNickname(this.getNickname()).build());
                    break;

                case "END_TURN":
                    send(new Message.MessageBuilder().setCommand(Command.END_TURN).setNickname(this.getNickname()).build());
                    break;

                case "QUIT":
                    send(new Message.MessageBuilder().setCommand(Command.QUIT).setNickname(this.getNickname()).build());
                    return false;

                default:
                    stdIn.nextLine();
                    default_case();
            }

        }catch (InputMismatchException e){
            stdIn.nextLine();
            System.out.println("The command you submitted isn't valid, please consult " + Color.ANSI_YELLOW.escape() + "HELP" + Color.RESET + " to know more about commands");
        }

        return true;
    }


    private void default_case(){
        System.out.println("Invalid command, type " + Color.ANSI_RED.escape() + "HELP" + Color.RESET + " to see all available commands");
    }


    /**
     * Validates the input when the player has to choose a free resourceType during the game setup Phase
     */
    private boolean set_up_Container(Scanner stdIn){
        SendContainer sendContainer;

        ResourceType resType = InputCheck.resourceType_null(stdIn.next());
        if (resType == null) return false;

        String destination = stdIn.next();
        if(InputCheck.not_deposit(destination)) return false;

        int destinationID = stdIn.nextInt();

        ResourceContainer container = new ResourceContainer(resType, 1);
        sendContainer = new SendContainer(Command.SETUP_CONTAINER, container, destination, destinationID, this.getNickname());

        //System.out.println(sendContainer);
        send(sendContainer);

        return true;
    }


    /**
     * Validates the input when the player has to choose a ResourceContainer in order to Produce or Buy
     */
    private boolean giveContainer(Scanner stdIn){
        SendContainer sendContainer;

        int qty = stdIn.nextInt();

        ResourceType resType = InputCheck.resourceType_null(stdIn.next());
        if (resType == null) return false;

        String from = stdIn.next();
        if (InputCheck.not_from(from)) return false;

        String destination = stdIn.next();
        if (InputCheck.not_vault_or_deposit(destination)) return false;

        if(destination.equals("DEPOSIT")){
            int destinationID = stdIn.nextInt();

            ResourceContainer container = new ResourceContainer(resType, qty);
            sendContainer = new SendContainer(Command.SEND_CONTAINER, container, destination, destinationID, this.getNickname());

        }else{ //VAULT
            ResourceContainer container = new ResourceContainer(resType, qty);
            sendContainer = new SendContainer(container, destination, this.getNickname());

        }

        //System.out.println(sendContainer);
        send(sendContainer);
        return true;
    }
    //------------------------------------------------------------------------------------------------------------------


    //PRINTS AND NOTIFIES OF THE VIEW ----------------------------------------------------------------------------------
    @Override
    public void printHello() {
        System.out.println(Color.ANSI_CYAN.escape() + "Hello!" + Color.RESET);
    }

    @Override
    public void printQuit(String nickname) {
        System.out.println("Disconnected");
    }


    @Override
    public void printReply(String payload) {
        System.out.println(payload + "\n");
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
        System.out.println(Color.ANSI_BLUE.escape() + "[LOBBIES]:" + Color.RESET);

        if (lobbiesInfos.isEmpty()){
            System.out.println("There are currently 0 active lobbies"+"\n");
            return;
        }

        for (String info : lobbiesInfos) {
            System.out.println(info);
        }
        System.out.println();
    }

    @Override
    public void printOrder(ArrayList<String> randomOrder) {

    }

    @Override
    public void printDeposit(String depositInfo) {

    }

    @Override
    public void askForResources(String nickname, int qty) {

    }

    @Override
    public void notifyFaithPathProgression(int qty, String nickname) {

    }

    @Override
    public void printLeaderCardRequest(String nickname) {

    }

    @Override
    public void printItsYourTurn(String nickname){
        printReply_uni("It is your turn, chose an action: " + "" +
                        "\n1)BUY A CARD (>BUY Row Column ProductionSlotID) " +
                        "\n2)SELECT FROM MARKET (>MARKET Row||Column number)" +
                        "\n3)PRODUCE (>PRODUCE cardID)"+
                        "\n4)ACTIVATE LEADER (>ACTIVATE leaderID)"+
                        "\n5)MANAGE DEPOSIT (>MOVE Qty Source_DepositID TO Destination_DepositID)"+
                        "\n6)END TURN (>END_TURN)" +
                        "\n7)SHOW (>SHOW_objectToShow)" +
                        "\nType HELP to see the full command list ", nickname);
    }

    @Override
    public void printReply_uni(String payload, String nickname) {
        printReply(payload);
    }

    @Override
    public void printReply_everyOneElse(String payload, String nickname) {
        printReply(payload);
    }
    //------------------------------------------------------------------------------------------------------------------
}