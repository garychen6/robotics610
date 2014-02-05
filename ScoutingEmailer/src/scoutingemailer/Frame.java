package scoutingemailer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Frame extends JFrame {

    int[] teams;
    static HashMap<String, Score> scores = new HashMap();
    Thread updateThread = new Thread() {
        public void run() {
            while (true) {
                teams = PhpRequest.getTeams();
                long start = System.currentTimeMillis();
                for (int team : teams) {
                    scores.put("" + team, Score.getScoreFromTable("tournament1_" + team));
                }
                try {
                    Thread.sleep(Math.max(0, 2 * 60 * 1000 - (System.currentTimeMillis() - start)));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };
    static final boolean DEBUG = false;
    static final Font FONT = new Font("Helvetica Neue", Font.BOLD, 22);
    static final Font FONT_SMALL = FONT.deriveFont(15f);
    JCheckBox[] teamBoxes;
    JTextField emailField;
    static JLabel notificationLabel;
    ImageIcon aerialAssistLogo;
    ImageIcon firstLogo;
    static final Color GREEN = new Color(0x169464);
    static final Color WHITE = Color.WHITE;
    static final Color RED = new Color(0xc80000);
    JPanel teamPanel;
    static GridLayout gLayout = new GridLayout(0, 3);
    ActionListener sendListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            final ArrayList<String> selectedTeams = new ArrayList();
            final String email = emailField.getText();
            for (JCheckBox b : teamBoxes) {
                if (b.isSelected()) {
                    selectedTeams.add(b.getText());
                }
            }
            if (!selectedTeams.isEmpty() && !email.equals("")) {
                for (JCheckBox b : teamBoxes) {
                    b.setSelected(false);
                }
                notificationLabel.setForeground(WHITE);
                notificationLabel.setText("");
                new Thread() {
                    public void run() {
                        Emailer.sendMail(email, selectedTeams);
                    }
                }.start();
                emailField.setText("");
            } else if (selectedTeams.isEmpty() && !email.equals("")) {
                notificationLabel.setForeground(RED);
                notificationLabel.setText("No Teams Selected");
            } else if (!selectedTeams.isEmpty() && email.equals("")) {
                notificationLabel.setForeground(RED);
                notificationLabel.setText("No Email Entered");
            } else {
                notificationLabel.setForeground(RED);
                notificationLabel.setText("No Email Entered & No Teams Selected");
            }
        }
    };
    ActionListener selectAllListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            for (JCheckBox b : teamBoxes) {
                b.setSelected(true);
            }
        }
    };
    ActionListener clearAllListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            for (JCheckBox b : teamBoxes) {
                b.setSelected(false);
            }
        }
    };
    ActionListener refreshTeamsListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            teamPanel.removeAll();
            teamPanel.invalidate();
            getTeams(teamPanel);
            teamPanel.revalidate();
        }
    };

    public Frame() {
        try {
            aerialAssistLogo = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("aerialAssistLogo.png")));
            firstLogo = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("firstLogo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setUndecorated(true);
        setVisible(true);
        setLayout(null);

        getContentPane().setBackground(GREEN);

        teamPanel = new JPanel(gLayout);
        teamPanel.setBackground(GREEN);
        int teamPanelWidth = 400;
        int panelX = size.width / 2 - teamPanelWidth / 2 + 25;
        teamPanel.setBounds(panelX, 0, teamPanelWidth, 600);

        getTeams(teamPanel);
        for (int team : teams) {
            scores.put("" + team, Score.getScoreFromTable("tournament1_" + team));
        }
        updateThread.start();

        int emailWidth = 350;

        JButton button = new JButton("Clear");
        button.setFont(FONT_SMALL);
        button.addActionListener(clearAllListener);
        button.setBounds(panelX, 620, 125, 30);
        add(button);

        button = new JButton("Select All");
        button.setFont(FONT_SMALL);
        button.addActionListener(selectAllListener);
        button.setBounds(panelX + 125, 620, 125, 30);
        add(button);

        button = new JButton("Refresh List");
        button.setFont(FONT_SMALL);
        button.addActionListener(refreshTeamsListener);
        button.setBounds(panelX + 250, 620, 125, 30);
        add(button);

        JLabel emailLabel = new JLabel("Enter e-mail");
        emailLabel.setForeground(WHITE);
        emailLabel.setFont(FONT_SMALL);
        int emailLabelWidth = stringWidth(emailLabel.getText(), emailLabel.getFont());
        emailLabel.setBounds(size.width / 2 - emailLabelWidth / 2 + 25, 675, emailWidth, 50);

        emailField = new JTextField();
        emailField.setToolTipText("Enter your e-mail address");
        emailField.setBounds(size.width / 2 - emailWidth / 2 + 25, 710, emailWidth, 30);

        button = new JButton("Send Email");
        button.setFont(FONT_SMALL);
        button.addActionListener(sendListener);
        button.setBounds(size.width / 2 - 75 + 25, 740, 150, 30);
        add(button);

        notificationLabel = new JLabel("", SwingConstants.CENTER);
        notificationLabel.setForeground(WHITE);
        notificationLabel.setFont(FONT_SMALL);
        notificationLabel.setBounds(size.width / 2 - 150 + 25, 760, 300, 50);
        add(notificationLabel);

        JLabel imageLabel = new JLabel(aerialAssistLogo);
        imageLabel.setBounds(panelX + teamPanelWidth + 50,
                size.height / 2 - aerialAssistLogo.getIconHeight() / 2,
                aerialAssistLogo.getIconWidth(),
                aerialAssistLogo.getIconHeight());
        add(imageLabel);

        imageLabel = new JLabel(firstLogo);
        imageLabel.setBounds(panelX - firstLogo.getIconWidth() - 130,
                size.height / 2 - firstLogo.getIconHeight() / 2,
                firstLogo.getIconWidth(),
                firstLogo.getIconHeight());
        add(imageLabel);

        add(emailLabel);
        add(emailField);

        add(teamPanel, 0);

        setSize(size);

        if (DEBUG) {
            com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this, true);
            com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
        } else {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice vc = ge.getDefaultScreenDevice();
            vc.setFullScreenWindow(this);
            setVisible(false); //this solves fullscreen bug
            setVisible(true); //which causes keyboard input to not work
        }
    }

    public void getTeams(JPanel p) {
        teams = PhpRequest.getTeams();
        teamBoxes = new JCheckBox[teams.length];
        for (int i = 0; i < teams.length; i++) {
            JCheckBox teamBox = new JCheckBox("" + teams[i]);
            teamBox.setForeground(WHITE);
            teamBox.setBackground(GREEN);
            teamBox.setFont(FONT);
            p.add(teamBox);
            teamBoxes[i] = teamBox;
        }
    }

    public int stringWidth(String s, Font f) {
        FontMetrics fm = getFontMetrics(f);
        return fm.stringWidth(s);
    }
}