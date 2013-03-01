/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksiserver;

/**
 *
 * @author billylo
 */
import com.itextpdf.text.DocumentException;
import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SocketServer extends JFrame {
    
    static int port = 9000,
            maxConnections = 10, // use 0 for unlimited;
            activeThreadCount = 0;
    static ArrayList<String> teams;
    static String dataBasePath;
    static javax.swing.JDialog jDialog1;
    static javax.swing.JScrollPane jScrollPane1;
    static DataBaseClient dataBase;
    static javax.swing.JTextArea log;
    
    SocketServer() {
        super("Scouting Server 1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        initMenus();
        setVisible(true);
    }
    
    private void initMenus() {
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(exit);
        menu.add(file);
        
        JMenu sheets = new JMenu("Info");
        JMenuItem team = new JMenuItem("Team Sheet");
        team.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    teamPDF();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JMenuItem match = new JMenuItem("Match Sheet");
        match.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    matchPDF();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JMenuItem rankings = new JMenuItem("Rankings");
        rankings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcRankings();
            }
        });
        sheets.add(team);
        sheets.add(match);
        sheets.add(rankings);
        menu.add(sheets);
        
        setJMenuBar(menu);
    }
    
    public static boolean isInteger(String s) {
        try {            
            Integer.parseInt(s);            
        } catch (NumberFormatException e) {            
            return false;            
        }
        return true;
    }
    
    public void calcRankings() {
        log.append("Rankings List (Highest KPR -> Lowest KPR)\n");
        dataBase.resetMaps();
        try {
            File[] files = new File(dataBasePath).listFiles();
            for (int i = 0; i < files.length; i++) {
                if (isInteger(files[i].getName())) {
                    dataBase.update(files[i].getName());
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        dataBase.updateRankings();
        while (dataBase.getRankings().peek() != null) {
            log.append(dataBase.getRankings().poll().getTeamNum() + "\n");
        }
    }
    
    public void teamPDF() throws FileNotFoundException, DocumentException {
        String teamNumber = JOptionPane.showInputDialog("Team:");
        String teamFileName = JOptionPane.showInputDialog("Sheet Name:");
        dataBase.resetMaps();
        dataBase.update(teamNumber);
        PDFCreator teamPDF = new PDFCreator(dataBasePath, teamFileName, "");
        teamPDF.createTeamPDF(dataBase.lookUpMatchSheets(teamNumber));
    }
    
    public void matchPDF() throws FileNotFoundException, DocumentException {
        String[] teams = JOptionPane.showInputDialog("Team Numbers: \n(eg.:Blue1,Blue2,Blue3,Red1,Red2,Red3)").split(",");
        String matchFileName = JOptionPane.showInputDialog("Sheet Name:");
        dataBase.resetMaps();
        for (int i = 0; i < teams.length; i++) {
            dataBase.update(teams[i]);
        }
        PDFCreator matchPDF = new PDFCreator(dataBasePath, "", matchFileName);
        matchPDF.createMatchPDF(dataBase.lookUpTeamSheet(teams[0]), dataBase.lookUpTeamSheet(teams[1]), dataBase.lookUpTeamSheet(teams[2]), dataBase.lookUpTeamSheet(teams[3]), dataBase.lookUpTeamSheet(teams[4]), dataBase.lookUpTeamSheet(teams[5]));
    }
    
    public static void main(String[] args) throws IOException {
        teams = new ArrayList();
        ServerSocket server = new ServerSocket(port, port, InetAddress.getByName("10.6.10.12"));
        dataBasePath = JOptionPane.showInputDialog("Database Path");
        dataBase = new DataBaseClient(dataBasePath);
        try {
            Properties props = new Properties();
            props.put("logoString", "610");
            GraphiteLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            SocketServer socketServer = new SocketServer();
            log.setEnabled(true);
            log.setEditable(false);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        while ((activeThreadCount++ < maxConnections) || (maxConnections == 0)) {
            Socket socket = server.accept();
            WorkerThread worker = new WorkerThread(socket);
            log.append(socket.getInetAddress().getHostAddress() + " is connected.\n");
            worker.start();
        }
        
    }
    
    private void initComponents() {
        
        jDialog1 = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        log = new javax.swing.JTextArea();
        
        
        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
                jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 400, Short.MAX_VALUE));
        jDialog1Layout.setVerticalGroup(
                jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 300, Short.MAX_VALUE));
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        log.setColumns(20);
        log.setRows(5);
        jScrollPane1.setViewportView(log);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE));
        
        pack();
    }// </editor-fold>

    synchronized static public void write(String msg) {
        log.append(msg + "\n");
    }
    
    synchronized static public String getDataBase() {
        return dataBasePath;
    }
    
    synchronized static public void addTeam(String team) {
        teams.add(team);
    }
    
    synchronized static public boolean containsTeam(String team) {
        return teams.contains(team);
    }
    
    synchronized static public void incrementThreadCount() {
        activeThreadCount++;
    }
    
    synchronized static public void decrementThreadCount() {
        activeThreadCount--;
    }
}
