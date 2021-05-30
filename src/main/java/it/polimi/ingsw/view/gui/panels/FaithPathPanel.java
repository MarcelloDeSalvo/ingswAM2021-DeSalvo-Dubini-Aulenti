package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.liteModel.LiteFaithPath;
import it.polimi.ingsw.view.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class FaithPathPanel extends BackgroundImagePanel {

    private Gui gui;
    private LiteFaithPath liteFaithPath;
    private Point point;
    private ArrayList<Point> points;
    private ArrayList<RedCrossImage> redCrossImages;

    public FaithPathPanel(Gui gui, LiteFaithPath liteFaithPath) {
        super("/images/FaithPath.jpg", 50, 250, true);

        points = new ArrayList<>();
        points.add(new Point(80, 256));
        points.add(new Point(115, 256));
        points.add(new Point(80, 296));
        points.add(new Point(115, 296));

        ArrayList<String> strings = new ArrayList<>();
        redCrossImages = new ArrayList<>();
        strings.add("CULO");
        strings.add("LORENZO");
        strings.add("SCEMO");
        liteFaithPath.setUpPositions(strings);

        int i = 0;
        for (String nickname: liteFaithPath.getNicknames()) {
            RedCrossImage redCrossImage = new RedCrossImage(gui, "/images/croceRossa.png",
                    new Dimension(30, 30), points.get(i), this, nickname);

            redCrossImages.add(redCrossImage);
            add(redCrossImage);
            i++;
        }

        incRedCrossImages("LORENZO");
    }

    public ArrayList<RedCrossImage> getRedCrossImages() {
        return redCrossImages;
    }

    public void incRedCrossImages(String senderNick) {
        for (RedCrossImage red: redCrossImages) {
            if (red.getNick().equals(senderNick))
                red.increasePos();
        }
    }
}
