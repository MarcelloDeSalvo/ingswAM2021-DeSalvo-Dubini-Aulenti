package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.Target;
import it.polimi.ingsw.view.ClientView;

import java.util.Scanner;

public class Cli extends ClientView {
    ClientSender sender;

    @Override
    public void readUpdates(Message mex){
        Command command = mex.getCommand();
        switch (command){
            case HELLO:
                printHello();
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

        switch (userInput.toUpperCase()){
            case "HELLO":
                sender.send(new Message(Command.HELLO));
                break;

            case "HELLO_ALL":
                sender.send(new Message(Command.HELLO_ALL));
                break;

            case "QUIT":
                sender.send(new Message(Command.QUIT));
                return false;
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
}