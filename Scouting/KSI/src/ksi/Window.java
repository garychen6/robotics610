/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Warfa
 */
public class Window extends JFrame {

    JFormattedTextField t;
    JFormattedTextField m;
    JFormattedTextField shotsFromAuton_TF;
    JFormattedTextField floorPickup;
    JFormattedTextField shotsFromTeleOp;
    JFormattedTextField hangLvl;
    JFormattedTextField timeToHang;
    JFormattedTextField hangRating;
    JFormattedTextField loc_A;
    JFormattedTextField s_A;
    JFormattedTextField a_A;
    JFormattedTextField s_T;
    JFormattedTextField a_T;
    JFormattedTextField d;
    JTextArea comments;
    JPanel form;
    NumberFormat nums;
    String tN;
    String mN;
    String shotsFromAuton;
    String s_AN;
    String a_AN;
    String s_TN;
    String a_TN;
    String dN;
    String floorPickup;
    String shotsFromTeleOp;
    String hangLvl;
    String timetoHang;
    String hangRating;
    String comment;
    String dataBasePath;
    FileWriter writer;
    ArrayList teams;
    DataBaseClient dataBase;

    Window() {
        super("Kevin Scouting Interface");
        teams = new ArrayList();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nums = NumberFormat.getNumberInstance();
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F1);
        JMenuItem newItem = new JMenuItem("New");
        dataBasePath = JOptionPane.showInputDialog("DataBase FilePath?\nThis is where team folders will be created.\n*NOTE:This must be a real path.* ");
        dataBase = new DataBaseClient(dataBasePath);
        reset();
        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    try {
                        saveFile();
                    } catch (ParseException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        file.add(newItem);
        file.add(save);
        file.add(exit);
        menu.add(file);
        setJMenuBar(menu);
        // show application
        setLocation(32, 32);
        setSize(500, 700);
        setVisible(true);
        pack();
        // repaint();
    }

    public void clearForm() {
        t.setValue(0);
        m.setValue(0);
        loc_A.setValue("");
        s_A.setValue(0);
        a_A.setValue(0);
        s_T.setValue(0);
        a_T.setValue(0);
        d.setValue(0);
        comments_A.setText("ADD AUTON COMMENTS HERE");
        comments_T.setText("ADD TELE-OP COMMENTS HERE");

    }

