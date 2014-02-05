package scoutingcenter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Frame extends JFrame {

    final static boolean DEBUG = true;
    final static double SCROLL_SPEED = .6;
    int[] teamList;
    final TeamStats[] teams;
    double y;
    Thread thread;
    Thread updateThread;
    static final Color BG_COLOUR = new Color(0x169464);
    final Color SCROLL_COLOUR = BG_COLOUR.darker();
    Dimension size;
    int drawX;
    Header header;
    static final int Y_STAT_START = 200;

    public Frame() {
        header = new Header();
        header.render();
        size = Toolkit.getDefaultToolkit().getScreenSize();
        drawX = 0;
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
        setUndecorated(true);

        teamList = PhpRequest.getTeams();
        teams = getTeamStats();
        y = teamList.length * TeamStats.STATS_HEIGHT;
        setVisible(true);
        scheduleUpdate();
        thread = new Thread() {
            public void run() {
                while (true) {
                    y -= SCROLL_SPEED;
                    if (y < 0) {
                        y = teamList.length * TeamStats.STATS_HEIGHT;
                        scheduleUpdate();
                    }
                    repaint();
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        if (DEBUG) {
            com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this, true);
            com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
        } else {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice vc = ge.getDefaultScreenDevice();
            vc.setFullScreenWindow(this);
        }
    }

    public void scheduleUpdate() {
        updateThread = new Thread() {
            public void run() {
                for (int i = 0; i < teamList.length; i++) {
                    int yIndex = Math.min((int) ((teams.length * TeamStats.STATS_HEIGHT - y)
                            / TeamStats.STATS_HEIGHT), teams.length - 1);
                    while (yIndex < i) {
                        yIndex = Math.min((int) ((teams.length * TeamStats.STATS_HEIGHT - y)
                                / TeamStats.STATS_HEIGHT), teams.length - 1);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    TeamStats stats = new TeamStats(Score.getScoreFromTable("tournament1_" + teamList[i]), i);
                    stats.render();
                    teams[i] = stats;
                    System.out.println(teamList[i]);
                }
            }
        };
        updateThread.start();
    }

    public void repaint() {
        paintComponents(getGraphics());
    }

    public void paintComponents(Graphics g2) {
        BufferedImage drawImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = drawImage.createGraphics();
        g.setColor(BG_COLOUR);
//        g.drawImage(drawImage, null, WIDTH, WIDTH);
        g.fillRect(0, 0, size.width, size.height);
        int index = Math.min((int) ((teams.length * TeamStats.STATS_HEIGHT - y) / TeamStats.STATS_HEIGHT),
                teams.length - 1);
        int dy = (teams.length - index) * TeamStats.STATS_HEIGHT;
        int numImagesOnScreen = (int) Math.ceil((768 - Y_STAT_START) / (double) TeamStats.STATS_HEIGHT) + 1;
        for (int i = 0; i < numImagesOnScreen; i++) {
            BufferedImage cur = teams[(index + i) % teams.length];
            g.drawImage(cur, 24, Y_STAT_START + (int) y - dy + TeamStats.STATS_HEIGHT * i, null);
        }
        g.drawImage(header, 24, 0, null);
        g.fillRect(0, 768-24, 1024, 768);
        g.setColor(Color.BLACK);
        g.fillRect(24, 768-24, 1024-48, 2);
        Graphics2D g2d = (Graphics2D) g2;
        g2d.drawImage(drawImage, 0, 0, null);
    }

    public TeamStats[] getTeamStats() {
        TeamStats[] teamStats = new TeamStats[teamList.length];
        for (int i = 0; i < teamList.length; i++) {
            TeamStats stats = new TeamStats(Score.getScoreFromTable("tournament1_" + teamList[i]), i);
            stats.render();
            teamStats[i] = stats;
        }
        return teamStats;
    }
}
