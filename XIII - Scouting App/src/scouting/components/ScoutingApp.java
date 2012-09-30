package scouting.components;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * 
 * @author Wilson Cheang, Peter Harquail
 */
public class ScoutingApp {
    private ButtonPanel buttonPanel;
    private PaintPanel paintPanel;
    private StatusPanel statusPanel;
    private ScoutForm scoutForm;
    
    public void buildGUI() {
        //Initializes frame
        JFrame frame = new JFrame("Team 610 Scouting Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200, 900));
        frame.setResizable(false);
        frame.setLayout(null);

        //Initialize components
        buttonPanel = new ButtonPanel(this);
        paintPanel = new PaintPanel(this);
        statusPanel = new StatusPanel(this);
        scoutForm = new ScoutForm(this);
        JSeparator vertSeparator = new JSeparator(SwingConstants.VERTICAL);
        JSeparator horizSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        JSeparator horizSeparatorB = new JSeparator(SwingConstants.HORIZONTAL);
        
        //Set component positions
        buttonPanel.setBounds(0, 650, 745, 200);
        paintPanel.setBounds(0, 0, 745, 324);
        statusPanel.setBounds(0, 328, 745, 320);
        scoutForm.setBounds(747, 0, 420, 900);
        vertSeparator.setBounds(745, 0, 2, 900);
        horizSeparator.setBounds(0, 325, 745, 2);
        horizSeparatorB.setBounds(0, 648, 745, 2);
        
        //Additional setup
        paintPanel.setBackgroundImage();
        paintPanel.requestFocusInWindow();
        
        //Adds components to the frame
        frame.getContentPane().add(buttonPanel);
        frame.getContentPane().add(paintPanel);
        frame.getContentPane().add(statusPanel);
        frame.getContentPane().add(scoutForm);
        frame.getContentPane().add(vertSeparator);
        frame.getContentPane().add(horizSeparator);
        frame.getContentPane().add(horizSeparatorB);
        
        //Pack frame
        frame.pack();
        
        //Make frame visible
        frame.setVisible(true);
    }
    
    //Returns the button panel
    protected ButtonPanel getButtonPanel() {
        return buttonPanel;
    }
    
    //Returns the paint panel
    protected PaintPanel getPaintPanel() {
        return paintPanel;
    }
    
    //Returns the status panel
    protected StatusPanel getStatusPanel() {
        return statusPanel;
    }
    
    //Returns the scout form
    protected ScoutForm getScoutForm() {
        return scoutForm;
    }
}
