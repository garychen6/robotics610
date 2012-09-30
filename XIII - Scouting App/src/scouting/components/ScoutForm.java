package scouting.components;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ListIterator;
import javax.swing.*;
import scouting.util.ScoutAppException;

/**
 *
 * @author Wilson Cheang, Peter Harquail
 */
public class ScoutForm extends JPanel {
    private ScoutingApp scoutApp;
    private int hybridScore, teleopScore;
    
    //Declare components
    private JButton saveButton;
    private JComboBox roundNumberCombo, allianceColourCombo;
    private JComboBox teleopBridgeAttemptCombo, teleopBridgeBalancedCombo, teleopBridgeCombo,
                      teleopCrossedBarrierCombo, teleopCrossedBridgeCombo;
    private JLabel titleLabel, teamNumberLabel, matchNumberLabel, 
                   roundNumberLabel, allianceColourLabel, driveRatingLabel, 
                   offenceRatingLabel, defenceRatingLabel,scoutNameLabel;
    private JLabel hybridPeriodLabel, hybridScoreLabel, hybridCommentsLabel, 
                   hybridFoulLabel, hybridTechFoulLabel;
    private JLabel teleopPeriodLabel, teleopScoreLabel, teleopFoulLabel, 
                   teleopTechFoulLabel, teleopBridgeAttemptLabel, teleopCrossedBarrierLabel,
                   teleopCrossedBridgeLabel, teleopBridgeBalancedLabel, teleopBridgeLabel, 
                   teleopCommentsLabel;
    private JScrollPane hybridCommentsScroll, teleopCommentsScroll;
    private JSpinner hybridFoulSpinner, hybridTechFoulSpinner,
                     teleopFoulSpinner, teleopTechFoulSpinner;
    private JSpinner teamNumberSpinner, matchNumberSpinner, driveRatingSpinner, 
                     offenceRatingSpinner, defenceRatingSpinner;
    private JTextField scoutNameField;
    private JTextArea hybridCommentsArea, teleopCommentsArea;
    private SpinnerNumberModel hybridFoulModel, hybridTechFoulModel,
                               teleopFoulModel, teleopTechFoulModel;
    private SpinnerNumberModel teamNumberModel, matchNumberModel, driveRatingModel, 
                               offenceRatingModel, defenceRatingModel;

