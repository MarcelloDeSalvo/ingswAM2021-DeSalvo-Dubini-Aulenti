package it.polimi.ingsw.view.gui.buttons;

import javax.swing.*;
import java.awt.*;

public class ButtonSelectable extends ButtonImage {
    private boolean selected = false;

    public ButtonSelectable(String path, Color color, int thickness) {
        super(path);
        this.setBorderPainted(true);
        this.addActionListener(e -> {

            if (!selected){
                selected = true;
                setBorder(BorderFactory.createLineBorder(color, thickness));
            }
            else{
                selected = false;
                setBorder(BorderFactory.createEmptyBorder());
            }

        });
    }

}
