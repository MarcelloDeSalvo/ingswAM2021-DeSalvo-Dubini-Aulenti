package it.polimi.ingsw.view.gui.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonSelectable extends ButtonImage {
    private boolean selected = false;

    public ButtonSelectable(String path, Color color, int thickness ) {
        super(path);
        this.setBorderPainted(true);
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!selected){
                    selected= true;
                    setBorder(BorderFactory.createLineBorder(color, thickness));
                }
                else{
                    selected = false;
                    setBorder(BorderFactory.createEmptyBorder());
                }

            }
        });
    }

}
