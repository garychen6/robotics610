package scouting.components;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JPanel;
import scouting.util.SHOT_TYPE;

/**
 * 
 * @author Wilson Cheang, Peter Harquail
 */
public class PaintPanel extends JPanel {
    private ScoutingApp scoutApp;
    
    //Paint coordinates
    private int x, y;
    
    //Declare components
    private Image paintLayer;
    private Graphics2D graphics2D;

    //Image setup
    private final URL fieldDrawingURL = getClass().getResource("scouting/images/FieldAlpha.png");
    private final Image fieldDrawing = Toolkit.getDefaultToolkit().getImage(fieldDrawingURL);
    //Declare and initialize representative paint colours
    private static final Color TOP = new Color(0x00, 0x33, 0xff, 90);
    private static final Color MID_LEFT = new Color(0x00, 0xff, 0x00, 90);
    private static final Color MID_RIGHT = new Color(0xff, 0x00, 0xff, 90);
    private static final Color BOTTOM = new Color(0xcc, 0x00, 0x00, 90);
    
    //Declare lists to keep track of scoring
    private LinkedList<Point> hybridTop, hybridMidLeft, hybridMidRight, hybridBottom;
    private LinkedList<Point> hybridMissedTop, hybridMissedMidLeft, hybridMissedMidRight, hybridMissedBottom;
    private LinkedList<Point> teleopTop, teleopMidLeft, teleopMidRight, teleopBottom;
    private LinkedList<Point> teleopMissedTop, teleopMissedMidLeft, teleopMissedMidRight, teleopMissedBottom;
    
