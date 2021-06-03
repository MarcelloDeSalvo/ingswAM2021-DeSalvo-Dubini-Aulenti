package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.liteModel.*;
import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.ProductionAbility;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.view.ClientView;

import java.io.FileNotFoundException;
import java.util.*;

public class Cli extends ClientView {

    private String nicknameTemp = null;
    private final Scanner stdIn;

    public Cli() throws FileNotFoundException {
        super();
        stdIn = new Scanner(System.in);
    }

    //USER INPUT AND UPDATES--------------------------------------------------------------------------------------------
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
                    produce();
                    break;

                case "F":
                case "FILL":
                    fillQMs();
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

                case "SB":
                case "SHOW_BOARD":
                    printBoard(getNickname());
                    break;

                case "SH":
                case "SHOW_HAND":
                    printHand();
                    break;

                case "SD":
                case "SHOW_DEPOSIT":
                    printDeposit();
                    break;

                case "SV":
                case "SHOW_VAULT":
                    printVault();
                    break;

                case "SP":
                case "SHOW_PRODUCTION":
                    printProduction();
                    break;

                case "SM":
                case "SHOW_MARKET":
                    printMarket();
                    break;

                case "SC":
                case "SHOW_CARDGRID":
                    printCardGrid();
                    break;

                case "SF":
                case "SHOW_FAITHPATH":
                    printFaithPath();
                    break;

                case "SO":
                case "SHOW_ORDER":
                    printOrder();
                    break;

                case "PL":
                case "PLAYER_LIST":
                    send(new Message.MessageBuilder().setCommand(Command.PLAYER_LIST).setNickname(this.getNickname()).build());
                    break;

                case "SPL":
                case "SHOW_PLAYER":
                    String pl = stdIn.next();
                    printBoard(pl);
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

    /**
     * Handles produce command
     */
    private void produce() {
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

        ProduceMessage produceMessage = new ProduceMessage(productionIDs, this.getNickname());
        send(produceMessage);
    }

    /**
     * Handles fill command
     */
    private void fillQMs() {
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

        if(!QMs.isEmpty()) {
            ResourceTypeSend resourceTypeSend = new ResourceTypeSend(Command.FILL_QM, QMs, this.getNickname());
            send(resourceTypeSend);
        }
    }
    //------------------------------------------------------------------------------------------------------------------


    //OnEvent-----------------------------------------------------------------------------------------------------------
    @Override
    public void onDisconnect(User user) {
    }

    @Override
    public void onReconnected(){
    }

    @Override
    public void onLogin(String info) {
        System.out.println(info);
        this.setNickname(nicknameTemp);
    }

    @Override
    public void onExitLobby() {
    }

    @Override
    public void onJoinLobby() {
    }
    //------------------------------------------------------------------------------------------------------------------


    //GENERIC PRINTS----------------------------------------------------------------------------------------------------
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
                        "> [PL] PLAYER_LIST " + "\n" +
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

