/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 *
 * @author Warfa
 */
public class WindowScreen extends JFrame {

    JFormattedTextField textField[] = new JFormattedTextField[15];
    String[] info = new String[15];
    String comment;
    JTextArea comments;
    JLabel jLabel1;
    JLabel jLabel10;
    JLabel jLabel11;
    JLabel jLabel12;
    JLabel jLabel13;
    JLabel jLabel14;
    JLabel jLabel15;
    JLabel jLabel16;
    JLabel jLabel2;
    JLabel jLabel3;
    JLabel jLabel4;
    JLabel jLabel5;
    JLabel jLabel6;
    JLabel jLabel7;
    JLabel jLabel8;
    JLabel jLabel9;
    JMenu jMenu1;
    JMenuItem jMenuItem1;
    JMenuItem jMenuItem2;
    JScrollPane jScrollPane1;
    String dataBasePath;
    DataBaseClient dataBase;
    ArrayList teams = new ArrayList();
    public int i = 0;

    WindowScreen() {
        super("Scouting Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F1);
        JMenuItem newItem = new JMenuItem("New");
        dataBasePath = JOptionPane.showInputDialog("DataBase FilePath?\nThis is where team folders will be created.\n*NOTE:This must be a real path.* ");
        dataBase = new DataBaseClient(dataBasePath);
        initComponents();
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
                        Logger.getLogger(WindowScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(WindowScreen.class.getName()).log(Level.SEVERE, null, ex);
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

    private void initComponents() {

        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        jLabel10 = new JLabel();
        jLabel11 = new JLabel();
        jLabel12 = new JLabel();
        jLabel13 = new JLabel();
        jLabel14 = new JLabel();
        jLabel15 = new JLabel();
        jLabel16 = new JLabel();

        for (i = 0; i < 15; i++) {
            textField[i] = new JFormattedTextField();
        }
        textField[0].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[0] = textField[0].getText();
            }
        });
        textField[1].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[1] = textField[1].getText();
            }
        });
        textField[2].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[2] = textField[2].getText();
            }
        });
        textField[3].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[3] = textField[3].getText();
            }
        });
        textField[4].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[4] = textField[4].getText();
            }
        });
        textField[5].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[5] = textField[5].getText();
            }
        });
        textField[6].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[6] = textField[6].getText();
            }
        });
        textField[7].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[7] = textField[7].getText();
            }
        });
        textField[8].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[8] = textField[8].getText();
            }
        });
        textField[9].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[9] = textField[9].getText();
            }
        });
        textField[10].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[10] = textField[10].getText();
            }
        });
        textField[11].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[11] = textField[11].getText();
            }
        });
        textField[12].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[12] = textField[12].getText();
            }
        });
        textField[13].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[13] = textField[13].getText();
            }
        });
        textField[14].addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[14] = textField[14].getText();
            }
        });
        comments = new JTextArea();
        comments.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                info[info.length - 1] = comments.getText();
            }
        });

        jScrollPane1 = new JScrollPane();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jLabel1.setText("Team Number");
        jLabel2.setText("Match Number");
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Autonomous");
        jLabel4.setText("Starting Position(L:C:R)");
        jLabel5.setText("Points Scored");
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Tele-Operated");
        jLabel7.setText("Pyramid");
        jLabel8.setText("High");
        jLabel9.setText("Discs Attempted");
        jLabel10.setText("Discs Scored");
        jLabel11.setText("Middle");
        jLabel12.setText("Low");
        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel13.setText("End Game");
        jLabel14.setText("Hang Level (0:1:2:3)");
        jLabel15.setText("Time For Hang (0 for N/A)");
        jLabel16.setText("Defense Rating (1-10)");
        comments.setColumns(20);
        comments.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        comments.setRows(5);
        comments.setText("Comments");
        jScrollPane1.setViewportView(comments);


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 394, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jLabel3)
                .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                .addComponent(textField[0], GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addComponent(textField[1])))
                .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jLabel4)
                .addComponent(jLabel5))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                .addComponent(textField[2])
                .addComponent(textField[3], GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)))
                .addComponent(jLabel6)
                .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jLabel9)
                .addComponent(jLabel10)
                .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                .addComponent(textField[12], GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addComponent(textField[8], GroupLayout.Alignment.LEADING)
                .addComponent(textField[4], GroupLayout.Alignment.LEADING)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                .addComponent(textField[5], GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
                .addComponent(textField[9]))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addGap(33, 33, 33)
                .addComponent(jLabel12))
                .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                .addComponent(textField[10], GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addComponent(textField[6], GroupLayout.Alignment.LEADING))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                .addComponent(textField[7], GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addComponent(textField[11])))))))
                .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jLabel13)
                .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jLabel15, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel14))
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(textField[13], GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                .addComponent(textField[14], GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                .addComponent(textField[0], GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addComponent(textField[1], GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(textField[2], GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(textField[3], GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel7)
                .addComponent(jLabel8)
                .addComponent(jLabel11)
                .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel9)
                .addComponent(textField[4], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(textField[5], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(textField[6], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(textField[7], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel10)
                .addComponent(textField[8], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(textField[9], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(textField[10], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(textField[11], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(textField[12], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel16))
                .addGap(26, 26, 26)
                .addComponent(jLabel13)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel14)
                .addComponent(textField[13], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel15, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addComponent(textField[14], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));

        pack();
    }// </editor-fold>

    public void clearForm() {
        for (i = 0; i < textField.length; i++) {
            textField[i].setText("");
        }
        comments.setText("Comments");
    }

    void commitEdit() throws ParseException {
        for (i = 0; i < textField.length; i++) {
            textField[i].commitEdit();
        }
        comment = comments.getText();
    }

    public void saveFile() throws IOException, ParseException {
        commitEdit();
        makeEntry();
        clearForm();
    }

    public void updateTeam(String team) throws FileNotFoundException {
    }

    public void makeEntry() throws IOException {
           if (!teams.contains(info[0])) {
            teams.add(info[0]);
            File dir = new File(dataBasePath + "\\" + info[0]);
            dir.mkdir();
            File commentFile = new File(dataBasePath + "\\" + info[0]+ "\\" +"Comments.txt");
            commentFile.createNewFile();
            commentFile.setReadable(true);
            commentFile.setWritable(true);
        }
            FileWriter fl = new FileWriter(dataBasePath + "\\" + info[0] + "\\" + info[0] + "-" + info[1] + ".txt");
            for(int j = 0; j < info.length; j++){
                fl.write(info[j]+"\r\n");
            }
            fl.close();
            fl = new FileWriter(dataBasePath + "\\" + info[0]+ "\\" +"Comments.txt");
            fl.write(comment+"\r\n");
            fl.close();
    }
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            props.put("logoString", "610");
            GraphiteLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            new WindowScreen();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