    public PaintPanel(final ScoutingApp scoutApp) {
        this.scoutApp = scoutApp;
        
        //Initialize panel
        setMinimumSize(new Dimension(745, 324));
        setMaximumSize(new Dimension(745, 324));
        setPreferredSize(new Dimension(745, 324));
        
        //Initialize lists
        hybridTop = new LinkedList<Point>();
        hybridMidLeft = new LinkedList<Point>();
        hybridMidRight = new LinkedList<Point>();
        hybridBottom = new LinkedList<Point>();
        hybridMissedTop = new LinkedList<Point>();
        hybridMissedMidLeft = new LinkedList<Point>();
        hybridMissedMidRight = new LinkedList<Point>();
        hybridMissedBottom = new LinkedList<Point>();
        teleopTop = new LinkedList<Point>();
        teleopMidLeft = new LinkedList<Point>();
        teleopMidRight = new LinkedList<Point>();
        teleopBottom = new LinkedList<Point>();
        teleopMissedTop = new LinkedList<Point>();
        teleopMissedMidLeft = new LinkedList<Point>();
        teleopMissedMidRight = new LinkedList<Point>();
        teleopMissedBottom = new LinkedList<Point>();
        
       
        //Add a mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                if(50 <= evt.getX() && evt.getX() <= 695) {
                    requestFocusInWindow();
                    x = evt.getX();
                    y = evt.getY();
                    if(evt.getButton() == MouseEvent.BUTTON1) {
                        if(scoutApp.getButtonPanel().inMissedMode()) {
                            if(scoutApp.getButtonPanel().isTopSelected()) makeShot(SHOT_TYPE.MISSED_TOP);
                            else if(scoutApp.getButtonPanel().isMidLeftSelected()) makeShot(SHOT_TYPE.MISSED_MID_LEFT);
                            else if(scoutApp.getButtonPanel().isMidRightSelected()) makeShot(SHOT_TYPE.MISSED_MID_RIGHT);
                            else if(scoutApp.getButtonPanel().isBottomSelected()) makeShot(SHOT_TYPE.MISSED_BOTTOM);
                        } else {
                            if(scoutApp.getButtonPanel().isTopSelected()) makeShot(SHOT_TYPE.SCORED_TOP);
                            else if(scoutApp.getButtonPanel().isMidLeftSelected()) makeShot(SHOT_TYPE.SCORED_MID_LEFT);
                            else if(scoutApp.getButtonPanel().isMidRightSelected()) makeShot(SHOT_TYPE.SCORED_MID_RIGHT);
                            else if(scoutApp.getButtonPanel().isBottomSelected()) makeShot(SHOT_TYPE.SCORED_BOTTOM);
                        }
                    } else if(evt.getButton() == MouseEvent.BUTTON3) {
                        if(scoutApp.getButtonPanel().isTopSelected()) makeShot(SHOT_TYPE.MISSED_TOP);
                        else if(scoutApp.getButtonPanel().isMidLeftSelected()) makeShot(SHOT_TYPE.MISSED_MID_LEFT);
                        else if(scoutApp.getButtonPanel().isMidRightSelected()) makeShot(SHOT_TYPE.MISSED_MID_RIGHT);
                        else if(scoutApp.getButtonPanel().isBottomSelected()) makeShot(SHOT_TYPE.MISSED_BOTTOM);
                    }
                    scoutApp.getScoutForm().update();
                    scoutApp.getStatusPanel().update();
                }
            }
        });
        
        addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent evt) {
                    if(evt.getKeyCode() == KeyEvent.VK_W) scoutApp.getButtonPanel().setTopActive();
                    if(evt.getKeyCode() == KeyEvent.VK_A) scoutApp.getButtonPanel().setMidLeftActive();
                    if(evt.getKeyCode() == KeyEvent.VK_S) scoutApp.getButtonPanel().setMidRightActive();
                    if(evt.getKeyCode() == KeyEvent.VK_D) scoutApp.getButtonPanel().setBottomActive();
                    if(evt.getKeyCode() == KeyEvent.VK_SPACE) scoutApp.getButtonPanel().setHybridMode(!scoutApp.getButtonPanel().isHybridMode());
                    if(evt.getKeyCode() == KeyEvent.VK_SHIFT) scoutApp.getButtonPanel().setMissedMode(true);
                }

                @Override
                public void keyReleased(KeyEvent evt) {
                    if(evt.getKeyCode() == KeyEvent.VK_SHIFT) scoutApp.getButtonPanel().setMissedMode(false);
                }
            });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(paintLayer == null) {
            paintLayer = createImage(getWidth(), getHeight());
            graphics2D = (Graphics2D)paintLayer.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.drawImage(fieldDrawing, 0, 0, null);
        }
        g.drawImage(paintLayer, 0, 0, null);
    }

    //Update shot data and paint onto screen
    public void makeShot(SHOT_TYPE shotType) {
        switch (shotType) {
             case SCORED_TOP:
                graphics2D.setColor(TOP);
                graphics2D.fillOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) hybridTop.add(new Point(x, y));
                else if(!scoutApp.getButtonPanel().isHybridMode()) teleopTop.add(new Point(x, y));
                break;
            case SCORED_MID_LEFT:
                graphics2D.setColor(MID_LEFT);
                graphics2D.fillOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) hybridMidLeft.add(new Point(x, y));
                else if(!scoutApp.getButtonPanel().isHybridMode()) teleopMidLeft.add(new Point(x, y));
                break;
            case SCORED_MID_RIGHT:
                graphics2D.setColor(MID_RIGHT);
                graphics2D.fillOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) hybridMidRight.add(new Point(x, y));
                else if(!scoutApp.getButtonPanel().isHybridMode()) teleopMidRight.add(new Point(x, y));
                break;
            case SCORED_BOTTOM:
                graphics2D.setColor(BOTTOM);
                graphics2D.fillOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) hybridBottom.add(new Point(x, y));
                else if(!scoutApp.getButtonPanel().isHybridMode()) teleopBottom.add(new Point(x, y));
                break;
            case MISSED_TOP:
                graphics2D.setColor(TOP);
                graphics2D.drawOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) hybridMissedTop.add(new Point(x, y));
                else if(!scoutApp.getButtonPanel().isHybridMode()) teleopMissedTop.add(new Point(x, y));
                break;
            case MISSED_MID_LEFT:
                graphics2D.setColor(MID_LEFT);
                graphics2D.drawOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) hybridMissedMidLeft.add(new Point(x, y));
                else if(!scoutApp.getButtonPanel().isHybridMode()) teleopMissedMidLeft.add(new Point(x, y));
                break;
            case MISSED_MID_RIGHT:
                graphics2D.setColor(MID_RIGHT);
                graphics2D.drawOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) hybridMissedMidRight.add(new Point(x, y));
                else if(!scoutApp.getButtonPanel().isHybridMode()) teleopMissedMidRight.add(new Point(x, y));
                break;
            case MISSED_BOTTOM:
                graphics2D.setColor(BOTTOM);
                graphics2D.drawOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) hybridMissedBottom.add(new Point(x, y));
                else if(!scoutApp.getButtonPanel().isHybridMode()) teleopMissedBottom.add(new Point(x, y));
                break;
        }
        repaint();
    }
      
    //Reset all data
    public void reset() {
        hybridTop.clear();
        hybridMidLeft.clear();
        hybridMidRight.clear();
        hybridBottom.clear();
        hybridMissedTop.clear();
        hybridMissedMidLeft.clear();
        hybridMissedMidRight.clear();
        hybridMissedBottom.clear();
        teleopTop.clear();
        teleopMidLeft.clear();
        teleopMidRight.clear();
        teleopBottom.clear();
        teleopMissedTop.clear();
        teleopMissedMidLeft.clear();
        teleopMissedMidRight.clear();
        teleopMissedBottom.clear();
        
        paintLayer = createImage(getWidth(), getHeight());
        graphics2D = (Graphics2D)paintLayer.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(fieldDrawing, 0, 0, null);
        repaint();
    }
    
    //Returns lists containing various shot points
    public LinkedList<Point> getHybridTop() {
        return hybridTop;
    }
    
    public LinkedList<Point> getHybridMidLeft() {
        return hybridMidLeft;
    }
    
    public LinkedList<Point> getHybridMidRight() {
        return hybridMidRight;
    }
    
    public LinkedList<Point> getHybridBottom() {
        return hybridBottom;
    }
    
    public LinkedList<Point> getMissedHybridTop() {
        return hybridMissedTop;
    }
    
    public LinkedList<Point> getMissedHybridMidLeft() {
        return hybridMissedMidLeft;
    }
    
    public LinkedList<Point> getMissedHybridMidRight() {
        return hybridMissedMidRight;
    }
    
    public LinkedList<Point> getMissedHybridBottom() {
        return hybridMissedBottom;
    }
    
    public LinkedList<Point> getTeleopTop() {
        return teleopTop;
    }
    
    public LinkedList<Point> getTeleopMidLeft() {
        return teleopMidLeft;
    }
    
    public LinkedList<Point> getTeleopMidRight() {
        return teleopMidRight;
    }
    
    public LinkedList<Point> getTeleopBottom() {
        return teleopBottom;
    }
    
    public LinkedList<Point> getMissedTeleopTop() {
        return teleopMissedTop;
    }
    
    public LinkedList<Point> getMissedTeleopMidLeft() {
        return teleopMissedMidLeft;
    }
    
    public LinkedList<Point> getMissedTeleopMidRight() {
        return teleopMissedMidRight;
    }
    
    public LinkedList<Point> getMissedTeleopBottom() {
        return teleopMissedBottom;
    }
}