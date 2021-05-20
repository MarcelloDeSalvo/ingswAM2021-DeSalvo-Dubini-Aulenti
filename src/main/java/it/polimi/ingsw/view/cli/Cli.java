package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.liteModel.LiteCardGrid;
import it.polimi.ingsw.liteModel.LiteFaithPath;
import it.polimi.ingsw.liteModel.LiteHand;
import it.polimi.ingsw.model.Cardgrid;
import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.player.deposit.Deposit;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.view.ClientView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Cli extends ClientView {

    private boolean isInGame = false;
    private String nicknameTemp = null;
    private final Scanner stdIn;
    private final Gson gson ;

    public Cli() throws FileNotFoundException {
        super();
        stdIn = new Scanner(System.in);
        gson = new Gson();
    }

    //USER INPUT AND UPDATES--------------------------------------------------------------------------------------------
    @Override
    public void readUpdates(String mex){
        Message deserializedMex = gson.fromJson(mex, Message.class);

        Command command = deserializedMex.getCommand();
        String senderNick = deserializedMex.getSenderNickname();

        if(command!= Command.PING)
            System.out.println();

        switch (command){

            case LOGIN:
                printReply(deserializedMex.getInfo());
                this.setNickname(nicknameTemp);
                break;

            case HELLO:
                printHello();
                break;

            case PING:
                send(new Message.MessageBuilder().setCommand(Command.PONG).
                        setNickname(this.getNickname()).build());
                break;

            case NOTIFY_CARDGRID:
                NotifyCardGrid notifyCardGrid = gson.fromJson(mex, NotifyCardGrid.class);
                notifyCardGridChanges(notifyCardGrid.getOldID(), notifyCardGrid.getNewID());
                break;

            case NOTIFY_HAND:
                ShowHandMessage showHandMessage = gson.fromJson(mex, ShowHandMessage.class);
                setHand(new LiteHand(showHandMessage.getCardsID(), getLeaderCards()));
                printHand(null, "");
                break;

            case NOTIFY_FAITHPATH_CURRENT:
                FaithPathUpdateMessage faithPathUpdateMessage= gson.fromJson(mex,FaithPathUpdateMessage.class);
                notifyCurrentPlayerIncrease(faithPathUpdateMessage.getId(), senderNick);
                break;

            case NOTIFY_FAITHPATH_OTHERS:
                FaithPathUpdateMessage faithPathUpdateOthersMessage= gson.fromJson(mex,FaithPathUpdateMessage.class);
                notifyOthersIncrease(faithPathUpdateOthersMessage.getId(), senderNick);
                break;

            case NOTIFY_FAITHPATH_FAVOURS:
                PapalFavourUpdateMessage papalFavourUpdateMessage=gson.fromJson(mex, PapalFavourUpdateMessage.class);
                notifyPapalFavour(papalFavourUpdateMessage.getPlayerFavours(), senderNick);
                break;

            case DISCARD_OK:
                IdMessage idMessage =gson.fromJson(mex, IdMessage.class);
                notifyLeaderDiscarded(idMessage.getId(),"");
                break;

            case ACTIVATE_OK:
                IdMessage activatedLeader = gson.fromJson(mex, IdMessage.class);
                notifyLeaderActivated(activatedLeader.getId(), "");
                break;

            case BUY_OK:
                notifyBoughtCard(senderNick);
                break;

            case GAME_SETUP:
                GameSetUp gameSetUp=gson.fromJson(mex,GameSetUp.class);
                notifyGameSetup(gameSetUp.getCardGridIDs(), gameSetUp.getNicknames());
                break;

            case END_GAME:
                notifyGameEnded();
                break;

            case LOBBY_LIST:
                LobbyListMessage lobbyListMessage = gson.fromJson(mex, LobbyListMessage.class);
                printLobby(lobbyListMessage.getLobbiesInfos());
                break;

            case SHOW_TURN_HELP:
                printItsYourTurn(senderNick);
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

                case "H":
                case "HELP":
                    printHelp();
                break;

                //LOBBY MANAGER PHASE-----------------------------------------------------------------------------------
                case "L":
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

                case "CR":
                case "CREATE":
                    CreateLobbyMessage createLobbyMessage = new CreateLobbyMessage(stdIn.next(), stdIn.nextInt(), this.getNickname());
                    if(createLobbyMessage.getNumOfPlayers()>4 || createLobbyMessage.getNumOfPlayers()<1){
                        throw new InputMismatchException("You cannot play with more than 4 people, please select a valid number");
                    }

                    send(createLobbyMessage);
                    break;

                case "J":
                case "JOIN":
                    send(new JoinLobbyMessage(stdIn.next(), this.getNickname()));
                    break;

                case "R":
                case "REFRESH":
                    send(new Message.MessageBuilder().setCommand(Command.LOBBY_LIST).setNickname(this.getNickname()).build());
                    break;


                //LOBBY PHASE-------------------------------------------------------------------------------------------
                case "EL":
                case "EXIT_LOBBY":
                    send(new Message.MessageBuilder().setCommand(Command.EXIT_LOBBY).setNickname(this.getNickname()).build());
                    break;

                case "SG":
                case "START_GAME":
                    send(new Message.MessageBuilder().setCommand(Command.START_GAME).setNickname(this.getNickname()).build());
                    break;

                case "SPL":
                case "SHOW_PLAYERS":
                    send(new Message.MessageBuilder().setCommand(Command.PLAYER_LIST).setNickname(this.getNickname()).build());
                    break;


                //GAME PHASE--------------------------------------------------------------------------------------------
                case "S":
                case "SELECT":
                    if (!set_up_Container(stdIn))
                        throw new InputMismatchException();

                    break;

                case "PUT":
                    ResourceType marketRes = InputCheck.resourceType_null(stdIn.next());
                    if (marketRes == null)  throw new InputMismatchException("Invalid resourceType");

                    String in = stdIn.next();
                    if (InputCheck.not_in(in)) throw new InputMismatchException("Wrong syntax");

                    String destination = stdIn.next();
                    if (InputCheck.not_deposit(destination))  throw new InputMismatchException("Invalid destination");

                    send(new SendContainer(Command.SEND_DEPOSIT_ID, new ResourceContainer(marketRes,1), destination, stdIn.nextInt(), this.getNickname()));
                    break;

                case "G":
                case "GIVE":
                    if (!giveContainer(stdIn))
                        throw new InputMismatchException();
                    break;

                case "B":
                case "BUY":
                    BuyMessage buyMessage = new BuyMessage(stdIn.nextInt(), stdIn.nextInt(), this.getNickname());
                    send(buyMessage);
                    break;

                case "P":
                case "PRODUCE":
                    ArrayList<Integer> productionIDs = new ArrayList<>();

                    while(stdIn.hasNext()) {
                        String input = stdIn.next();
                        if(input.equalsIgnoreCase("DONE"))
                            break;

                        try{
                            productionIDs.add(Integer.parseInt(input));
                        }
                        catch(NumberFormatException e){
                            throw new InputMismatchException("\"'\" + input + \"' is not a valid input! Please type the ID of a Production Slot or 'STOP' when you are done selecting IDs\"");
                        }
                    }

                    if(InputCheck.duplicatedElement(productionIDs)) {
                        throw new InputMismatchException("You cannot insert the same Production Slot IDs multiple times!");
                    }

                    System.out.println(productionIDs.toString());

                    ProduceMessage produceMessage = new ProduceMessage(productionIDs, this.getNickname());
                    send(produceMessage);
                    break;

                case "F":
                case "FILL":
                    ArrayList<ResourceType> QMs = new ArrayList<>();

                    while(stdIn.hasNext()) {
                        String input = stdIn.next();

                        if(input.equalsIgnoreCase("DONE"))
                            break;

                        ResourceType questionMarkType = InputCheck.resourceType_null(input);

                        if (questionMarkType == null) {
                            QMs.clear();
                            throw new InputMismatchException("'" + input + "' is not a valid input! Please type a valid ResourceType or 'STOP' when you are done selecting IDs");
                        }
                        else if(!questionMarkType.canAddToVault()) {
                            QMs.clear();
                            throw new InputMismatchException("ResourceType '" + input + "' cannot be selected for production! Please type a valid ResourceType or 'STOP' when you are done selecting IDs");
                        }
                        else
                            QMs.add(questionMarkType);
                    }

                    /*ResourceType questionMarkType = InputCheck.resourceType_null(stdIn.next());
                    if (questionMarkType == null) break;*/
                    System.out.println(QMs.toString());

                    if(!QMs.isEmpty()) {
                        ResourceTypeSend resourceTypeSend = new ResourceTypeSend(Command.FILL_QM, QMs, this.getNickname());
                        send(resourceTypeSend);
                    }

                    break;

                case "M":
                case "MARKET":
                    String selection = stdIn.next(); //MUST BE ROW OR COLUMN
                    int num = stdIn.nextInt();

                    if(InputCheck.not_row_or_column(selection))
                        throw new InputMismatchException("Invalid syntax");

                    MarketMessage marketMessage = new MarketMessage(selection, num, this.getNickname());
                    send(marketMessage);
                    break;

                case "C":
                case "CONVERT":
                    ResourceType conversionType = InputCheck.resourceType_null(stdIn.next());
                    if (conversionType == null) throw new InputMismatchException("Invalid resourceType");

                    ResourceTypeSend convTypeSend = new ResourceTypeSend(Command.CONVERSION, conversionType, this.getNickname());
                    send(convTypeSend);
                    break;

                case "D":
                case "DISCARD":
                    send(new IdMessage(Command.DISCARD_LEADER, stdIn.nextInt(), this.getNickname()));
                    break;

                case "A":
                case "ACTIVATE":
                    send(new IdMessage(Command.ACTIVATE_LEADER, stdIn.nextInt(), this.getNickname()));
                    break;

                case "MV":
                case "MOVE":
                    int qty = stdIn.nextInt();
                    int sourceId = stdIn.nextInt();
                    String to = stdIn.next();
                    if (InputCheck.not_to(to))
                        throw new InputMismatchException("Invalid syntax");

                    int destId = stdIn.nextInt();

                    send(new ManageDepositMessage(qty, sourceId, destId, this.getNickname()));
                    break;

                case "SW":
                case "SWITCH":
                    int source = stdIn.nextInt();
                    String with = stdIn.next();
                    if (InputCheck.not_with(with))
                        throw new InputMismatchException("Invalid syntax");

                    int destin = stdIn.nextInt();
                    send(new SwitchDepositMessage(source, destin, this.getNickname()));
                    break;

                case "DONE":
                    send(new Message.MessageBuilder().setCommand(Command.DONE).setNickname(this.getNickname()).build());
                    break;

                case "SH":
                case "SHOW_HAND":
                    printHand(null, "");
                    break;

                case "SD":
                case "SHOW_DEPOSIT":
                    send(new Message.MessageBuilder().setCommand(Command.SHOW_DEPOSIT).setNickname(this.getNickname()).build());
                    break;

                case "SV":
                case "SHOW_VAULT":
                    send(new Message.MessageBuilder().setCommand(Command.SHOW_VAULT).setNickname(this.getNickname()).build());
                    break;

                case "SP":
                case "SHOW_PRODUCTION":
                    send(new Message.MessageBuilder().setCommand(Command.SHOW_PRODUCTION).setNickname(this.getNickname()).build());
                    break;

                case "SM":
                case "SHOW_MARKET":
                    send(new Message.MessageBuilder().setCommand(Command.SHOW_MARKET).setNickname(this.getNickname()).build());
                    break;

                case "SC":
                case "SHOW_CARDGRID":
                    printCardGrid();
                    break;

                case "SF":
                case "SHOW_FAITHPATH":
                    printFaithPath();
                    break;

                case "ET":
                case "END_TURN":
                    send(new Message.MessageBuilder().setCommand(Command.END_TURN).setNickname(this.getNickname()).build());
                    break;

                case "QUIT":
                    send(new Message.MessageBuilder().setCommand(Command.QUIT).setNickname(this.getNickname()).build());
                    return false;

                case "BARBAGIALLA":
                case "CHEAT":           //CHEAT COMMAND
                    send(new Message.MessageBuilder().setCommand(Command.CHEAT_VAULT).setNickname(getNickname()).build());
                    break;

                default:
                    stdIn.nextLine();
                    System.out.println("Invalid command, type " + Color.ANSI_RED.escape() + "HELP" + Color.RESET + " to see all available commands");
            }

        }catch (InputMismatchException e){
            stdIn.nextLine();
            if (e.getMessage()!=null) System.out.println(e.getMessage()+"\n");
            System.out.println("The command you submitted has some wrong parameters, please consult " + Color.ANSI_YELLOW.escape() + "HELP" + Color.RESET + " to know more about commands"+"\n");
        }

        //System.out.println("#Wrote:" + userInput);
        return true;
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

        if(destination.equalsIgnoreCase("DEPOSIT")){
            int destinationID = stdIn.nextInt();

            ResourceContainer container = new ResourceContainer(resType, qty);
            sendContainer = new SendContainer(Command.SEND_CONTAINER, container, destination, destinationID, this.getNickname());

        }else{ //VAULT
            ResourceContainer container = new ResourceContainer(resType, qty);
            sendContainer = new SendContainer(container, destination, this.getNickname());

        }

        send(sendContainer);
        return true;
    }

    @Override
    public void onDisconnect(User user) {
    }
    //------------------------------------------------------------------------------------------------------------------


    //PRINTS AND NOTIFIES OF THE VIEW ----------------------------------------------------------------------------------
    public void printHelp(){
        System.out.println(
                "#_HELP SECTION_#" + "\n" + "\n" +
                "Syntax: "+"\n"+
                "[Short Command] COMMAND parameter1 parameter2 ecc.." + "\n" + "\n" +
                "##############################[NETWORK]##############################" + "\n" +
                "--------GLOBAL COMMANDS (can be used everywhere)---------" + "\n" +
                "> [Q] QUIT " + "\n" +
                "> CHAT receiver_nickname message" + "\n" +
                "> CHAT_ALL message" + "\n" +

                "\n" + "--------LOBBY MANAGER COMMANDS---------" + "\n" +
                "> [J] JOIN lobby_name" + "\n" +
                "> [CR] CREATE lobby_name max_num_of_players" + "\n" +
                "> [R] REFRESH" + "\n" +

                "\n" + "--------LOBBY COMMANDS---------" + "\n" +
                "> [EL] EXIT_LOBBY " + "\n" +
                "> [SPL] PLAYER_LIST " + "\n" +
                "> [SG] START_GAME " + "\n" +

                "\n" +"\n" + "##############################[GAME]##############################" +
                "\n" + "--------SET_UP PHASE COMMANDS---------" + "\n" +
                "> [D] DISCARD leaderID"+ "\n" +
                "> [S] SELECT ResourceType 'DEPOSIT' depositID"+ "\n" +

                "\n" + "--------TURN_PHASE COMMANDS---------" + "\n" +
                "> [B] BUY DevelopmentCardID ProductionSlotID " + "\n" +
                "> [G] GIVE Qty ResourceType 'FROM' ('DEPOSIT' depositID) or ('VAULT') " + "\n" +
                "> DONE" + "\n" + "\n" +

                "> [M] MARKET 'Row' or 'Column' number" + "\n" +
                "> [D] PUT ResourceType 'IN DEPOSIT' depositID" + "\n" +
                "> [C] CONVERSION ResourceType" + "\n" + "\n" +

                "> [P] PRODUCE productionID1 productionID2  ...  'DONE' "+ "\n" +
                "> [F] FILL ResourceType1 ResourceType2  ...  'DONE' " + "\n" +
                "> [G] GIVE Qty ResourceType 'FROM' ('DEPOSIT' depositID) or ('VAULT') " + "\n" +
                "> DONE" + "\n" + "\n" +

                "> [A] ACTIVATE leaderID"+ "\n" + "\n" +

                "> [MV] MOVE Qty Source_DepositID 'TO' Destination_DepositID"+ "\n" + "\n" +
                "> [SW] SWITCH Source_DepositID 'WITH' Destination_DepositID"+ "\n" + "\n" +
                "> [ET] END_TURN" + "\n" +

                "\n" + "--------SHOW COMMANDS---------" + "\n" +

                "> [SM] SHOW_MARKET" + "\n" +
                "> [SC] SHOW_CARDGRID" + "\n" +
                "> [SF] SHOW_FAITHPATH" + "\n" +

                "> [SB] SHOW_BOARD" + "\n" +
                "> [SD] SHOW_DEPOSIT" + "\n" +
                "> [SV] SHOW_VAULT" + "\n" +
                "> [SP] SHOW_PRODUCTION" + "\n" +

                "> [SH] SHOW_HAND" + "\n" +

                "> [SP] SHOW_PLAYER nickname" + "\n"

                );
    }


    //GENERIC PRINTS----------------------------------------------------------------------------------------------------
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

    //GAME PRINTS-------------------------------------------------------------------------------------------------------
    @Override
    public void printItsYourTurn(String nickname){
        printReply_uni("It is your turn, chose an action: " + "" +
                "\n1) BUY A CARD (>BUY DevelopmentCardID ProductionSlotID) " +
                "\n2) SELECT FROM MARKET (>MARKET Row||Column number)" +
                "\n3) PRODUCE (>PRODUCE productionID1 productionID2 ... 'DONE')"+
                "\n4) ACTIVATE LEADER (>ACTIVATE leaderID)"+
                "\n5) MANAGE DEPOSIT (>MOVE Qty Source_DepositID TO Destination_DepositID, SWITCH DepositID1 'WITH' DepositID2)"+
                "\n6) END TURN (>END_TURN)" +
                "\n7) SHOW (>SHOW_objectToShow)" +
                "\nType HELP to see the full command list ", nickname);
    }

    //HAND AND LEADERS PRINT--------------------------------------------------------------------------------------------
    @Override
    public void printHand(ArrayList<Integer> leaderIDs, String nickname) {
        if (!isInGame) return;
        System.out.println(getHand().toString());
    }

    @Override
    public void printDeposit(Deposit deposit, String depositInfo) {
    }

    public void printCardGrid(){
        if (!isInGame) return;
        System.out.println(getLiteCardGrid().toString());
        getLiteCardGrid().printGridIDs();
    }

    public void printFaithPath(){
        if (!isInGame) return;
        System.out.println(getLiteFaithPath().toString());
    }


    @Override
    public void printOrder(ArrayList<String> randomOrder) {

    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void askForResources(String nickname, int qty) {

    }

    @Override
    public void askForLeaderCardID(String nickname) {

    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void notifyBoughtCard(String nickname) {
        if (!nickname.equals(getNickname()))
            System.out.println(nickname + " bought a new card!");
        else
            printReply_uni("You bought the card correctly!", nickname);
    }

    @Override
    public void notifyCurrentPlayerIncrease(int faithpoints, String nickname) {
        getLiteFaithPath().incrementPosition(faithpoints, nickname);

        if (!nickname.equals(getNickname()))
            System.out.println(nickname + "'s position has been incremented by " + faithpoints + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape());
        else
            System.out.println("Your current position has been incremented by " + faithpoints + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape());
    }

    @Override
    public void notifyOthersIncrease(int faithpoints, String nickname) {
        getLiteFaithPath().incrementOthersPositions(faithpoints, nickname);

        if (!nickname.equals(getNickname()))
            System.out.println(nickname+ " has discarded " + faithpoints+ " resources\nYour current position has been incremented by " + faithpoints + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape());

        System.out.println("Everybody's position (except "+ nickname+") has been incremented by " + faithpoints + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape());
    }

    @Override
    public void notifyPapalFavour(ArrayList<Integer> playerFavours, String senderNick) {
        getLiteFaithPath().incrementPlayerFavours(playerFavours);
        StringBuilder playersThatGotThePoints = new StringBuilder();
        playersThatGotThePoints.append("A papal favour has been activated: ");
        int i=0;
        int eligiblePlayer=0;
        int point = 0 ;
        for (String nick: getLiteFaithPath().getNicknames()) {
            if(playerFavours.get(i)!=0){
                playersThatGotThePoints.append(nick).append(" ");
                point = playerFavours.get(i);
                eligiblePlayer++;
            }
            i++;
        }

        if (eligiblePlayer>1)
            playersThatGotThePoints.append("were ");
        else
            playersThatGotThePoints.append("was ");

        playersThatGotThePoints.append("eligible and received ").append(point).append(Color.ANSI_YELLOW.escape()).append(" VICTORY POINTS").append(Color.ANSI_RESET.escape());
        System.out.println(playersThatGotThePoints);
    }

    @Override
    public void notifyGameSetup(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames) {
        setLiteCardGrid(new LiteCardGrid(cardGridIDs,getDevelopmentCards()));
        getLiteFaithPath().reset(nicknames); // Should i be creating a new one each time through parsing?
        isInGame = true;
    }

    @Override
    public void notifyCardGridChanges(int oldID, int newID) {
        getLiteCardGrid().gridUpdated(oldID, newID);
    }

    @Override
    public void notifyLeaderDiscarded(int id, String nickname){
        System.out.println("Leader discarded!\n");
        getHand().discardFromHand(id);
        printHand(null, "");
    }

    @Override
    public void notifyLeaderActivated(int id, String nickname){
        System.out.println("Leader activated\n");
        getHand().activateLeader(id);
    }

    @Override
    public void notifyCardRemoved(int amount, Colour color, int level) {
        printReply("LORENZO has removed "+ amount+ " "+color+ " development cards with level = " + level);
    }

    @Override
    public void notifyVaultAdd(ResourceContainer added) {

    }

    @Override
    public void notifyVaultRemove(ResourceContainer removed) {

    }

    @Override
    public void notifyLastTurn() {
        printReply("# THIS IS THE LAST TURN");
    }

    @Override
    public void notifyWinner(ArrayList<String> winner) {
        printReply("[#-_- "+Color.ANSI_YELLOW.escape()+"Winner"+Color.ANSI_RESET.escape()+":"+ winner.toString() +" -_-#");
    }

    @Override
    public void notifyScores(List<Integer> playersTotalVictoryPoints) {

    }

    @Override
    public void notifyGameEnded() {
        printReply("# The game is ended, you are now in the lobby");
        isInGame= false;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
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