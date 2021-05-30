package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteFaithPath;
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
        this.add(buttons, BorderLayout.EAST);
        this.add(topPanel, BorderLayout.NORTH);

    }

    private void topPanel(){
        topPanel = new JPanel();
        JLabel title = new JLabel("NOTIFICHE GAME", JLabel.CENTER);
        title.setFont(new Font("Rubik", Font.BOLD, 24));
        title.setForeground(new Color(240,150,100));
        title.setOpaque(true);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title);
    }

    private void buttonSection(){

        buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

        JButton show_my_board = new JButton("SHOW MY BOARD");
        show_my_board.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(main, "0");
            }
        });
        show_my_board.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton showFaithpath = new JButton("SHOW FAITHPATH");
        showFaithpath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(main, "1");
            }
        });
        showFaithpath.setAlignmentX(Component.CENTER_ALIGNMENT);


        JButton showMarket = new JButton("SHOW MARKET");
        showMarket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(main, "2");
            }
        });
        showMarket.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton showCardGrid = new JButton("SHOW CARD GRID");
        showCardGrid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(main, "3");
            }
        });
        showCardGrid.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton showPlayer = new JButton("SHOW SHOW PLAYER");
        showPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(main, "4");
            }
        });
        showPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton increase = new JButton("Increase Pos");
        increase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                faithPathPanel.incRedCrossImages("LORENZO");
            }
        });
        increase.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttons.setBackground(new Color(200,128,122));

        buttons.add(Box.createRigidArea(new Dimension(30,300)));
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

    }
}
