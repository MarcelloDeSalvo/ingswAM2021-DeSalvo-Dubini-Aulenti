package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteCardGrid;
import it.polimi.ingsw.liteModel.LiteFaithPath;
import it.polimi.ingsw.liteModel.LiteMarket;
import it.polimi.ingsw.model.exceptions.ImageNotFound;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GamePanel extends JPanel {

    private final FaithPathPanel faithPathPanel;
    private final PlayerBoardPanel playerBoardPanel;
    private final CardGridPanel cardGridPanel;
    private final MarketPanel marketPanel;
    private final ArrayList<String> nicknames;
    private final HashMap<String,ShowPlayerPanel> playerPanelsMap;

    private final Gui gui;
    private final BackgroundImagePanel main;
    private final CardLayout cardLayout;
    private JPanel buttons;
    private JPanel topPanel;

    private JLabel notifyLabel;

    /**
     * Creates tha main panel in which the game takes place
     */
    public GamePanel(Gui gui, LiteFaithPath liteFaithPath, LiteCardGrid liteCardGrid, LiteMarket liteMarket) throws ImageNotFound {
        super();
        this.gui = gui;
        this.nicknames=new ArrayList<>(gui.getLiteFaithPath().getNicknames());
        this.setLayout(new BorderLayout());

        main = new BackgroundImagePanel("/images/backgrounds/daVinci.jpg",false);
        main.setHeight(1080);
        main.setWidth(1920);
        cardLayout = new CardLayout();
        main.setLayout(cardLayout);
        main.setOpaque(false);

        faithPathPanel = new FaithPathPanel(gui, liteFaithPath);
        faithPathPanel.setOpaque(false);

        playerBoardPanel = new PlayerBoardPanel(gui);
        playerBoardPanel.setOpaque(false);

        cardGridPanel = new CardGridPanel(gui, liteCardGrid);
        cardGridPanel.setOpaque(false);

        marketPanel=new MarketPanel(gui, liteMarket);
        marketPanel.setOpaque(false);

        playerPanelsMap=new HashMap<>();
        for (String nick:nicknames) {
            if(!nick.equals("LORENZO") && !nick.equals(gui.getNickname())){
                ShowPlayerPanel spp=new ShowPlayerPanel(gui,nick);
                playerPanelsMap.put(nick, spp);
                main.add(spp,nick+"Panel");
            }

        }


        main.add(playerBoardPanel,"playerBoardPanel");
        main.add(faithPathPanel,"faithPathPanel");
        main.add(cardGridPanel, "cardGridPanel");
        main.add(marketPanel, "marketPanel");

        cardLayout.show(main,"playerBoardPanel");

        buttonSection();
        topPanel();


        this.add(main, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);
        this.add(topPanel, BorderLayout.NORTH);
        this.setOpaque(false);

    }

    private void topPanel(){
        topPanel = new JPanel();
        notifyLabel= new JLabel("GAME UPDATES", JLabel.CENTER);
        notifyLabel.setFont(new Font("Rubik", Font.BOLD, 24));
        notifyLabel.setForeground(new Color(255,255,255));
        notifyLabel.setOpaque(false);
        notifyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(notifyLabel);
        topPanel.setBackground(new Color(219, 139, 0));
    }

    private void buttonSection() {

        buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        nicknames.remove(gui.getNickname());
        JComboBox<String> nickList = new JComboBox<>(nicknames.toArray(new String[0]));
        if (nicknames.get(0).equals("LORENZO")) {
            nickList.setVisible(false);

        }
        JButton showPlayer = new ButtonImage(" PLAYER ", 22, true);

        showPlayer.addActionListener(e -> cardLayout.show(main, nicknames.get(nickList.getSelectedIndex()) + "Panel"));

        JButton showFaithpath = new ButtonImage(" FAITHPATH ", 22, true);
        showFaithpath.addActionListener(e -> cardLayout.show(main, "faithPathPanel"));

        JButton showMarket = new ButtonImage(" MARKET ", 22, true);
        showMarket.addActionListener(e -> cardLayout.show(main, "marketPanel"));

        JButton show_my_board = new ButtonImage(" MY BOARD ", 22, true);
        show_my_board.addActionListener(e -> cardLayout.show(main, "playerBoardPanel"));

        JButton showCardGrid = new ButtonImage(" CARD GRID ", 22, true);
        showCardGrid.addActionListener(e -> cardLayout.show(main, "cardGridPanel"));

        JButton cheat = new ButtonImage(" CHEAT ", 22, true);
        cheat.addActionListener(
                e -> gui.send(new Message.MessageBuilder().setCommand(Command.CHEAT_VAULT).setNickname(gui.getNickname()).build()));

        JButton endTurn = new ButtonImage(" END TURN ", 22, true);
        endTurn.addActionListener(
                e -> gui.send(new Message.MessageBuilder().setCommand(Command.END_TURN).setNickname(gui.getNickname()).build()));


        buttons.setBackground(new Color(255, 235, 204));
        buttons.add(Box.createRigidArea(new Dimension(300, 30)));

        if (nicknames.get(0).equals("LORENZO")) {
            buttons.add(Box.createRigidArea(new Dimension(190, 30)));
        }
        else {
            buttons.add(nickList);
            buttons.add(Box.createRigidArea(new Dimension(20, 30)));
            buttons.add(showPlayer);
        }

        buttons.add(Box.createRigidArea(new Dimension(20,30)));
        buttons.add(show_my_board);
        buttons.add(Box.createRigidArea(new Dimension(20,30)));
        buttons.add(showFaithpath);
        buttons.add(Box.createRigidArea(new Dimension(20,30)));
        buttons.add(showMarket);
        buttons.add(Box.createRigidArea(new Dimension(20,30)));
        buttons.add(showCardGrid);
        buttons.add(Box.createRigidArea(new Dimension(20,30)));
        buttons.add(cheat);
        buttons.add(Box.createRigidArea(new Dimension(20,30)));
        buttons.add(endTurn);
        buttons.add(Box.createHorizontalGlue());
        buttons.setBorder(BorderFactory.createEmptyBorder(5,50,5,50));

    }

    public FaithPathPanel getFaithPathPanel() { return faithPathPanel; }

    public PlayerBoardPanel getPlayerBoardPanel() { return playerBoardPanel; }

    public JPanel getMain() { return main; }

    public CardLayout getCardLayout() { return cardLayout; }

    public JPanel getButtons() { return buttons; }

    public JLabel getNotifyLabel() { return notifyLabel; }

    public CardGridPanel getCardGridPanel() { return cardGridPanel; }

    public MarketPanel getMarketPanel() { return marketPanel; }

    public HashMap<String, ShowPlayerPanel> getPlayerPanelsMap() { return playerPanelsMap; }

    public HandPanel getOtherHandPanels(String nick){ return playerPanelsMap.get(nick).getHandPanel();}

    public ProductionPanel getOtherProductionPanel(String nick){ return  playerPanelsMap.get(nick).getProductionPanel();}
}
