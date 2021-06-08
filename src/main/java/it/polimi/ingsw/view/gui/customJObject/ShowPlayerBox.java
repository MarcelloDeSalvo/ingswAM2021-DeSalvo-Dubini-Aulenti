package it.polimi.ingsw.view.gui.customJObject;

import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;
import java.util.ArrayList;

public class ShowPlayerBox extends JMenu {
    private final ArrayList<String> nicks;
    private final Gui gui;


    public ShowPlayerBox( Gui gui) {
        super();
        this.nicks = gui.getLiteFaithPath().getNicknames();
        this.gui=gui;


        for (String nick:nicks) {
            if(!nick.equals(gui.getNickname()) && !nick.equals("LORENZO")){
                JMenuItem item=new JMenuItem(nick);
                this.add(item);
            }
        }
    }
}
