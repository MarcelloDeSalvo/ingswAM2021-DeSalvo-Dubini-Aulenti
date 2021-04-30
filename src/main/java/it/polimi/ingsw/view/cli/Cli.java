package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.MoveMessage;
import it.polimi.ingsw.observers.ObserverViewIO;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Scanner;

public class Cli extends View {
    ArrayList<ObserverViewIO> observers = new ArrayList<>();

    @Override
    public void update(Message mex){
        Command command = mex.getCommand();
        switch (command){
            case REPLY:
                System.out.println(mex.toString());
                break;

            case MOVE:
                System.out.println(mex.toString());
                break;
        }

    }


    @Override
    public boolean readInput() {
        String userInput;
        Scanner stdIn = new Scanner(System.in);
        userInput = stdIn.next();

        switch (userInput.toUpperCase()){
            case "MOVE":
                askMove();
                break;
            case "INCREASE":
                increasePos();
                break;
            case "HELLO":
                notifyIO(new Message(Command.HELLO));
                break;
            case "QUIT":
                notifyIO(new Message(Command.QUIT));
                return false;
        }

        return true;

    }

    @Override
    public void askMove() {
        Scanner in = new Scanner(System.in);
        System.out.println("Where do you want to move? Insert the command [ Move: x,y,string ] ");
        Message moveMex = new MoveMessage(in.nextInt(), in.nextInt(), in.next());
        //System.out.println(moveMex);

        notifyIO(moveMex);
    }

    @Override
    public void increasePos() {
        Message increaseMex = new Message(Command.INCREASE, "");
        notifyIO(increaseMex);
    }

    @Override
    public void addObserverIO(ObserverViewIO observer) {
        if(observer!=null)
            observers.add(observer);
    }

    @Override
    public void notifyIO(Message message) {
        for (ObserverViewIO obs: observers) {
            obs.update(message);
        }
    }

}