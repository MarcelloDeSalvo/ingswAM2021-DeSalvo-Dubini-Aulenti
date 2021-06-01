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
        playerList.setBorder(BorderFactory.createEmptyBorder(200,50,200,100));
        playerList.setLayout(new GridLayout(4,1));

        playerList.setFont(new Font("Helvetica", Font.PLAIN, 34));

        lobbyOptions = new JPanel();
        lobbyOptions.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));
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

        playerList.add(new Label(info + "\n", JLabel.CENTER));
        for (String name: names) {
            playerList.add(new Label("- " + name, JLabel.CENTER));
        }

        playerList.validate();
        playerList.repaint();
    }
}
