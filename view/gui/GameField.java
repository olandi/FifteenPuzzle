package project.olandi.view.gui;

import project.olandi.controller.Controller;

import javax.swing.*;
import java.awt.*;

class GameField extends JPanel {
    private Controller controller;

    static final Color BG_COLOR = new Color(0x6B8300);//new Color(0xbbada0);
    private static final Color TILE_COLOR = new Color(0x8C8E0B);
    static final String FONT_NAME = "Arial";
    static final int TILE_SIZE = 96;
    static final int TILE_MARGIN = 12;


    GameField(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(BG_COLOR);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);

        for (int i = 0; i < 16; i++) {
            drawTile(g, controller.getModel().getGameField()[i], i % 4, i / 4);
        }

    }

    private void drawTile(Graphics g2, int tileIndex, int x, int y) {
        Graphics2D g = ((Graphics2D) g2);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);

        g.setColor(TILE_COLOR);
        g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 8, 8);
        g.setColor(Color.BLACK);
        final int size = tileIndex < 100 ? 36 : tileIndex < 1000 ? 32 : 24;
        final Font font = new Font(FONT_NAME, Font.BOLD, size);
        g.setFont(font);

        String s = String.valueOf(tileIndex);
        final FontMetrics fm = getFontMetrics(font);

        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

        if (tileIndex != 0)
            g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);
    }

    private static int offsetCoors(int arg) {
        return arg * (TILE_MARGIN + TILE_SIZE) + TILE_MARGIN;
    }
}