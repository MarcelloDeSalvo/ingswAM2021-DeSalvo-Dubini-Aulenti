package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LobbyPanel extends BackgroundImagePanel {

    private final Gui gui;
    private final JPanel lobbies;

    /**
     * Creates the panel used to display the current lobbies
     */
    public LobbyPanel(Gui gui,List<LobbyListMessage.LobbyInfo> lobbyInfos ) {
        super("/images/backgrounds/lobbyBackground.png", -688, 5, false);

        this.gui = gui;
        this.setLayout(new BorderLayout());

        //lobbies = new TableWithButtons();
        lobbies = new JPanel();
        lobbies.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        lobbies.setBackground(new Color(255, 255, 255));

        showLobbyInfos(lobbyInfos);

        // SCROLLABLE LOBBIES------------------------------------------------------------------
        //jPanel_lobbies.setOpaque(false);
        JScrollPane jScrollable_lobbies = new JScrollPane(lobbies, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollable_lobbies.setBorder(BorderFactory.createEmptyBorder(100,100,50,100));
        jScrollable_lobbies.setOpaque(false);
        jScrollable_lobbies.getViewport().setOpaque(false);

        this.add(jScrollable_lobbies, BorderLayout.CENTER);
        //this.add(lobbies, BorderLayout.CENTER);

        // BOTTOM PANEL -----------------------------------------------------------------------
        JPanel final_panel = new JPanel();
        final_panel.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));
        final_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 50,10));

        ButtonImage refreshButton = new ButtonImage("/images/buttons/B_Refresh.png");
        refreshButton.addActionListener(e -> gui.send(new Message.MessageBuilder().setCommand(Command.LOBBY_LIST).build()));

        ButtonImage createButton = new ButtonImage("/images/buttons/B_Create.png");
        createButton.addActionListener(e -> createLobbyWindow());

        final_panel.add(refreshButton);
        final_panel.add(createButton);
        final_panel.setOpaque(false);

        this.add(final_panel, BorderLayout.PAGE_END);
    }

    public void showLobbyInfos(List<LobbyListMessage.LobbyInfo> lobbyInfos){
        if (lobbyInfos.isEmpty()){
            JLabel label = new JLabel("NO LOBBIES AVAILABLE", JLabel.CENTER);
            label.setFont(new Font("Helvetica", Font.PLAIN, 30));
            label.setOpaque(false);

            lobbies.add(label);
        }
        else {
            lobbies.setLayout(new GridLayout(0 , 5));

            lobbies.add(new JLabel("Lobby Name", JLabel.CENTER));
            lobbies.add(new JLabel("Owner", JLabel.CENTER));
            lobbies.add(new JLabel("Num of Players", JLabel.CENTER));
            lobbies.add(new JLabel("Full", JLabel.CENTER));
            lobbies.add(new JLabel("Closed", JLabel.CENTER));
        }

        for (LobbyListMessage.LobbyInfo lobby : lobbyInfos) {
            ButtonImage lobby_button = new ButtonImage(lobby.getLobbyName(), true);
            lobby_button.addActionListener(e -> gui.send(new JoinLobbyMessage(lobby.getLobbyName(),gui.getNickname())));
            lobbies.add(lobby_button);

            lobbies.add(new JLabel(lobby.getOwner(), JLabel.CENTER));
            lobbies.add(new JLabel(lobby.getNumOfPlayersConnected() + " / " + lobby.getMaxPlayers(), JLabel.CENTER));
            lobbies.add(new JLabel(String.valueOf(lobby.isFull()), JLabel.CENTER));
            lobbies.add(new JLabel(String.valueOf(lobby.isClosed()), JLabel.CENTER));
        }
    }

    private void createLobbyWindow() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(0, 2, 2, 2));

        pane.add(new JLabel("Lobby name"));
        JTextField lobbyName = new JTextField(5);
        pane.add(lobbyName);

        pane.add(new JLabel("Number of players"));
        JComboBox<String> menu = new JComboBox<>(new String[] {"1", "2", "3", "4"});
        pane.add(menu);

        int option = JOptionPane.showConfirmDialog(this, pane, "CREATE LOBBY", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);

        if(option == JOptionPane.OK_OPTION){
            if (lobbyName.getText().length()==0)
                gui.printReply("Please select a non empty name");
            else
                gui.send(new CreateLobbyMessage(lobbyName.getText(), Integer.parseInt(menu.getSelectedItem().toString()), gui.getNickname()));
        }
    }
}
