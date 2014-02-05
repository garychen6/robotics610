package scoutingcenter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class TeamStats extends BufferedImage {

    public static final int STATS_WIDTH = 1024 - 24 * 2;
    public static final int STATS_HEIGHT = 40;
    private static final Font FONT = new Font("Helvetica Neue", Font.PLAIN, 26);
    private static final Font FONT_BOLD = new Font("Helvetica Neue", Font.BOLD, 26);
    private static final int CELL_WIDTH = STATS_WIDTH / 8;
    private static final Color WHITE = Color.WHITE;
    private static final Color GREY = new Color(0xD6D6D6);
    private static final Color YELLOW = new Color(0xFFFF00);
    
    Color bg;
    Score score;

    public TeamStats(Score score, int i) {
        super(STATS_WIDTH, STATS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.score = score;
        bg = i % 2 == 0 ? WHITE : GREY;
    }

    public void render() {
        Graphics2D g = createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(bg);
        g.fillRect(0, 0, STATS_WIDTH, STATS_HEIGHT);

        Composite init = g.getComposite();

        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .25f);
        g.setComposite(comp);
        g.setColor(YELLOW);
        g.fillRect(CELL_WIDTH, 0, CELL_WIDTH * 2, STATS_HEIGHT);
        g.setComposite(init);

        g.setColor(Color.BLACK);

        drawString("" + score.teamNum, g, FONT_BOLD, 0);
        drawVerticalLine(4, g, 1 * CELL_WIDTH);
        drawString(String.format("%.0f", score.autonFieldGoalPercent) + "%", g, FONT, 1);
        drawString(String.format("%.1f", score.autonScore), g, FONT, 2);
        drawVerticalLine(4, g, 3 * CELL_WIDTH);
        drawString(String.format("%.0f", score.fieldGoalPercent) + "%", g, FONT, 3);
        drawString(String.format("%.1f", score.avgAssist), g, FONT, 4);
        drawString(String.format("%.1f", score.avgTruss), g, FONT, 5);
        drawString(String.format("%.1f", score.avgCatch), g, FONT, 6);
        drawString(String.format("%.1f", score.teleScore + score.autonScore), g, FONT_BOLD, 7);
        drawVerticalLine(2, g, 8 * CELL_WIDTH - 2);

        g.fillRect(0, STATS_HEIGHT - 2, STATS_WIDTH, 2);
    }

    public void drawVerticalLine(int thickness, Graphics2D g, int x) {
        g.fillRect(x, 0, thickness, STATS_HEIGHT);
    }

    public void drawString(String s, Graphics2D g, Font f, int row) {
        g.setFont(f);
        int x = (row + 1) * (CELL_WIDTH);
        int xLine = row * CELL_WIDTH;
        Rectangle2D bounds = g.getFontMetrics(f).getStringBounds(s, g);
        double stringHeight = bounds.getHeight();
        double stringWidth = bounds.getWidth();
        int offset = s.indexOf("%") == -1 ? 12 : 7;
        g.drawString(s, (int) (x - stringWidth - offset), (int) (STATS_HEIGHT / 2 + stringHeight / 2) - 8);
        drawVerticalLine(2, g, xLine);
    }

    public void saveImage(int team) {
        try {
            ImageIO.write(this, "png", new File(team + ".png"));
        } catch (IOException ex) {
            Logger.getLogger(TeamStats.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}