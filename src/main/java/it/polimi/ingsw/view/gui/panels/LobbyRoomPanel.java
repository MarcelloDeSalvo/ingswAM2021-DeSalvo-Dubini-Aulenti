package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LobbyRoomPanel extends JPanel {

    private final Gui gui;
    private final JPanel playerList;
    private final JPanel lobbyOptions;

    public LobbyRoomPanel(Gui gui) {
        super();
        this.gui = gui;

        this.setLayout(new BorderLayout());

        playerList = new JPanel();
        playerList.setLayout(new GridLayout(6,1));
        playerList.setBorder(BorderFactory.createEmptyBorder(100,100,100,100));

        playerList.setFont(new Font("Helvetica", Font.PLAIN, 34));

        lobbyOptions = new JPanel();
        lobbyOptions.setBorder(BorderFactory.createEmptyBorder(60,100,50,100));
        GridLayout gridLayout = new GridLayout(0,2);
        gridLayout.setHgap(30);
        lobbyOptions.setLayout(gridLayout);

        ButtonImage startButton = new ButtonImage("START", true);
        lobbyOptions.add(startButton);
        startButton.addActionListener(e -> gui.send(new Message.MessageBuilder().setCommand(Command.START_GAME).setNickname(gui.getNickname()).build()));

        ButtonImage exitButton = new ButtonImage("EXIT",true);
        lobbyOptions.add(exitButton);
        exitButton.addActionListener(e ->
                gui.send(new Message.MessageBuilder().setCommand(Command.EXIT_LOBBY).setNickname(gui.getNickname()).build()));

        this.add(playerList, BorderLayout.CENTER);
        this.add(lobbyOptions, BorderLayout.PAGE_END);
    }

    public void printPlayerList(String info, ArrayList<String> names) {
        if (playerList != null)
            playerList.removeAll();

        JLabel infoLabel = new JLabel(info.toUpperCase() +"\n", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Rubik", Font.PLAIN, 42));
        infoLabel.setForeground(new Color(241, 153, 0));

        playerList.add(infoLabel);
        playerList.add(new JLabel());

        for (String name: names) {
            JLabel nameLabel = new JLabel("- " + name, SwingConstants.CENTER);
            nameLabel.setFont(new Font("Rubik", Font.PLAIN, 35));
            playerList.add(nameLabel);
        }

        playerList.validate();
        playerList.repaint();
    }
}
