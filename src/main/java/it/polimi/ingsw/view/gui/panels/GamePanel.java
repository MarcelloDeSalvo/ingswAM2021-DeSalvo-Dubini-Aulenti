package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteFaithPath;
import it.polimi.ingsw.view.gui.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel {

    private final FaithPathPanel faithPathPanel;

    private JPanel main;
    private CardLayout cardLayout ;
    private JPanel buttons ;
    private JPanel topPanel ;

    public GamePanel(Gui gui, LiteFaithPath liteFaithPath) {
        super();
        this.setLayout(new BorderLayout(10,10));

        main = new JPanel();
        cardLayout = new CardLayout();
        main.setLayout(cardLayout);

        faithPathPanel = new FaithPathPanel(gui, liteFaithPath);
        main.add(new JPanel(),"0");
        main.add(faithPathPanel,"1");
        cardLayout.show(main,"0");

        buttonSection();
        topPanel();

        this.add(main, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);
        this.add(topPanel, BorderLayout.NORTH);

    }

    private void topPanel(){
        topPanel = new JPanel();
        JLabel title = new JLabel("NOTIFICHE GAME", JLabel.CENTER);
        title.setFont(new Font("Rubik", Font.BOLD, 24));
        title.setForeground(new Color(255,255,255));
        title.setOpaque(false);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title);
        topPanel.setBackground(new Color(219, 139, 0));
    }

    private void buttonSection(){

        buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        JButton show_my_board = new ButtonImage("MY BOARD", 22,true);
        show_my_board.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(main, "0");
            }
        });
        show_my_board.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton showFaithpath = new ButtonImage("FAITHPATH", 22,true);
        showFaithpath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(main, "1");
            }
        });
        showFaithpath.setAlignmentX(Component.CENTER_ALIGNMENT);


        JButton showMarket = new ButtonImage("MARKET", 22,true);
        showMarket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(main, "2");
            }
        });
        showMarket.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton showCardGrid = new ButtonImage("CARD GRID", 22,true);
        showCardGrid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(main, "3");
            }
        });
        showCardGrid.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton showPlayer = new ButtonImage("PLAYER", 22,true);
        showPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(main, "4");
            }
        });
        showPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton increase = new ButtonImage("Increase Pos", 22,true);
        increase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                faithPathPanel.incRedCrossImages("LORENZO");
            }
        });
        increase.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttons.setBackground(new Color(255, 235, 204));

        buttons.add(Box.createRigidArea(new Dimension(350,30)));
        buttons.add(showFaithpath);
        buttons.add(Box.createRigidArea(new Dimension(30,30)));
        buttons.add(showMarket);
        buttons.add(Box.createRigidArea(new Dimension(30,30)));
        buttons.add(showPlayer);
        buttons.add(Box.createRigidArea(new Dimension(30,30)));
        buttons.add(show_my_board);
        buttons.add(Box.createRigidArea(new Dimension(30,30)));
        buttons.add(showCardGrid);
        buttons.add(Box.createRigidArea(new Dimension(30,30)));
        buttons.add(increase);
        buttons.add(Box.createRigidArea(new Dimension(30,30)));

    }
}
