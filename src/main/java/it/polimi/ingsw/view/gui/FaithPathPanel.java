package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.liteModel.LiteFaithPath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class FaithPathPanel extends BackgroundImagePanel {

    private Gui gui;
    private LiteFaithPath liteFaithPath;
    private Point point;
    private ArrayList<Point> points;

    public FaithPathPanel(Gui gui, LiteFaithPath liteFaithPath) {
        super("/images/FaithPath.jpg", 50, 250, true);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("CULO");
        strings.add("LOERNZO");
        liteFaithPath.setUpPositions(strings);

        for (String nickname: liteFaithPath.getNicknames()) {
            SmartImagePanel redCrossImg =
                    new RedCrossImage(gui, "/images/croceRossa.png",
                            new Dimension(30, 30), new Point(171, 536), this, nickname);
            add(redCrossImg);
        }

        this.addMouseListener(new ClickListener());
    }


    /**
     * Listens for mouse clicks
     */
    private class ClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println(e.getPoint());
        }
    }



}
