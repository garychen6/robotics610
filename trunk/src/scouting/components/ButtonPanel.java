package scouting.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Wilson Cheang, Peter Harquail
 */
public class ButtonPanel extends JPanel {

    private ScoutingApp scoutApp;
    
    //Declare buttons
    private static JButton BUTTON_W, BUTTON_A, BUTTON_S, BUTTON_D, BUTTON_SHIFT, BUTTON_SPACE;
    
    //Hybrid mode toggle
    private boolean isHybridMode;
    
    public ButtonPanel(final ScoutingApp scoutApp) {
        this.scoutApp = scoutApp;
        
        //Initialize variables
        isHybridMode = true;
        
        //Initialize panel
        setMinimumSize(new Dimension(745, 200));
        setMaximumSize(new Dimension(745, 200));
        setPreferredSize(new Dimension(745, 200));
        setLayout(null);

        //Initialize buttons
        BUTTON_W = new JButton("<html><p align = 'center'>Top<br>(W)</p></html>");
        BUTTON_A = new JButton("<html><p align = 'center'>Mid-Left<br>(A)</p></html>");
        BUTTON_S = new JButton("<html><p align = 'center'>Bottom<br>(S)</p></html>");
        BUTTON_D = new JButton("<html><p align = 'center'>Mid-Right<br>(D)</p></html>");
        BUTTON_SHIFT = new JButton("<html><p align = 'center'>Scored<br>(Shift)</p></html>");
        BUTTON_SPACE = new JButton("<html><p align = 'center'>Hybrid Period<br>(Space)</p></html>");

        //Set button locations
        BUTTON_W.setBounds(120, 10, 90, 90);
        BUTTON_A.setBounds(25, 105, 90, 90);
        BUTTON_S.setBounds(120, 105, 90, 90);
        BUTTON_D.setBounds(215, 105, 90, 90);
        BUTTON_SHIFT.setBounds(600, 30, 120, 45);
        BUTTON_SPACE.setBounds(340, 105, 380, 90);
        
        //Additional setup
        BUTTON_W.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setTopActive();
            }
        });
        BUTTON_W.setFocusable(false);
        BUTTON_W.setEnabled(false);
        BUTTON_W.setSelected(true);
        BUTTON_A.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setMidLeftActive();
            }
        });
        BUTTON_A.setFocusable(false);
        BUTTON_A.setEnabled(true);
        BUTTON_A.setSelected(false);
        BUTTON_S.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setBottomActive();
            }
        });
        BUTTON_S.setFocusable(false);
        BUTTON_S.setEnabled(true);
        BUTTON_S.setSelected(false);
        BUTTON_D.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setMidRightActive();
            }
        });
        BUTTON_D.setFocusable(false);
        BUTTON_D.setEnabled(true);
        BUTTON_D.setSelected(false);
        BUTTON_SHIFT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setMissedMode(!BUTTON_SHIFT.isSelected());
            }
        });
        BUTTON_SHIFT.setFocusable(false);
        BUTTON_SHIFT.setSelected(false);
        BUTTON_SPACE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setHybridMode(isHybridMode ? false : true);
            }
        });
        BUTTON_SPACE.setFocusable(false);
        
        //Add buttons to panel
        add(BUTTON_W);
        add(BUTTON_A);
        add(BUTTON_S);
        add(BUTTON_D);
        add(BUTTON_SHIFT);
        add(BUTTON_SPACE);
    }
    
    //Makes top button active
    public void setTopActive() {
        BUTTON_W.setSelected(true);
        BUTTON_A.setSelected(false);
        BUTTON_S.setSelected(false);
        BUTTON_D.setSelected(false);

        BUTTON_W.setEnabled(false);
        BUTTON_A.setEnabled(true);
        BUTTON_S.setEnabled(true);
        BUTTON_D.setEnabled(true);
    }
    
    //Makes mid-left button active
    public void setMidLeftActive() {
        BUTTON_W.setSelected(false);
        BUTTON_A.setSelected(true);
        BUTTON_S.setSelected(false);
        BUTTON_D.setSelected(false);

        BUTTON_W.setEnabled(true);
        BUTTON_A.setEnabled(false);
        BUTTON_S.setEnabled(true);
        BUTTON_D.setEnabled(true);
    }
    
    //Makes mid-right button active
    public void setMidRightActive() {
        BUTTON_W.setSelected(false);
        BUTTON_A.setSelected(false);
        BUTTON_S.setSelected(false);
        BUTTON_D.setSelected(true);

        BUTTON_W.setEnabled(true);
        BUTTON_A.setEnabled(true);
        BUTTON_S.setEnabled(true);
        BUTTON_D.setEnabled(false);
    }
    
    //Makes bottom button active
    public void setBottomActive() {
        BUTTON_W.setSelected(false);
        BUTTON_A.setSelected(false);
        BUTTON_S.setSelected(true);
        BUTTON_D.setSelected(false);

        BUTTON_W.setEnabled(true);
        BUTTON_A.setEnabled(true);
        BUTTON_S.setEnabled(false);
        BUTTON_D.setEnabled(true);
    }
    
    //Makes shift button active
    public void setMissedMode(boolean missed) {
        BUTTON_SHIFT.setSelected(missed);
        BUTTON_SHIFT.setText(missed ? "<html><p align = 'center'>Missed<br>(Shift)</p></html>" 
                                                       : "<html><p align = 'center'>Scored<br>(Shift)</p></html>");
    }
    
    //Set isHybridMode
    public void setHybridMode(boolean isHybridMode) {
        this.isHybridMode = isHybridMode;
        BUTTON_SPACE.setText((isHybridMode) ? "<html><p align = 'center'>Hybrid Period<br>(Space)</p></html>" 
                                            : "<html><p align = 'center'>Teleop Period<br>(Space)</p></html>");
    }
    
    //Returns whether or not in missed shot mode
    public boolean inMissedMode() {
        return BUTTON_SHIFT.isSelected();
    }
    
    //Returns isHybridMode
    public boolean isHybridMode() {
        return isHybridMode;
    }
    
    //Returns true if top is selected
    public boolean isTopSelected() {
        return BUTTON_W.isSelected();
    }
    
    //Returns true if mid-left is selected
    public boolean isMidLeftSelected() {
        return BUTTON_A.isSelected();
    }
    
    //Returns true if mid-right is selected
    public boolean isMidRightSelected() {
        return BUTTON_D.isSelected();
    }
    
    //Returns true if bottom is selected
    public boolean isBottomSelected() {
        return BUTTON_S.isSelected();
    }
    
    //Resets all data
    public void reset() {
        setHybridMode(true);
    }
}
