package it.polimi.ingsw.view.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.parser.LeaderCardParser;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.view.ClientView;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cli extends ClientView {
    ClientSender sender;
    ArrayList<LeaderCard> leaderCards;

    public Cli() throws FileNotFoundException {
        leaderCards = LeaderCardParser.deserializeLeaderList();
    }

    @Override
    public void readUpdates(Message mex){
        Command command = mex.getCommand();
        Gson gson = new Gson();
        String original = mex.serialize();

        switch (command){
            case HELLO:
                printHello();
                break;

            case SHOW_HAND:
                ShowHandMessage showHandMessage = gson.fromJson(original, ShowHandMessage.class);
                printHand(showHandMessage.getCardsID());
                break;

            case REPLY:
                printReply(mex.getInfo());
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
                case "LOGIN":
                    String nickname = stdIn.next();
                    Message login = new Message(Command.LOGIN, nickname);
                    this.setNickname(nickname);
                    sender.send(login);
                    break;

                case "CREATE_LOBBY":
                    sender.send(new CreateLobbyMessage(stdIn.next(), stdIn.nextInt(), stdIn.nextBoolean(), this.getNickname()));
                    break;


                case "HELLO":
                    sender.send(new Message(Command.HELLO, "Hello", this.getNickname()));
                    break;

                case "HELLO_ALL":
                    sender.send(new Message(Command.HELLO_ALL, "Hello all", this.getNickname()));
                    break;

                case "DISCARD_LEADER":
                    sender.send(new DiscardLeaderMessage(stdIn.nextInt(), this.getNickname()));
                    break;

                case "QUIT":
                    sender.send(new Message(Command.QUIT));
                    return false;


                default:
                    System.out.println("Invalid command, type HELP to see all available commands");
            }
        }catch (InputMismatchException e){
            System.out.println("The command you submitted isn't valid, please consult HELP to know more about commands");
        }
        return true;

    }

    @Override
    public void printHello() {
        System.out.println("Hello!");
    }

    @Override
    public void printQuit() {
        System.out.println("Disconnected");
    }

    @Override
    public void printReply(String payload) {
        System.out.println(payload);
    }

    @Override
    public void setSender(ClientSender clientSender) {
        this.sender = clientSender;
    }


    @Override
    public void printHand(ArrayList<Integer> leaderIDs) {
        for (int id: leaderIDs)
            System.out.println(leaderCards.get(id));
    }


}