    public void reset() {
        JLabel tL = new JLabel("Team #");
        JLabel mL = new JLabel("Match #");
        JLabel locL = new JLabel("Where Scored");
        JLabel autL = new JLabel("Autonomous");
        JLabel s_AL = new JLabel("Shots Scored");
        JLabel a_AL = new JLabel("Shots Attempted");
        JLabel cAL = new JLabel("Auton Comments");
        JLabel teleL = new JLabel("Tele-Operated");
        JLabel s_TL = new JLabel("Shots Scored");
        JLabel a_TL = new JLabel("Shots Attempted");
        JLabel dL = new JLabel("Defensive Rating [1-10]");
        t = new JFormattedTextField("");
        t.setColumns(5);
        t.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                tN = t.getText();
            }
        });
        m = new JFormattedTextField("");
        m.setColumns(5);
        m.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                mN = m.getText();
            }
        });
        loc_A = new JFormattedTextField();
        loc_A.setValue(new String());
        loc_A.setColumns(5);
        loc_A.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                shotsFromAuton = loc_A.getText();
            }
        });
        s_A = new JFormattedTextField(nums);
        s_A.setValue(new Integer(0));
        s_A.setColumns(5);
        s_A.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                s_AN = s_A.getText();
            }
        });
        a_A = new JFormattedTextField(nums);
        a_A.setValue(new Integer(0));
        a_A.setColumns(5);
        a_A.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                a_AN = a_A.getText();
            }
        });
        s_T = new JFormattedTextField(nums);
        s_T.setValue(new Integer(0));
        s_T.setColumns(5);
        s_T.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                s_TN = s_T.getText();
            }
        });
        a_T = new JFormattedTextField(nums);
        a_T.setValue(new Integer(0));
        a_T.setColumns(5);
        a_T.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                a_TN = a_T.getText();
            }
        });
        d = new JFormattedTextField(nums);
        d.setValue(new Integer(0));
        d.setColumns(5);
        d.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                dN = d.getText();
            }
        });
        commentA = "ADD AUTON COMMENTS HERE";
        comments_A = new JTextArea(commentA);
        comments_A.setFont(new Font("Serif", Font.PLAIN, 12));
        comments_A.setLineWrap(true);
        comments_A.setWrapStyleWord(true);
        JScrollPane comments_AScroll = new JScrollPane(comments_A);
        comments_AScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        comments_AScroll.setPreferredSize(new Dimension(100, 100));
        comment = "ADD TELE-OP COMMENTS HERE";
        comments_T = new JTextArea(comment);
        comments_T.setFont(new Font("Serif", Font.PLAIN, 12));
        comments_T.setLineWrap(true);
        comments_T.setWrapStyleWord(true);
        JScrollPane comments_TScroll = new JScrollPane(comments_T);
        comments_TScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        comments_TScroll.setPreferredSize(new Dimension(100, 100));
        JPanel formLTop = new JPanel(new GridLayout(0, 1));
        formLTop.add(tL);
        formLTop.add(mL);
        formLTop.add(locL);
        JPanel formTop = new JPanel(new GridLayout(0, 1));
        formTop.add(t);
        formTop.add(m);
        formTop.add(loc_A);
        JPanel formLMid = new JPanel(new GridLayout(0, 1));
        formLMid.add(s_AL);
        formLMid.add(a_AL);
        JPanel formMid = new JPanel(new GridLayout(0, 1));
        formMid.add(s_A);
        formMid.add(a_A);
        JPanel formLBottom = new JPanel(new GridLayout(0, 1));
        formLBottom.add(s_TL);
        formLBottom.add(a_TL);
        formLBottom.add(dL);
        JPanel formBottom = new JPanel(new GridLayout(0, 1));
        formBottom.add(s_T);
        formBottom.add(a_T);
        formBottom.add(d);
        JPanel top = new JPanel(new GridLayout(0, 2));
        top.add(formLTop);
        top.add(formTop);
        JPanel middle = new JPanel(new GridLayout(0, 2));
        middle.add(formLMid);
        middle.add(formMid);
        JPanel bottom = new JPanel(new GridLayout(0, 2));
        bottom.add(formLBottom);
        bottom.add(formBottom);
        JPanel form = new JPanel(new GridLayout(0, 1));
        form.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        form.add(top);
        form.add(autL);
        form.add(middle);
        form.add(comments_A);
        form.add(teleL);
        form.add(bottom);
        form.add(comments_T);
        form.setBackground(Color.GRAY);
        add(form);
        form.setVisible(true);
    }

    void commitEdit() throws ParseException {
        t.commitEdit();
        m.commitEdit();
        loc_A.commitEdit();
        s_A.commitEdit();
        a_A.commitEdit();
        s_T.commitEdit();
        a_T.commitEdit();
        d.commitEdit();
    }

    public void saveFile() throws IOException, ParseException {
        commitEdit();
        commentA = comments_A.getText();
        comment = comments_T.getText();
        makeEntry();
        clearForm();
    }
    
    public void updateTeam(String team) throws FileNotFoundException{
        dataBase.update(team);
    }
    public void makeEntry() throws IOException {
        if (!teams.contains(tN)) {
            teams.add(tN);
            File dir = new File(dataBasePath + "\\" + tN);
            dir.mkdir();
        }
        FileWriter fl = new FileWriter(dataBasePath + "\\" + tN + "\\" + tN + "-" + mN + ".txt");
        fl.write(shotsFromAuton + "@");
        fl.write(s_AN + "@");
        fl.write(a_AN+"@");
        fl.write(s_TN+"@");
        fl.write(a_TN+"@");
        fl.write(dN+"@");
        fl.write(commentA+"@");
        fl.write(comment+"@");
        fl.close();
    }

    public static void main(String[] args) {
        try {
            // select Look and Feel

            Properties props = new Properties();
            props.put("logoString", "610");
            GraphiteLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            new Window();
            // start application

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end main
}
