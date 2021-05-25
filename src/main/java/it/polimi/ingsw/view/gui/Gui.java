package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.model.cards.Colour;
import it.polimi.ingsw.model.cards.ProductionAbility;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.server.User;
import it.polimi.ingsw.view.ClientView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Gui extends ClientView {

    private int exit = 0;

    public Gui() throws FileNotFoundException{
        super();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public void createAndShowGUI(){

        JFrame frame = new JFrame("MASTER OF RENAISSANCE");
        frame.setResizable(false);

        //LOGIN PANEL------------------
        JPanel jPanel = new JPanel();

        JLabel label = new JLabel("WELCOME TO MASTER OF RENAISSANCE", JLabel.CENTER);
        jPanel.setBorder(BorderFactory.createEmptyBorder(300,300,300,300));
        jPanel.setLayout(new GridLayout(0,1));
        label.setOpaque(true);

        JTextField jTextField = new JTextField("Username..");
        jTextField.setBounds(50,100, 200,30);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send(new Message.MessageBuilder().setCommand(Command.LOGIN).setInfo(jTextField.getText()).build());
            }
        });

        jPanel.add(label);
        jPanel.add(jTextField);
        jPanel.add(loginButton);
        //------------------------------

        frame.add(jPanel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    @Override
    public void printHello() {

    }

    @Override
    public void printQuit(String nickname) {

    }

    @Override
    public void printReply(String payload) {

    }

    @Override
    public void printReply_uni(String payload, String nickname) {

    }

    @Override
    public void printReply_everyOneElse(String payload, String nickname) {

    }

    @Override
    public void printItsYourTurn(String nickname) {

    }

    @Override
    public void askForResources(String nickname, int qty) {

    }

    @Override
    public void askForLeaderCardID(String nickname) {

    }

    @Override
    public void askForMarketDestination(ArrayList<ResourceContainer> containers, String nickname) {

    }

    @Override
    public void notifyCardsInHand(ArrayList<Integer> leaderIDs, String nickname) {

    }

    @Override
    public void notifyBuyOk(String nickname, int slotID, int cardID) {

    }

    @Override
    public void notifyGameSetup(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames, ArrayList<ResourceContainer> marketSetUp) {

    }

    @Override
    public void notifyLeaderDiscarded(int id, String nickname) {

    }

    @Override
    public void notifyLeaderActivated(int id, String nickname) {

    }

    @Override
    public void notifyProductionOk(String senderNick) {

    }

    @Override
    public void notifyMoveOk(String senderNick) {

    }

    @Override
    public void notifyMarketUpdate(String selection, int selected) {

    }

    @Override
    public void notifyLastTurn() {

    }

    @Override
    public void notifyWinner(ArrayList<String> winners) {

    }

    @Override
    public void notifyScores(List<Integer> playersTotalVictoryPoints, ArrayList<String> nicknames) {

    }

    @Override
    public void notifyGameEnded() {

    }

    @Override
    public void onDisconnect(User user) {

    }

    @Override
    public void notifyCardRemoved(int amount, Colour color, int level) {

    }

    @Override
    public void notifyCardGridChanges(int oldID, int newID) {

    }

    @Override
    public void notifyNewDepositSlot(int maxDim, ResourceType resourceType, String senderNick) {

    }

    @Override
    public void notifyDepositChanges(int id, ResourceContainer resourceContainer, boolean added, String senderNick) {

    }

    @Override
    public void notifyCurrentPlayerIncrease(int faithpoints, String nickname) {

    }

    @Override
    public void notifyOthersIncrease(int faithpoints, String nickname) {

    }

    @Override
    public void notifyPapalFavour(ArrayList<Integer> playerFavours, String senderNick) {

    }

    @Override
    public void notifyNewProductionSlot(ProductionAbility productionAbility, String senderNick) {

    }

    @Override
    public void notifyVaultChanges(ResourceContainer container, boolean added, String senderNick) {

    }

    @Override
    public boolean readInput() {
        while (exit==0){
            //CASE
        }
        return false;
    }

    @Override
    public void readUpdates(String message) {

    }
}