                        "> [SPL] SHOW_PLAYER nickname" + "\n"

        );
    }

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
    public void printWaitingRoom(StringsMessage stringsMessage) {
        ArrayList<String> playerList = stringsMessage.getData();
        String info = stringsMessage.getInfo();
        System.out.println();
        System.out.println(info);
        for (String name: playerList) {
            System.out.println("- " + name);
        }
        System.out.println("\n");
    }

    @Override
    public void printChatMessage(String senderNick, String info, boolean all) {
        if (!all)
            System.out.print(Color.ANSI_BLUE.escape() + senderNick + Color.RESET + " whispers you: ");
        else
            System.out.print(Color.ANSI_PURPLE.escape() + senderNick + " in ALL chat:" + Color.RESET);
        System.out.println(info);
    }

    @Override
    public void printLobby(ArrayList<LobbyListMessage.LobbyInfo> lobbyInfos) {
        System.out.println("\n");
        System.out.println(Color.ANSI_BLUE.escape() + "[LOBBIES]:" + Color.RESET);

        if (lobbyInfos.isEmpty()){
            System.out.println("There are currently 0 active lobbies"+"\n");
            return;
        }

        for (LobbyListMessage.LobbyInfo info : lobbyInfos) {
            String coloredFull, coloredClosed, available = Color.ANSI_GREEN.escape();
            if(info.isFull() ) {
                coloredFull = Color.ANSI_RED.escape();
                available = Color.ANSI_RED.escape();
            }
            else
                coloredFull = Color.ANSI_GREEN.escape();
            if(info.isClosed() ) {
                coloredClosed = Color.ANSI_RED.escape();
                available = Color.ANSI_RED.escape();
            }
            else
                coloredClosed = Color.ANSI_GREEN.escape();


            System.out.println( available+"\u06DD Lobby " + Color.ANSI_RESET.escape() + info.getLobbyName() +
                    ", connected=" + info.getNumOfPlayersConnected() + "/" + info.getMaxPlayers() +
                    ", Owner=" + info.getOwner() +
                    ","+coloredFull+" isFull=" + info.isFull() +Color.ANSI_RESET.escape()+
                    ","+coloredClosed+" isClosed=" +  info.isClosed() +Color.ANSI_RESET.escape());
        }

        System.out.println();
        System.out.println();
    }
    //------------------------------------------------------------------------------------------------------------------



    //GAME PRINTS-------------------------------------------------------------------------------------------------------
    @Override
    public void printItsYourTurn(String nickname){
        printReply_uni("\nIt is your turn, choose an action: " + "" +
                "\n1) BUY A CARD (>BUY DevelopmentCardID ProductionSlotID) " +
                "\n2) SELECT FROM MARKET (>MARKET Row||Column number)" +
                "\n3) PRODUCE (>PRODUCE productionID1 productionID2 ... 'DONE')"+
                "\n4) ACTIVATE LEADER (>ACTIVATE leaderID)"+
                "\n5) MANAGE DEPOSIT (>MOVE Qty Source_DepositID TO Destination_DepositID, SWITCH DepositID1 'WITH' DepositID2)"+
                "\n6) END TURN (>END_TURN)" +
                "\n7) SHOW (>SHOW_objectToShow)" +
                "\nType HELP to see the full command list ", nickname);
    }

    @Override
    public void printTurnHelp(String nickname) {
        printItsYourTurn(nickname);
    }

    @Override
    public void printPlayerList(String info, ArrayList<String> names) {
        System.out.println(info);
        for(String name: names)
            System.out.println(" - " + name);

        System.out.println();
    }

    @Override
    public void printOrder() {
        if (!this.isInGame()) return;

        ArrayList<String> randomOrder = getLiteFaithPath().getNicknames();

        StringBuilder orderBuild = new StringBuilder();
        orderBuild.append("This is the Turn Order: \n");

        for (int i = 0; i<randomOrder.size(); i++){
            orderBuild.append(i+1).append(": ").append(randomOrder.get(i)).append(" \n");
        }

        System.out.println(orderBuild.toString());
    }
    //------------------------------------------------------------------------------------------------------------------


    //LITE MODEL PRINT--------------------------------------------------------------------------------------------------
    public void printHand() {
        if (!this.isInGame()) return;
        System.out.println(getMyHand().toString(getNickname()));
    }

    public void printVault(){
        if (!this.isInGame()) return;
        System.out.println(getMyLiteVault().toString());
    }

    public void printCardGrid(){
        if (!this.isInGame()) return;
        System.out.println(getLiteCardGrid().toString());
        getLiteCardGrid().printGridIDs();
    }

    public void printFaithPath(){
        if (!this.isInGame()) return;
        System.out.println(getLiteFaithPath().toString());
    }

    public void printDeposit(){
        if (!this.isInGame()) return;
        System.out.println(getMyLiteDeposit().toString());
    }

    public void printProduction(){
        if (!this.isInGame()) return;
        System.out.println(getMyLiteProduction().toString());
    }

    public void printMarket(){
        if (!this.isInGame()) return;
        System.out.println(getLiteMarket().toString());
    }

    public void printBoard(String nickname){
        if (!this.isInGame()) return;

        if(!getLiteFaithPath().getNicknames().contains(nickname)){
            System.out.println("Invalid nickname!");
            return;
        }

        System.out.println(getLitePlayerBoard(nickname).toString(nickname));
    }
    //------------------------------------------------------------------------------------------------------------------


    //ASK---------------------------------------------------------------------------------------------------------------
    @Override
    public void askForResources(String nickname, int qty) {

    }

    @Override
    public void askForLeaderCardID(String nickname) { }

    @Override
    public void askForMarketDestination(ArrayList<ResourceContainer> containers, String nickname) {
        printDeposit();
    }
    //------------------------------------------------------------------------------------------------------------------


    //UPDATES FROM THE SERVER-------------------------------------------------------------------------------------------
    @Override
    public void notifyGameIsStarted() {
        System.out.println("---THE GAME HAS BEEN STARTED---\n\t--HAVE FUN--");
    }

    @Override
    public void notifyGameSetup(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames, ArrayList<ResourceContainer> marketSetUp) {
        setLiteCardGrid(new LiteCardGrid(cardGridIDs,getDevelopmentCards()));
        litePlayerBoardsSetUp(nicknames);
        setLiteMarket(new LiteMarket(marketSetUp));
        getLiteFaithPath().reset(nicknames); // Should i be creating a new one each time through parsing?

        this.setInGame(true);
        printOrder();
    }

    @Override
    public void notifyCurrentPlayerIncrease(int faithPoints, String nickname) {
        getLiteFaithPath().incrementPosition(faithPoints, nickname);

        if (!nickname.equals(getNickname()))
            System.out.println(nickname + "'s position has been incremented by " + faithPoints + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape());
        else
            System.out.println("Your current position has been incremented by " + faithPoints + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape());

        System.out.println();
    }

    @Override
    public void notifyOthersIncrease(int faithPoints, String nickname) {
        getLiteFaithPath().incrementOthersPositions(faithPoints, nickname);

        if (!nickname.equals(getNickname()))
            System.out.println(nickname+ " has discarded " + faithPoints+ " resources\nYour current position has been incremented by " + faithPoints + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape());

        System.out.println("Everybody's position (except "+ nickname+") has been incremented by " + faithPoints + Color.ANSI_RED.escape() + " FAITH POINT" + Color.ANSI_RESET.escape());
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
    public void notifyBuyOk(String nickname, int slotID, int cardID) {
        getSomeonesLiteProduction(nickname).addCardToSlot(slotID, cardID);
        if (!nickname.equals(getNickname()))
            printReply(nickname + " bought a new card (ID: "+ cardID +" ) !");
        else {
            printReply("You bought the card correctly!");
            printProduction();
        }
    }

    @Override
    public void notifyBuySlotOk(String mex) {
        printReply(mex);
        System.out.println();
    }

    @Override
    public void notifyBuyError(String error) {
        printReply("\n" + error);
    }

    @Override
    public void notifyProductionOk(String senderNick) {
        if (!senderNick.equals(getNickname()))
            printReply(senderNick+" has used the production this turn!");
        else{
            printReply("Production executed correctly!");
            printVault();
        }
    }

    @Override
    public void notifyProductionError(String error, String senderNick) {
        printReply("\n" + error);
    }

    @Override
    public void notifyStartFilling(int productionID, String senderNick) {
        printReply("Please start filling the Production Slots N: " + productionID +
        " with resources of your choice by typing >FILL ResourceType1 ResourceType2  ... 'DONE'");
    }

    @Override
    public void notifyFillOk(int productionID, String senderNick) {
        printReply("Resources of your choice for Production Slot N: " + productionID + " have been filled correctly!");
    }

    @Override
    public void notifyProductionPrice(ArrayList<ResourceContainer> resourcesPrice, String senderNick) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;

        stringBuilder.append("The ProductionSlots you selected requires: ");

        for (ResourceContainer container: resourcesPrice) {
            stringBuilder.append(container.getQty()).append(" ").append(container.getResourceType());

            i++;

            if(i < resourcesPrice.size())
                stringBuilder.append(" + ");
        }
        stringBuilder.append("\nPlease select resources as a payment by typing > GIVE Qty ResourceType 'FROM' ('DEPOSIT' DepositID) or ('VAULT') ");

        printReply(stringBuilder.toString());
    }

    @Override
    public void notifyMoveOk(String senderNick) {
        printDeposit();
        printReply("The action on deposit has been executed correctly!");
    }

    @Override
    public void notifyMarketUpdate(String selection, int selected) {
        getLiteMarket().liteMarketUpdate(selection, selected);
    }

    @Override
    public void notifyResourcesArrived(ArrayList<ResourceContainer> resourceContainers) {
        StringBuilder marketOutChoice = new StringBuilder("Now select where do you want to place them by typing >PUT ResourceType 'IN deposit' deposit_id").append("\n");
        marketOutChoice.append("Where do you want to put: ");
        for (ResourceContainer res: resourceContainers) {
            marketOutChoice.append(res.getResourceType()).append("  ");
        }
        printReply(marketOutChoice.toString());
    }

    @Override
    public void notifyMarketOk(String senderNick) {
        printReply("The market operation ended successfully! \n");
        printTurnHelp(senderNick);
    }

    @Override
    public void notifyCardGridChanges(int oldID, int newID) {
        getLiteCardGrid().gridUpdated(oldID, newID);
    }

    @Override
    public void notifyCardsInHand(ArrayList<Integer> leaderIDs, String nickname) {
        setMyHand(new LiteHand(leaderIDs, getLeaderCards()));
        printHand();
    }

    @Override
    public void notifyLeaderDiscarded(int id, String nickname){
        printReply("Leader discarded!");
        getMyHand().discardFromHand(id);
        printHand();
    }

    @Override
    public void notifyLeaderActivated(int id, String nickname){
        if(nickname.equals(getNickname()))
            printReply("Leader activated!");
        else {
            printReply(nickname + " has activated the " + id + " ID leader!");
            getSomeonesHand(nickname).addLeader(id);
        }
        getSomeonesHand(nickname).activateLeader(id);
    }

    @Override
    public void notifyLorenzoAction(int actionID, Colour colour) {
        if(actionID < 5)
            printReply("LORENZO has removed 2 " + colour + " development cards");
    }

    @Override
    public void notifyVaultChanges(ResourceContainer container, boolean added, String senderNick) {
        if (added)
            getSomeonesLiteVault(senderNick).addToVault(container);
        else
            getSomeonesLiteVault(senderNick).removeFromVault(container);
    }

    @Override
    public void notifyDepositChanges(int id, ResourceContainer resourceContainer, boolean added, String senderNick) {
        if (added)
            getSomeonesLiteDeposit(senderNick).addRes(resourceContainer, id);
        else
            getSomeonesLiteDeposit(senderNick).removeRes(resourceContainer, id);
    }

    @Override
    public void notifyNewDepositSlot(int maxDim, ResourceType resourceType, String senderNick) {
        getSomeonesLiteDeposit(senderNick).addSlot(maxDim, resourceType, null);
    }

    @Override
    public void notifyNewProductionSlot(ProductionAbility productionAbility, String senderNick) {
        getSomeonesLiteProduction(senderNick).addProductionSlot(productionAbility);
        if(senderNick.equals(getNickname())){
            printReply("You activated a new production!");
            printProduction();
        }
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
    public void notifyScores(List<Integer> playersTotalVictoryPoints, ArrayList<String> nicknames) {

    }

    @Override
    public void notifyGameEnded() {
        printReply("# The game is ended, you are now in the lobby");
        this.setInGame(false);
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