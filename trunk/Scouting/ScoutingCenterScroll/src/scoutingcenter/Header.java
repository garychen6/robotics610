package scoutingcenter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import static scoutingcenter.TeamStats.STATS_WIDTH;

public class Header extends BufferedImage {

    private static final int CELL_WIDTH = STATS_WIDTH / 8;
    private static final Font FONT = new Font("Helvetica Neue", Font.BOLD, 34);
    private static final int IMG_HEIGHT = 165;
    
    BufferedImage aerialAssistLogo;
    BufferedImage firstLogo;
    BufferedImage crescentLogo;

    public Header() {
        super(1024, Frame.Y_STAT_START, BufferedImage.TYPE_INT_ARGB);
        try {
            aerialAssistLogo = ImageIO.read(getClass().getResourceAsStream("aerialAssistLogo.png"));
            firstLogo = ImageIO.read(getClass().getResourceAsStream("firstLogo.png"));
            crescentLogo = ImageIO.read(getClass().getResourceAsStream("crescent.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render() {
        Graphics2D g = createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Frame.BG_COLOUR);
        g.fillRect(0, 0, 1024, Frame.Y_STAT_START);
        
        g.drawImage(aerialAssistLogo, 0, 5, null);
        g.drawImage(firstLogo, 1024-firstLogo.getWidth()-48, 5, null);
        g.drawImage(crescentLogo, 1024/2-crescentLogo.getWidth()/2, 5,null);

        g.setColor(Color.BLACK);
        g.fillRect(0, IMG_HEIGHT, STATS_WIDTH, 2);
        g.fillRect(STATS_WIDTH - 2, IMG_HEIGHT, 2, Frame.Y_STAT_START - IMG_HEIGHT);

        drawString("TEAM", g, FONT, 0);
        drawVerticalLine(4, g, 1 * CELL_WIDTH);
        drawString("AFG%", g, FONT, 1);
        drawString("APPG", g, FONT, 2);
        drawVerticalLine(4, g, 3 * CELL_WIDTH);
        drawString("FG%", g, FONT, 3);
        drawString("APG", g, FONT, 4);
        drawString("TPG", g, FONT, 5);
        drawString("CPG", g, FONT, 6);
        drawString("PPG", g, FONT, 7);
        
        g.fillRect(0, Frame.Y_STAT_START - 2, STATS_WIDTH, 2);
    }

    public void drawString(String s, Graphics2D g, Font f, int row) {
        g.setColor(Color.WHITE);
        g.setFont(f);
        int x = row * CELL_WIDTH;
        int x2 = x+CELL_WIDTH/2;
        Rectangle2D bounds = g.getFontMetrics(f).getStringBounds(s, g);
        double width = bounds.getWidth();
        g.drawString(s, x2-(int)(width/2), Frame.Y_STAT_START-5);
        g.setColor(Color.BLACK);
        drawVerticalLine(2, g, x);
    }

    public void drawVerticalLine(int thickness, Graphics2D g, int x) {
        g.fillRect(x, IMG_HEIGHT, thickness, Frame.Y_STAT_START - IMG_HEIGHT);
    }
}
