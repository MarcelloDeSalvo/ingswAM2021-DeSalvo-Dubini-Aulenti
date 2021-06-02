package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.view.gui.buttons.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.customJObject.TextFieldPlaceHolder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoginPanel extends JPanel {

    public LoginPanel(Gui swing) {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("", JLabel.CENTER);
        title.setFont(new Font("Rubik", Font.BOLD, 44));
        title.setForeground(new Color(240,150,100));
        title.setOpaque(true);

        BufferedImage img = null;
        try {
            img = ImageIO.read(this.getClass().getResourceAsStream("/images/others/copertina.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image dimg = img.getScaledInstance(600, 300,
                Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        title.setIcon(imageIcon);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField jTextField = new TextFieldPlaceHolder("Username...");
        jTextField.setForeground(Color.GRAY);
        jTextField.setMaximumSize(new Dimension(500,50));
        jTextField.setFont(new Font("Rubik", Font.PLAIN,20));

        ButtonImage loginButton = new ButtonImage("/images/buttons/B_Login.png");
        loginButton.setMnemonic(KeyEvent.VK_ENTER);
        loginButton.addActionListener(e -> {
            swing.send(new Message.MessageBuilder().setCommand(Command.LOGIN).setInfo(jTextField.getText()).build());
            swing.setNickname(jTextField.getText());
        });
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createRigidArea(new Dimension(100,80)));
        this.add(title);
        this.add(Box.createRigidArea(new Dimension(100,70)));
        this.add(jTextField);
        this.add(Box.createRigidArea(new Dimension(100,30)));
        this.add(loginButton);
    }



}
