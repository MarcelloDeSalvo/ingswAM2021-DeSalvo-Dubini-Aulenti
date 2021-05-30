package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;
import it.polimi.ingsw.view.gui.ButtonImage;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.TextFieldPlaceHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class LoginPanel extends JPanel {

    public LoginPanel(Gui swing) {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("WELCOME TO MASTER OF RENAISSANCE", JLabel.CENTER);
        title.setFont(new Font("Rubik", Font.BOLD, 44));
        title.setForeground(new Color(240,150,100));
        title.setOpaque(true);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField jTextField = new TextFieldPlaceHolder("Username...");
        jTextField.setForeground(Color.GRAY);
        jTextField.setMaximumSize(new Dimension(500,50));

        ButtonImage loginButton = new ButtonImage("/images/buttons/B_Login.png");
        loginButton.setMnemonic(KeyEvent.VK_ENTER);
        loginButton.addActionListener(e -> swing.send(new Message.MessageBuilder().setCommand(Command.LOGIN).setInfo(jTextField.getText()).build()));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createRigidArea(new Dimension(100,160)));
        this.add(title);
        this.add(Box.createRigidArea(new Dimension(100,120)));
        this.add(jTextField);
        this.add(Box.createRigidArea(new Dimension(100,30)));
        this.add(loginButton);
    }



}
