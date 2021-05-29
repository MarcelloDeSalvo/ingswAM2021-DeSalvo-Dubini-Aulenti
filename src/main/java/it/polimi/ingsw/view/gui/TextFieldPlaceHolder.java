package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TextFieldPlaceHolder extends JTextField {

    public TextFieldPlaceHolder(String text) {
        super(text);

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (getText().equals(text)) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (getText().isEmpty()) {
                    setForeground(Color.GRAY);
                    setText(text);
                }
            }

        });
    }


}
