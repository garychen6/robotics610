package scouting.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Wilson Cheang, Peter Harquail
 */
public class StatusPanel extends JPanel {
    private ScoutingApp scoutApp;
    
    private JButton resetButton;
    private JLabel hybridLabel, teleopLabel;
    private JLabel topLabel, midLeftLabel, midRightLabel, bottomLabel, totalLabel;
    private JLabel hybridTopLabel, hybridMidLeftLabel, hybridMidRightLabel, 
                       hybridBottomLabel, hybridTotalLabel, teleopTopLabel, 
                       teleopMidLeftLabel,teleopMidRightLabel, teleopBottomLabel,
                       teleopTotalLabel;
    
    public StatusPanel(final ScoutingApp scoutApp) {
        this.scoutApp = scoutApp;
        
        //Initialize panel
        setMinimumSize(new Dimension(745, 320));
        setMaximumSize(new Dimension(745, 320));
        setPreferredSize(new Dimension(745, 320));
        setLayout(null);
        
        //Initialize components
        hybridLabel = new JLabel("Hybrid Period");
        teleopLabel = new JLabel("Teleop Period");
        topLabel = new JLabel("Top");
        midLeftLabel = new JLabel("Mid-Left");
        midRightLabel = new JLabel("Mid-Right");
        bottomLabel = new JLabel("Bottom");
        totalLabel = new JLabel("Total");
        hybridTopLabel = new JLabel("N/A");
        hybridMidLeftLabel = new JLabel("N/A");
        hybridMidRightLabel = new JLabel("N/A");
        hybridBottomLabel = new JLabel("N/A");
        hybridTotalLabel = new JLabel("N/A");
        teleopTopLabel = new JLabel("N/A");
        teleopMidLeftLabel = new JLabel("N/A");
        teleopMidRightLabel = new JLabel("N/A");
        teleopBottomLabel = new JLabel("N/A");
        teleopTotalLabel = new JLabel("N/A");
        resetButton = new JButton("<html><p align = 'center'>Reset</p></html>");
        
        //Set component locations
        hybridLabel.setBounds(300, 20, 200, 24);
        teleopLabel.setBounds(500, 20, 200, 24);
        topLabel.setBounds(150, 65, 200, 24);
        midLeftLabel.setBounds(150, 100, 200, 24);
        midRightLabel.setBounds(150, 140, 200, 24);
        bottomLabel.setBounds(150, 180, 200, 24);
        totalLabel.setBounds(150, 220, 200, 24);
        hybridTopLabel.setBounds(325, 65, 200, 24);
        hybridMidLeftLabel.setBounds(325, 100, 200, 24);
        hybridMidRightLabel.setBounds(325, 140, 200, 24);
        hybridBottomLabel.setBounds(325, 180, 200, 24);
        hybridTotalLabel.setBounds(325, 220, 200, 24);
        teleopTopLabel.setBounds(525, 65, 200, 24);
        teleopMidLeftLabel.setBounds(525, 100, 200, 24);
        teleopMidRightLabel.setBounds(525, 140, 200, 24);
        teleopBottomLabel.setBounds(525, 180, 200, 24);
        teleopTotalLabel.setBounds(525, 220, 200, 24);
        resetButton.setBounds(620, 280, 100, 24);
        
        //Additional setup
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Object[] options = {"Clear all data!", "No, please don't! I'm not done yet!"};
                if(JOptionPane.showOptionDialog(new JFrame(), "Are you sure you want to clear all data?", "Clear data?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]) == 0) {
                    scoutApp.getButtonPanel().reset();
                    scoutApp.getPaintPanel().reset();
                    scoutApp.getScoutForm().reset();
                    reset();
                }
            }
        });
        
        //Add components to panel
        add(hybridLabel);
        add(teleopLabel);
        add(topLabel);
        add(midLeftLabel);
        add(midRightLabel);
        add(bottomLabel);
        add(totalLabel);
        add(hybridTopLabel);
        add(hybridMidLeftLabel);
        add(hybridMidRightLabel);
        add(hybridBottomLabel);
        add(hybridTotalLabel);
        add(teleopTopLabel);
        add(teleopMidLeftLabel);
        add(teleopMidRightLabel);
        add(teleopBottomLabel);
        add(teleopTotalLabel);
        add(resetButton);
    }
    
    //Update panel data and repaint
    public void update() {
        int hT = scoutApp.getPaintPanel().getHybridTop().size();
        int hL = scoutApp.getPaintPanel().getHybridMidLeft().size();
        int hR = scoutApp.getPaintPanel().getHybridMidRight().size();
        int hB = scoutApp.getPaintPanel().getHybridBottom().size();
        int hMT = scoutApp.getPaintPanel().getMissedHybridTop().size();
        int hML = scoutApp.getPaintPanel().getMissedHybridMidLeft().size();
        int hMR = scoutApp.getPaintPanel().getMissedHybridMidRight().size();
        int hMB = scoutApp.getPaintPanel().getMissedHybridBottom().size();
        int tT = scoutApp.getPaintPanel().getTeleopTop().size();
        int tL = scoutApp.getPaintPanel().getTeleopMidLeft().size();
        int tR = scoutApp.getPaintPanel().getTeleopMidRight().size();
        int tB = scoutApp.getPaintPanel().getTeleopBottom().size();
        int tMT = scoutApp.getPaintPanel().getMissedTeleopTop().size();
        int tML = scoutApp.getPaintPanel().getMissedTeleopMidLeft().size();
        int tMR = scoutApp.getPaintPanel().getMissedTeleopMidRight().size();
        int tMB = scoutApp.getPaintPanel().getMissedTeleopBottom().size();
        
        hybridTopLabel.setText(hT + " / " + (hT + hMT));
        hybridMidLeftLabel.setText(hL + " / " + (hL + hML));
        hybridMidRightLabel.setText(hR + " / " + (hR + hMR));
        hybridBottomLabel.setText(hB + " / " + (hB + hMB));
        hybridTotalLabel.setText((hT + hL + hR + hB) + " / " + (hT + hMT + hL + hML + hR + hMR + hB + hMB));
        teleopTopLabel.setText(tT + " / " + (tT + tMT));
        teleopMidLeftLabel.setText(tL + " / " + (tL + tML));
        teleopMidRightLabel.setText(tR + " / " + (tR + tMR));
        teleopBottomLabel.setText(tB + " / " +  (tB + tMB));
        teleopTotalLabel.setText((tT + tL + tR + tB) + " / " + (tT + tMT + tL + tML + tR + tMR + tB + tMB));
        
        repaint();
    }
    
    //Reset data and repaint
    public void reset() {
        hybridTopLabel.setText("N/A");
        hybridMidLeftLabel.setText("N/A");
        hybridMidRightLabel.setText("N/A");
        hybridBottomLabel.setText("N/A");
        hybridTotalLabel.setText("N/A");
        teleopTopLabel.setText("N/A");
        teleopMidLeftLabel.setText("N/A");
        teleopMidRightLabel.setText("N/A");
        teleopBottomLabel.setText("N/A");
        teleopTotalLabel.setText("N/A");
        
        repaint();
    }
}
