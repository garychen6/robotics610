package scouting.components;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
    private BufferedImage paintLayer;
    private Graphics2D graphics2D;

    //Image setup
    private final URL FIELD_DRAWING_URL, FIELD_DRAWING_B_URL;
    private Image[] fieldDrawing;
    
    
    //Change image?
    private boolean isRedOnLeft;
    
    //Last shot made
    private int lastClick;
    
    //CTRL toggle
    private boolean isCTRLDown;
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
        
        //Initialize variables
        isRedOnLeft = true;
        lastClick = 0;
        isCTRLDown = false;
        
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
        
        //Initialize image components
        FIELD_DRAWING_URL = getClass().getClassLoader().getResource("scouting/images/FieldAlpha.png");
        FIELD_DRAWING_B_URL = getClass().getClassLoader().getResource("scouting/images/FieldAlpha2.png");
        
        fieldDrawing = new Image[2];
        try {
            fieldDrawing[0] = ImageIO.read(FIELD_DRAWING_URL);
            fieldDrawing[1] = ImageIO.read(FIELD_DRAWING_B_URL);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "An Error has Occurred", JOptionPane.WARNING_MESSAGE);
        }
        
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
                isCTRLDown = false;
            }
        });
        
        //Add a mouse motion listener
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent evt) {
                requestFocusInWindow();
            }
        });
       
        //Add a key listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_W) scoutApp.getButtonPanel().setTopActive();
                if(evt.getKeyCode() == KeyEvent.VK_A) scoutApp.getButtonPanel().setMidLeftActive();
                if(evt.getKeyCode() == KeyEvent.VK_S) scoutApp.getButtonPanel().setBottomActive();
                if(evt.getKeyCode() == KeyEvent.VK_D) scoutApp.getButtonPanel().setMidRightActive();
                if(evt.getKeyCode() == KeyEvent.VK_SPACE) scoutApp.getButtonPanel().setHybridMode(!scoutApp.getButtonPanel().isHybridMode());
                if(evt.getKeyCode() == KeyEvent.VK_SHIFT) scoutApp.getButtonPanel().setMissedMode(true);
                
                //Quick hack for CTRL+Z
                if(evt.getKeyCode() == KeyEvent.VK_CONTROL) isCTRLDown = true;
                if(evt.getKeyCode() == KeyEvent.VK_Z && isCTRLDown) undoLastShot();
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
        if(paintLayer == null) loadImage();
        g.drawImage(paintLayer, 0, 0, null);
    }

    //Set isRedOnLeft with a confirm dialog
    protected void setBackgroundImage() {
        isRedOnLeft = JOptionPane.showConfirmDialog(this, "Is the red alliance on the left?", "Set Display Image", JOptionPane.YES_NO_OPTION) == 0;
    }
    
    //Load the field image
    protected final void loadImage() {
        paintLayer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        graphics2D = (Graphics2D)paintLayer.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(isRedOnLeft) graphics2D.drawImage(fieldDrawing[0], 0, 0, null);
        else graphics2D.drawImage(fieldDrawing[1], 0, 0, null);
    }
    
    //Update shot data and paint onto screen
    private void makeShot(SHOT_TYPE shotType) {
        Point p = (isRedOnLeft) ? new Point(x, y) : new Point(745 - x, 324 - y);
        switch (shotType) {
             case SCORED_TOP:
                graphics2D.setColor(TOP);
                graphics2D.fillOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) {
                    hybridTop.add(p);
                    lastClick = 0;
                } else {
                    teleopTop.add(p);
                    lastClick = 1;
                }
                break;
            case SCORED_MID_LEFT:
                graphics2D.setColor(MID_LEFT);
                graphics2D.fillOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) {
                    hybridMidLeft.add(p);
                    lastClick = 2;
                } else {
                    teleopMidLeft.add(p);
                    lastClick = 3;
                }
                break;
            case SCORED_MID_RIGHT:
                graphics2D.setColor(MID_RIGHT);
                graphics2D.fillOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) {
                    hybridMidRight.add(p);
                    lastClick = 4;
                } else {
                    teleopMidRight.add(p);
                    lastClick = 5;
                }
                break;
            case SCORED_BOTTOM:
                graphics2D.setColor(BOTTOM);
                graphics2D.fillOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) {
                    hybridBottom.add(p);
                    lastClick = 6;
                } else {
                    teleopBottom.add(p);
                    lastClick = 7;
                }
                break;
            case MISSED_TOP:
                graphics2D.setColor(TOP);
                graphics2D.drawOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) {
                    hybridMissedTop.add(p);
                    lastClick = 8;
                } else {
                    teleopMissedTop.add(p);
                    lastClick = 9;
                }
                break;
            case MISSED_MID_LEFT:
                graphics2D.setColor(MID_LEFT);
                graphics2D.drawOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) {
                    hybridMissedMidLeft.add(p);
                    lastClick = 10;
                } else {
                    teleopMissedMidLeft.add(p);
                    lastClick = 11;
                }
                break;
            case MISSED_MID_RIGHT:
                graphics2D.setColor(MID_RIGHT);
                graphics2D.drawOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) {
                    hybridMissedMidRight.add(p);
                    lastClick = 12;
                } else {
                    teleopMissedMidRight.add(p);
                    lastClick = 13;
                }
                break;
            case MISSED_BOTTOM:
                graphics2D.setColor(BOTTOM);
                graphics2D.drawOval(x - 4, y - 4, 8, 8);
                if(scoutApp.getButtonPanel().isHybridMode()) {
                    hybridMissedBottom.add(p);
                    lastClick = 14;
                } else {
                    teleopMissedBottom.add(p);
                    lastClick = 15;
                }
                break;
        }
        repaint();
    }
      
    //Undo last shot
    private void undoLastShot() {
        switch(lastClick) {
            case 0:
                if(hybridTop.size() != 0) hybridTop.removeLast();
                break;
            case 1:
                if(teleopTop.size() != 0) teleopTop.removeLast();
                break;
            case 2:
                if(hybridMidLeft.size() != 0) hybridMidLeft.removeLast();
                break;
            case 3:
                if(teleopMidLeft.size() != 0) teleopMidLeft.removeLast();
                break;
            case 4:
                if(hybridMidRight.size() != 0) hybridMidRight.removeLast();
                break;
            case 5:
                if(teleopMidRight.size() != 0) teleopMidRight.removeLast();
                break;
            case 6:
                if(hybridBottom.size() != 0) hybridBottom.removeLast();
                break;
            case 7:
                if(teleopBottom.size() != 0) teleopBottom.removeLast();
                break;
            case 8:
                if(hybridMissedTop.size() != 0) hybridMissedTop.removeLast();
                break;
            case 9:
                if(teleopMissedTop.size() != 0) teleopMissedTop.removeLast();
                break;
            case 10:
                if(hybridMissedMidLeft.size() != 0) hybridMissedMidLeft.removeLast();
                break;
            case 11:
                if(teleopMissedMidLeft.size() != 0) teleopMissedMidLeft.removeLast();
                break;
            case 12:
                if(hybridMissedMidRight.size() != 0) hybridMissedMidRight.removeLast();
                break;
            case 13:
                if(teleopMissedMidRight.size() != 0) teleopMissedMidRight.removeLast();
                break;
            case 14:
                if(hybridMissedBottom.size() != 0) hybridMissedBottom.removeLast();
                break;
            case 15:
                if(teleopMissedBottom.size() != 0) teleopMissedBottom.removeLast();
                break;
        }
        scoutApp.getStatusPanel().update();
        scoutApp.getScoutForm().update();
        
        isCTRLDown = false;
        lastClick = -1;
        replaceImage();
    }
    
    //Paints points according to the lists
    private void replaceImage() {
        loadImage();
        ListIterator<Point> it = hybridTop.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(TOP);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.fillOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.fillOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = hybridMidLeft.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(MID_LEFT);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.fillOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.fillOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = hybridMidRight.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(MID_RIGHT);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.fillOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.fillOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = hybridBottom.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(BOTTOM);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.fillOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.fillOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = teleopTop.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(TOP);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.fillOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.fillOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = teleopMidLeft.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(MID_LEFT);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.fillOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.fillOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = teleopMidRight.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(MID_RIGHT);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.fillOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.fillOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = teleopBottom.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(BOTTOM);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.fillOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.fillOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = hybridMissedTop.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(TOP);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.drawOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.drawOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = hybridMissedMidLeft.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(MID_LEFT);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.drawOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.drawOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = hybridMissedMidRight.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(MID_RIGHT);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.drawOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.drawOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = hybridMissedBottom.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(BOTTOM);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.drawOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.drawOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = teleopMissedTop.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(TOP);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.drawOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.drawOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = teleopMissedMidLeft.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(MID_LEFT);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.drawOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.drawOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = teleopMissedMidRight.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(MID_RIGHT);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.drawOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.drawOval(741 - p.x, 320 - p.y, 8, 8);
        }
        it = teleopMissedBottom.listIterator();
        while(it.hasNext()) {
            graphics2D.setColor(BOTTOM);
            Point p = it.next();
            if(isRedOnLeft) graphics2D.drawOval(p.x - 4, p.y - 4, 8, 8);
            else graphics2D.drawOval(741 - p.x, 320 - p.y, 8, 8);
        }
        repaint();
    }
    
    //Reset all data
    protected void reset() {
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
        
        loadImage();
        repaint();
    }
    
    //Returns lists containing various shot points
    protected LinkedList<Point> getHybridTop() {
        return hybridTop;
    }
    
    protected LinkedList<Point> getHybridMidLeft() {
        return hybridMidLeft;
    }
    
    protected LinkedList<Point> getHybridMidRight() {
        return hybridMidRight;
    }
    
    protected LinkedList<Point> getHybridBottom() {
        return hybridBottom;
    }
    
    protected LinkedList<Point> getMissedHybridTop() {
        return hybridMissedTop;
    }
    
    protected LinkedList<Point> getMissedHybridMidLeft() {
        return hybridMissedMidLeft;
    }
    
    protected LinkedList<Point> getMissedHybridMidRight() {
        return hybridMissedMidRight;
    }
    
    protected LinkedList<Point> getMissedHybridBottom() {
        return hybridMissedBottom;
    }
    
    protected LinkedList<Point> getTeleopTop() {
        return teleopTop;
    }
    
    protected LinkedList<Point> getTeleopMidLeft() {
        return teleopMidLeft;
    }
    
    protected LinkedList<Point> getTeleopMidRight() {
        return teleopMidRight;
    }
    
    protected LinkedList<Point> getTeleopBottom() {
        return teleopBottom;
    }
    
    protected LinkedList<Point> getMissedTeleopTop() {
        return teleopMissedTop;
    }
    
    protected LinkedList<Point> getMissedTeleopMidLeft() {
        return teleopMissedMidLeft;
    }
    
    protected LinkedList<Point> getMissedTeleopMidRight() {
        return teleopMissedMidRight;
    }
    
    protected LinkedList<Point> getMissedTeleopBottom() {
        return teleopMissedBottom;
    }
}