    public ScoutForm(final ScoutingApp scoutApp) {
        this.scoutApp = scoutApp;
        
        //Initialize panel
        setMinimumSize(new Dimension(400, 900));
        setMaximumSize(new Dimension(400, 900));
        setPreferredSize(new Dimension(400, 900));
        setLayout(null);
        
        //Initialize non-components
        teamNumberModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        matchNumberModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        hybridScore = 0;
        hybridFoulModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        hybridTechFoulModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        teleopScore = 0;
        teleopFoulModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        teleopTechFoulModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        driveRatingModel = new SpinnerNumberModel(0, -1, 10, 1);
        offenceRatingModel = new SpinnerNumberModel(0, -1, 10, 1);
        defenceRatingModel = new SpinnerNumberModel(0, -1, 10, 1);
        
        //Initialize components
        titleLabel = new JLabel("Team 610 Scouting Form");
        teamNumberLabel = new JLabel("Team number:");
        teamNumberSpinner = new JSpinner(teamNumberModel);
        matchNumberLabel = new JLabel("Match Number:");
        matchNumberSpinner = new JSpinner(matchNumberModel);
        roundNumberLabel = new JLabel("Round Number:");
        roundNumberCombo = new JComboBox();
        allianceColourLabel = new JLabel("Alliance Colour:");
        allianceColourCombo = new JComboBox();
        hybridPeriodLabel = new JLabel("Hybrid Period");
        hybridScoreLabel = new JLabel("Score: " + hybridScore);
        hybridFoulLabel = new JLabel("Fouls committed:");
        hybridFoulSpinner = new JSpinner(hybridFoulModel);
        hybridTechFoulLabel = new JLabel("Technical fouls committed:");
        hybridTechFoulSpinner = new JSpinner(hybridTechFoulModel);
        hybridCommentsLabel = new JLabel("Comments:");
        hybridCommentsArea = new JTextArea();
        hybridCommentsScroll = new JScrollPane(hybridCommentsArea, 
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        teleopPeriodLabel = new JLabel("Teleop Period");
        teleopScoreLabel = new JLabel("Score: " + teleopScore);
        teleopBridgeAttemptLabel = new JLabel("Did the robot attempt balance a bridge?");
        teleopBridgeAttemptCombo = new JComboBox();
        teleopBridgeLabel = new JLabel("Which Bridge?");
        teleopBridgeCombo = new JComboBox();
        teleopBridgeBalancedLabel = new JLabel("Was the bridge balanced?");
        teleopBridgeBalancedCombo = new JComboBox();
        teleopFoulLabel = new JLabel("Fouls committed:");
        teleopFoulSpinner = new JSpinner(teleopFoulModel);
        teleopTechFoulLabel = new JLabel("Technical fouls committed:");
        teleopTechFoulSpinner = new JSpinner(teleopTechFoulModel);
        teleopCrossedBarrierLabel = new JLabel("Crossed barrier?");
        teleopCrossedBarrierCombo = new JComboBox();
        teleopCrossedBridgeLabel = new JLabel("Crossed bridge?");
        teleopCrossedBridgeCombo = new JComboBox();
        driveRatingLabel = new JLabel("Rate driving ability:");
        driveRatingSpinner = new JSpinner(driveRatingModel);
        offenceRatingLabel = new JLabel("Rate offence capability:");
        offenceRatingSpinner = new JSpinner(offenceRatingModel);
        defenceRatingLabel = new JLabel("Rate defence capability:");
        defenceRatingSpinner = new JSpinner(defenceRatingModel);
        teleopCommentsLabel = new JLabel("Comments:");
        teleopCommentsArea = new JTextArea();
        teleopCommentsScroll = new JScrollPane(teleopCommentsArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scoutNameLabel = new JLabel("Scout name:");
        scoutNameField = new JTextField();
        saveButton = new JButton("Save Report");
        
        //Set component locations
        titleLabel.setBounds(150, 20, 200, 14);
        matchNumberLabel.setBounds(20, 55, 200, 24);
        matchNumberSpinner.setBounds(110, 55, 50, 24);
        roundNumberLabel.setBounds(273, 55, 200, 24);
        roundNumberCombo.setBounds(366, 55, 50, 24);
        teamNumberLabel.setBounds(20, 98, 200, 14);
        teamNumberSpinner.setBounds(110, 93, 50, 24);
        allianceColourLabel.setBounds(273, 98, 200, 14);
        allianceColourCombo.setBounds(366, 93, 50, 24);
        hybridPeriodLabel.setBounds(20, 135, 200, 24);
        hybridScoreLabel.setBounds(355, 135, 200, 24);
        hybridFoulLabel.setBounds(20, 175, 200, 14);
        hybridFoulSpinner.setBounds(130, 170, 42, 24);
        hybridTechFoulLabel.setBounds(210, 175, 250, 14);
        hybridTechFoulSpinner.setBounds(377, 170, 42, 24);
        hybridCommentsLabel.setBounds(20, 210, 200, 14);
        hybridCommentsScroll.setBounds(25, 230, 395, 100);
        teleopPeriodLabel.setBounds(20, 345, 200, 24);
        teleopScoreLabel.setBounds(355, 345, 200, 24);
        teleopFoulLabel.setBounds(20, 385, 200, 14);
        teleopFoulSpinner.setBounds(130, 380, 42, 24);
        teleopTechFoulLabel.setBounds(210, 385, 250, 14);
        teleopTechFoulSpinner.setBounds(377, 380, 42, 24);
        teleopBridgeAttemptLabel.setBounds(20, 420, 250, 24);
        teleopBridgeAttemptCombo.setBounds(370, 420, 49, 24);
        teleopBridgeBalancedLabel.setBounds(20, 455, 250, 24);
        teleopBridgeBalancedCombo.setBounds(370, 455, 49, 24);
        teleopBridgeLabel.setBounds(20, 495, 250, 24);
        teleopBridgeCombo.setBounds(360, 495, 59, 24);
        teleopCrossedBarrierLabel.setBounds(20, 535, 150, 24);
        teleopCrossedBarrierCombo.setBounds(122, 535, 50, 24);
        teleopCrossedBridgeLabel.setBounds(270, 535, 150, 24);
        teleopCrossedBridgeCombo.setBounds(370, 535, 50, 24);
        driveRatingLabel.setBounds(20, 570, 200, 24);
        driveRatingSpinner.setBounds(377, 570, 42, 24);
        offenceRatingLabel.setBounds(20, 605, 200, 24);
        offenceRatingSpinner.setBounds(377, 605, 42, 24);
        defenceRatingLabel.setBounds(20, 640, 200, 24);
        defenceRatingSpinner.setBounds(377, 640, 42, 24);
        teleopCommentsLabel.setBounds(20, 680, 200, 14);
        teleopCommentsScroll.setBounds(25, 700, 395, 100);
        scoutNameLabel.setBounds(20, 820, 150, 24);
        scoutNameField.setBounds(100, 820, 150, 24);
        saveButton.setBounds(293, 815, 125, 35);

        //Additional Setup
        roundNumberCombo.addItem(1);
        roundNumberCombo.addItem(2);
        roundNumberCombo.addItem(3);
        allianceColourCombo.addItem("Red");
        allianceColourCombo.addItem("Blue");
        hybridCommentsArea.setLineWrap(true);
        teleopBridgeAttemptCombo.addItem("No");
        teleopBridgeAttemptCombo.addItem("Yes");
        teleopBridgeAttemptCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if(teleopBridgeAttemptCombo.getSelectedItem().toString().equals("sNo")) {
                    teleopBridgeBalancedCombo.setEnabled(false);
                    teleopBridgeCombo.setEnabled(false);
                } else {
                    teleopBridgeBalancedCombo.setEnabled(true);
                    teleopBridgeCombo.setEnabled(true);
                }
            }
        });
        teleopBridgeBalancedCombo.addItem("No");
        teleopBridgeBalancedCombo.addItem("Yes");
        teleopBridgeBalancedCombo.setEnabled(false);
        teleopBridgeCombo.addItem("Red");
        teleopBridgeCombo.addItem("Blue");
        teleopBridgeCombo.addItem("Co-op");
        teleopBridgeCombo.setEnabled(false);
        teleopCrossedBarrierCombo.addItem("No");
        teleopCrossedBarrierCombo.addItem("Yes");
        teleopCrossedBridgeCombo.addItem("No");
        teleopCrossedBridgeCombo.addItem("Yes");
        teleopCommentsArea.setLineWrap(true);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                saveReport();
                scoutApp.getButtonPanel().reset();
                scoutApp.getPaintPanel().reset();
                scoutApp.getStatusPanel().reset();
                resetAfterSave();
            }
        });
        saveButton.setFocusable(false);
        
        //Add components to panel
        add(titleLabel);
        add(teamNumberLabel);
        add(teamNumberSpinner);
        add(matchNumberLabel);
        add(matchNumberSpinner);
        add(roundNumberLabel);
        add(roundNumberCombo);
        add(allianceColourLabel);
        add(allianceColourCombo);
        add(hybridScoreLabel);
        add(hybridPeriodLabel);
        add(hybridFoulLabel);
        add(hybridFoulSpinner);
        add(hybridTechFoulLabel);
        add(hybridTechFoulSpinner);
        add(hybridCommentsLabel);
        add(hybridCommentsScroll);
        add(teleopPeriodLabel);
        add(teleopScoreLabel);
        add(teleopFoulLabel);
        add(teleopFoulSpinner);
        add(teleopTechFoulLabel);
        add(teleopTechFoulSpinner);
        add(teleopBridgeAttemptLabel);
        add(teleopBridgeAttemptCombo);
        add(teleopBridgeBalancedLabel);
        add(teleopBridgeBalancedCombo);
        add(teleopBridgeLabel);
        add(teleopBridgeCombo);
        add(teleopCrossedBarrierLabel);
        add(teleopCrossedBarrierCombo);
        add(teleopCrossedBridgeLabel);
        add(teleopCrossedBridgeCombo);
        add(driveRatingLabel);
        add(driveRatingSpinner);
        add(offenceRatingLabel);
        add(offenceRatingSpinner);
        add(defenceRatingLabel);
        add(defenceRatingSpinner);
        add(teleopCommentsLabel);
        add(teleopCommentsScroll);
        add(scoutNameLabel);
        add(scoutNameField);
        add(saveButton);
    }

    //Update this panel
    protected void update() {
        hybridScore = (6 * scoutApp.getPaintPanel().getHybridTop().size()) + 
                      (5 * (scoutApp.getPaintPanel().getHybridMidLeft().size() +
                            scoutApp.getPaintPanel().getHybridMidRight().size())) +
                      (4 * scoutApp.getPaintPanel().getHybridBottom().size());
        teleopScore = (3 * scoutApp.getPaintPanel().getTeleopTop().size()) + 
                      (2 * (scoutApp.getPaintPanel().getTeleopMidLeft().size() +
                            scoutApp.getPaintPanel().getTeleopMidRight().size())) +
                      (scoutApp.getPaintPanel().getTeleopBottom().size());
        hybridScoreLabel.setText("Score: " + hybridScore);
        teleopScoreLabel.setText("Score: " + teleopScore);
        repaint();
    }
    
    //Reset all data
    protected void reset() {
        roundNumberCombo.setSelectedIndex(0);
        teamNumberSpinner.setValue(1);
        allianceColourCombo.setSelectedIndex(0);
        hybridFoulSpinner.setValue(0);
        hybridTechFoulSpinner.setValue(0);
        hybridCommentsArea.setText("");
        teleopFoulSpinner.setValue(0);
        teleopTechFoulSpinner.setValue(0);
        teleopBridgeAttemptCombo.setSelectedIndex(0);
        teleopBridgeBalancedCombo.setSelectedIndex(0);
        teleopBridgeBalancedCombo.setEnabled(false);
        teleopBridgeCombo.setSelectedIndex(0);
        teleopBridgeCombo.setEnabled(false);
        teleopCrossedBarrierCombo.setSelectedIndex(0);
        teleopCrossedBridgeCombo.setSelectedIndex(0);
        driveRatingSpinner.setValue(0);
        offenceRatingSpinner.setValue(0);
        defenceRatingSpinner.setValue(0);
        teleopCommentsArea.setText("");
        scoutNameField.setText("");
        update();
    }
    
    private void resetAfterSave() {
        matchNumberSpinner.setValue(Integer.parseInt(matchNumberSpinner.getValue().toString()) + 1);
        roundNumberCombo.setSelectedIndex(0);
        teamNumberSpinner.setValue(1);
        allianceColourCombo.setSelectedIndex(0);
        hybridFoulSpinner.setValue(0);
        hybridTechFoulSpinner.setValue(0);
        hybridCommentsArea.setText("");
        teleopFoulSpinner.setValue(0);
        teleopTechFoulSpinner.setValue(0);
        teleopBridgeAttemptCombo.setSelectedIndex(0);
        teleopBridgeBalancedCombo.setSelectedIndex(0);
        teleopBridgeBalancedCombo.setEnabled(false);
        teleopBridgeCombo.setSelectedIndex(0);
        teleopBridgeCombo.setEnabled(false);
        teleopCrossedBarrierCombo.setSelectedIndex(0);
        teleopCrossedBridgeCombo.setSelectedIndex(0);
        driveRatingSpinner.setValue(0);
        offenceRatingSpinner.setValue(0);
        defenceRatingSpinner.setValue(0);
        teleopCommentsArea.setText("");
        scoutNameField.setText("");
        update();
    }
    
    //Outputs data to a file
    protected void saveReport() {
       try{
            if(scoutNameField.getText().length() == 0) throw new ScoutAppException("Please enter your name.");
           
            new File("scouting/output/").mkdirs();
            File report = new File("scouting/output/M" + matchNumberSpinner.getValue() + "_R" 
                + roundNumberCombo.getSelectedItem().toString() + "_T" + teamNumberSpinner.getValue() + ".xml");
        
            if(!report.exists()) report.createNewFile();
            
            FileWriter reporter = new FileWriter(report);
                    
            reporter.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            reporter.write("\r\n<report>");
                reporter.write("\r\n\t<scoutName>" + scoutNameField.getText() + "</scoutName>");
                reporter.write("\r\n\t<matchNum>" + matchNumberSpinner.getValue() + "</matchNum>");
                reporter.write("\r\n\t<roundNum>" + roundNumberCombo.getSelectedItem().toString() + "</roundNum>");
                reporter.write("\r\n\t<alliance>" + allianceColourCombo.getSelectedItem().toString() + "</alliance>");
                reporter.write("\r\n\t<teamNum>" + teamNumberSpinner.getValue() + "</teamNum>");
                reporter.write("\r\n\t<hybridPeriod>");
                    reporter.write("\r\n\t\t<scored>");
                    if(scoutApp.getPaintPanel().getHybridTop().size() > 0) {
                        reporter.write("\r\n\t\t\t<top>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getHybridTop().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</top>");
                    }
                    if(scoutApp.getPaintPanel().getHybridMidLeft().size() > 0) {
                        reporter.write("\r\n\t\t\t<midLeft>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getHybridMidLeft().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</midLeft>");
                    }
                    if(scoutApp.getPaintPanel().getHybridMidRight().size() > 0) {
                        reporter.write("\r\n\t\t\t<midRight>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getHybridMidRight().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</midRight>");
                    }
                    if(scoutApp.getPaintPanel().getHybridBottom().size() > 0) {
                        reporter.write("\r\n\t\t\t<bottom>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getHybridBottom().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</bottom>");
                    }
                    reporter.write("\r\n\t\t</scored>");
                    reporter.write("\r\n\t\t<missed>");
                    if(scoutApp.getPaintPanel().getMissedHybridTop().size() > 0) {
                        reporter.write("\r\n\t\t\t<top>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getMissedHybridTop().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</top>");
                    }
                    if(scoutApp.getPaintPanel().getMissedHybridMidLeft().size() > 0) {
                        reporter.write("\r\n\t\t\t<midLeft>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getMissedHybridMidLeft().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</midLeft>");
                    }
                    if(scoutApp.getPaintPanel().getMissedHybridMidRight().size() > 0) {
                        reporter.write("\r\n\t\t\t<midRight>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getMissedHybridMidRight().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</midRight>");
                    }
                    if(scoutApp.getPaintPanel().getMissedHybridBottom().size() > 0) {
                        reporter.write("\r\n\t\t\t<bottom>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getMissedHybridBottom().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</bottom>");
                    }
                    reporter.write("\r\n\t\t</missed>");
                    reporter.write("\r\n\t\t<foul>" + hybridFoulSpinner.getValue() + "</foul>");
                    reporter.write("\r\n\t\t<techFoul>" + hybridTechFoulSpinner.getValue() + "</techFoul>");
                    reporter.write("\r\n\t\t<hybridScore>" + hybridScore + "</hybridScore>");
                if(hybridCommentsArea.getText().length() > 0) {
                    reporter.write("\r\n\t\t<comments>");
                        reporter.write("\r\n\t\t\t" + hybridCommentsArea.getText());
                    reporter.write("\r\n\t\t</comments>");
                }
                reporter.write("\r\n\t</hybridPeriod>");
                reporter.write("\r\n\t<teleopPeriod>");
                    reporter.write("\r\n\t\t<scored>");
                    if(scoutApp.getPaintPanel().getTeleopTop().size() > 0) {
                        reporter.write("\r\n\t\t\t<top>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getTeleopTop().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</top>");
                    }
                    if(scoutApp.getPaintPanel().getTeleopMidLeft().size() > 0) {
                        reporter.write("\r\n\t\t\t<midLeft>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getTeleopMidLeft().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</midLeft>");
                    }
                    if(scoutApp.getPaintPanel().getTeleopMidRight().size() > 0) {
                        reporter.write("\r\n\t\t\t<midRight>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getTeleopMidRight().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</midRight>");
                    }
                    if(scoutApp.getPaintPanel().getTeleopBottom().size() > 0) {
                        reporter.write("\r\n\t\t\t<bottom>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getTeleopBottom().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</bottom>");
                    }
                    reporter.write("\r\n\t\t</scored>");
                    reporter.write("\r\n\t\t<missed>");
                    if(scoutApp.getPaintPanel().getMissedTeleopTop().size() > 0) {
                        reporter.write("\r\n\t\t\t<top>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getMissedTeleopTop().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</top>");
                    }
                    if(scoutApp.getPaintPanel().getMissedTeleopMidLeft().size() > 0) {
                        reporter.write("\r\n\t\t\t<midLeft>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getMissedTeleopMidLeft().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</midLeft>");
                    }
                    if(scoutApp.getPaintPanel().getMissedTeleopMidRight().size() > 0) {
                        reporter.write("\r\n\t\t\t<midRight>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getMissedTeleopMidRight().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</midRight>");
                    }
                    if(scoutApp.getPaintPanel().getMissedTeleopBottom().size() > 0) {
                        reporter.write("\r\n\t\t\t<bottom>");
                        for(ListIterator<Point> it = scoutApp.getPaintPanel().getMissedTeleopBottom().listIterator(); 
                                it.hasNext();) {
                            Point p = it.next();
                            reporter.write("\r\n\t\t\t\t<point>");
                                reporter.write("\r\n\t\t\t\t\t<x>" + p.x + "</x>");
                                reporter.write("\r\n\t\t\t\t\t<y>" + p.y + "</y>");
                            reporter.write("\r\n\t\t\t\t</point>");
                        }
                        reporter.write("\r\n\t\t\t</bottom>");
                    }
                    reporter.write("\r\n\t\t</missed>");
                    reporter.write("\r\n\t\t<foul>" + teleopFoulSpinner.getValue() + "</foul>");
                    reporter.write("\r\n\t\t<techFoul>" + teleopTechFoulSpinner.getValue() + "</techFoul>");
                    reporter.write("\r\n\t\t<teleopScore>" + teleopScore + "</teleopScore>");
                    reporter.write("\r\n\t\t<bridge>");
                        reporter.write("\r\n\t\t\t<attempted>" + teleopBridgeAttemptCombo.getSelectedItem().toString().equals("Yes") + "</attempted>");
                    if(teleopBridgeAttemptCombo.getSelectedItem().toString().equals("Yes")) {
                        reporter.write("\r\n\t\t\t<balanced>" + teleopBridgeBalancedCombo.getSelectedItem().toString().equals("Yes") + "</balanced>");
                        reporter.write("\r\n\t\t\t<bridgeType>" + teleopBridgeCombo.getSelectedItem().toString() + "</bridgeType>");
                    }
                    reporter.write("\r\n\t\t</bridge>");
                    reporter.write("\r\n\t\t<mobility>");
                        reporter.write("\r\n\t\t\t<crossedBarrier>" + teleopCrossedBarrierCombo.getSelectedItem().toString().equals("Yes") + "</crossedBarrier>");
                        reporter.write("\r\n\t\t\t<crossedBridge>" + teleopCrossedBridgeCombo.getSelectedItem().toString().equals("Yes") + "</crossedBridge>");
                    reporter.write("\r\n\t\t</mobility>");
                if(teleopCommentsArea.getText().length() > 0) {
                    reporter.write("\r\n\t\t<comments>");
                        reporter.write("\r\n\t\t\t" + teleopCommentsArea.getText());
                    reporter.write("\r\n\t\t</comments>");
                }
                reporter.write("\r\n\t</teleopPeriod>");
                reporter.write("\r\n\t<driveRating>" + driveRatingSpinner.getValue() + "</driveRating>");
                reporter.write("\r\n\t<offenceRating>" + offenceRatingSpinner.getValue() + "</offenceRating>");
                reporter.write("\r\n\t<defenceRating>" + defenceRatingSpinner.getValue() + "</defenceRating>");
                reporter.write("\r\n\t<endScore>" + (hybridScore + teleopScore) + "</endScore>");
            reporter.write("\r\n</report>");
            
            reporter.close();
        } catch (ScoutAppException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "An Error has Occurred", JOptionPane.ERROR_MESSAGE);
        }
    }
}
