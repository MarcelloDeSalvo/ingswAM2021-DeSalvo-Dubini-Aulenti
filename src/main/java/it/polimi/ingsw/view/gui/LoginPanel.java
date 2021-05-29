package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class LoginPanel extends JPanel {

    public LoginPanel(Gui swing) {
        super();
        //jPanel_login.setBorder(BorderFactory.createEmptyBorder(150,300,300,300));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("WELCOME TO MASTER OF RENAISSANCE", JLabel.CENTER);
        title.setFont(new Font("Helvetica",Font.PLAIN, 44));
        title.setForeground(new Color(240,150,100));
        title.setOpaque(true);

        JTextField jTextField = new TextFieldPlaceHolder("Username...");
        jTextField.setForeground(Color.GRAY);
        jTextField.setMinimumSize(new Dimension(600,50));

        ButtonImage loginButton = new ButtonImage("/images/buttons/B_Login.png");
        loginButton.setMnemonic(KeyEvent.VK_ENTER);
        loginButton.addActionListener(e -> swing.send(new Message.MessageBuilder().setCommand(Command.LOGIN).setInfo(jTextField.getText()).build()));

        this.add(Box.createRigidArea(new Dimension(100,100)));
        this.add(title);
        this.add(Box.createRigidArea(new Dimension(100,100)));
        this.add(jTextField);
        this.add(loginButton);
    }

}
