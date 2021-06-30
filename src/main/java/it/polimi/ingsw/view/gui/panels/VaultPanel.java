package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteVault;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.network.commands.SendContainer;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiStatus;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VaultPanel extends JLayeredPane {

    private final Gui gui;
    private final LiteVault liteVault;

    private final ButtonImage gold;
    private final ButtonImage stone;
    private final ButtonImage minion;
    private final ButtonImage shield;

    private final JLabel goldLabel;
    private final JLabel stoneLabel;
    private final JLabel minionLabel;
    private final JLabel shieldLabel;

    /**
     * Creates the vault panel structure
     * @param printOnly doesn't add the listeners if true
     */
    public VaultPanel(Gui gui, boolean printOnly, LiteVault liteVault) {
        super();

        int buttonSize = 110;

        this.gui = gui;
        this.liteVault =liteVault;

        this.setLayout(null);
        this.setOpaque(false);

        //LAYER0 -------------------------------------------------------------------------------------------------------
        JPanel layer0 = new JPanel();
        layer0.setLayout(new BoxLayout(layer0, BoxLayout.Y_AXIS));
        layer0.setOpaque(false);
        layer0.setBounds(0,0,330, 270);

        //ROW 0-1 -------------------------
        JPanel row01 = new JPanel();
        row01.setLayout(new BoxLayout(row01, BoxLayout.X_AXIS));
        row01.setOpaque(false);

        gold = new ButtonImage("/images/resourceImages/gold.png", new Dimension(buttonSize,buttonSize));
        if(!printOnly)
            gold.addActionListener(e-> resourceMenu(gold, ResourceType.GOLD));
        gold.setMinimumSize(new Dimension(buttonSize-1, buttonSize-1));
        gold.setMaximumSize(new Dimension(buttonSize, buttonSize));
        gold.setOpaque(false);

        stone = new ButtonImage("/images/resourceImages/stone.png", new Dimension(buttonSize,buttonSize));
        if(!printOnly)
            stone.addActionListener(e-> resourceMenu(stone, ResourceType.STONE));
        stone.setMinimumSize(new Dimension(buttonSize-1, buttonSize-1));
        stone.setMaximumSize(new Dimension(buttonSize, buttonSize));
        stone.setOpaque(false);

        row01.add(gold);
        row01.add(Box.createRigidArea(new Dimension(25,5)));
        row01.add(stone);

        //ROW 0-2 -------------------------
        JPanel row02 = new JPanel();
        row02.setLayout(new BoxLayout(row02, BoxLayout.X_AXIS));
        row02.setOpaque(false);

        minion = new ButtonImage("/images/resourceImages/minion.png", new Dimension(buttonSize, buttonSize));
        if(!printOnly)
            minion.addActionListener(e-> resourceMenu(minion, ResourceType.MINION));
        minion.setMinimumSize(new Dimension(buttonSize-1, buttonSize-1));
        minion.setMaximumSize(new Dimension(buttonSize, buttonSize));
        minion.setOpaque(false);

        shield = new ButtonImage("/images/resourceImages/shield.png", new Dimension(buttonSize, buttonSize));
        if(!printOnly)
            shield.addActionListener(e-> resourceMenu(shield, ResourceType.SHIELD));
        shield.setMinimumSize(new Dimension(buttonSize-1, buttonSize-1));
        shield.setMaximumSize(new Dimension(buttonSize, buttonSize));
        shield.setOpaque(false);

        row02.add(minion);
        row02.add(Box.createRigidArea(new Dimension(25,5)));
        row02.add(shield);

        layer0.add(row01);
        layer0.add(Box.createRigidArea(new Dimension(5,25)));
        layer0.add(row02);

        this.add(layer0, JLayeredPane.DEFAULT_LAYER);


        //LAYER1 -------------------------------------------------------------------------------------------------------
        JPanel layer1 = new JPanel();
        layer1.setLayout(new BoxLayout(layer1, BoxLayout.Y_AXIS));
        layer1.setOpaque(false);
        layer1.setBounds(-10,0,330, 270);

        Font font = new Font("Rubik", Font.BOLD, 25);

        //ROW 1-1 -------------------------
        JPanel row11 = new JPanel();
        row11.setLayout(new BoxLayout(row11, BoxLayout.X_AXIS));
        row11.setOpaque(false);

        goldLabel = new JLabel();
        goldLabel.setFont(font);
        goldLabel.setForeground(Color.BLACK);

        stoneLabel = new JLabel();
        stoneLabel.setFont(font);
        stoneLabel.setForeground(Color.BLACK);

        row11.add(goldLabel);
        row11.add(Box.createRigidArea(new Dimension(100,5)));
        row11.add(stoneLabel);

        //ROW 1-2 -------------------------
        JPanel row12 = new JPanel();
        row12.setLayout(new BoxLayout(row12, BoxLayout.X_AXIS));
        row12.setOpaque(false);

        minionLabel = new JLabel();
        minionLabel.setFont(font);
        minionLabel.setForeground(Color.BLACK);

        shieldLabel = new JLabel();
        shieldLabel.setFont(font);
        shieldLabel.setForeground(Color.BLACK);

        row12.add(minionLabel);
        row12.add(Box.createRigidArea(new Dimension(100,5)));
        row12.add(shieldLabel);

        layer1.add(Box.createRigidArea(new Dimension(5,30)));
        layer1.add(row11);
        layer1.add(Box.createRigidArea(new Dimension(5,110)));
        layer1.add(row12);

        this.add(layer1, JLayeredPane.POPUP_LAYER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        goldLabel.setText(" " + liteVault.getQtyOfResource(ResourceType.GOLD) + " ");
        stoneLabel.setText(" " + liteVault.getQtyOfResource(ResourceType.STONE) + " ");
        minionLabel.setText(" " + liteVault.getQtyOfResource(ResourceType.MINION) + " ");
        shieldLabel.setText(" " + liteVault.getQtyOfResource(ResourceType.SHIELD) + " ");
    }

    /**
     * Lists the operations that can be applied to the vault slots
     */
    private void resourceMenu(ButtonImage button, ResourceType resourceType){
        JPopupMenu popupmenu = new JPopupMenu("Vault");
        JMenuItem give = new JMenuItem("Pay with this");
        JMenuItem giveMultiple = new JMenuItem("Pay amount");
        JMenuItem done = new JMenuItem("Done");

        popupmenu.add(give);
        popupmenu.add(giveMultiple);
        popupmenu.add(done);

        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(gui.getGuiStatus() == GuiStatus.SELECTING_PAY_RESOURCES)
                    popupmenu.show(button , e.getX(), e.getY());
            }
        });

        give.addActionListener(e -> {
            if (gui.getGuiStatus() == GuiStatus.SELECTING_PAY_RESOURCES){
                gui.send(new SendContainer(new ResourceContainer(resourceType, 1),"VAULT", gui.getNickname()));

                button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 4));
                button.setBorderPainted(true);
            }
        });

        giveMultiple.addActionListener(e -> {
            if (gui.getGuiStatus() == GuiStatus.SELECTING_PAY_RESOURCES){
                String response = JOptionPane.showInputDialog("Chose an amount");
                int amount;

                try{
                    amount = Integer.parseInt(response);
                    gui.send(new SendContainer(new ResourceContainer(resourceType, amount),"VAULT", gui.getNickname()));
                    button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 4));
                    button.setBorderPainted(true);

                }catch (NumberFormatException f){
                    gui.printReply("Please select a valid amount");
                }
            }
        });

        done.addActionListener(e -> {
            if (gui.getGuiStatus() == GuiStatus.SELECTING_DEST_AFTER_MARKET || gui.getGuiStatus() == GuiStatus.SELECTING_PAY_RESOURCES){
                gui.send(new Message.MessageBuilder().setCommand(Command.DONE).setNickname(gui.getNickname()).build());
            }
        });
    }

    /**
     * Deselects all the button (removes the selection border)
     */
    public void deselectAll(){
        gold.setBorderPainted(false);
        stone.setBorderPainted(false);
        minion.setBorderPainted(false);
        shield.setBorderPainted(false);
        repaint();
    }

}
