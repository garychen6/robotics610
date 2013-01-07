/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Warfa
 */
public class DataBaseClient {

    String path;
    Map<String, TeamSheet> teamSheets;
    Map<String, ArrayList<MatchSheet>> matchSheets;

    DataBaseClient(String path) {
        this.path = path;
        teamSheets = new HashMap();
        matchSheets = new HashMap();
    }

    public void update(String teamName) throws FileNotFoundException {
        File dir = new File(path + "\\" + teamName);
        for (File child : dir.listFiles()) {
            if (!child.getName().equals("Comments.txt")) {
                addSheet(child);
            }
        }
    }

    public void addSheet(File child) throws FileNotFoundException {
        FileReader fl = new FileReader(child.getAbsoluteFile());
        Scanner scan = new Scanner(fl);
        String[] info = new String[15];
        for (int i = 0; i < info.length; i++) {
            info[i] = scan.nextLine();
        }
        updateTeamSheet(info);
        addMatchSheet(info);
    }

    public void updateTeamSheet(String[] info) {
        TeamSheet tm;
        if (!teamSheets.containsKey(info[0])) {
            tm = new TeamSheet(info[0]);
            teamSheets.put(info[0], tm);
        } else {
            tm = teamSheets.get(info[0]);
        }
        tm.incMatches();
        tm.setAutoStart(info[2]);
        tm.updateAvgAutoPoints(Integer.parseInt(info[3]));
        tm.updateScoringPercentage(Integer.parseInt(info[4]) + Integer.parseInt(info[5]) + Integer.parseInt(info[6]) + Integer.parseInt(info[7]),
                Integer.parseInt(info[8]) + Integer.parseInt(info[9]) + Integer.parseInt(info[10]) + Integer.parseInt(info[11]));
        tm.updateTelePoints(Integer.parseInt(info[8]), Integer.parseInt(info[9]), Integer.parseInt(info[10]), Integer.parseInt(info[11]));
        tm.updateDefenseRating(Integer.parseInt(info[12]));
        tm.updateHangTime(Integer.parseInt(info[14]));
        tm.updateHangLevel(Integer.parseInt(info[13]));
        tm.updateKPR(Integer.parseInt(info[13]));
        teamSheets.put(info[0], tm);
    }

    public void addMatchSheet(String[] info) {
        MatchSheet ms = new MatchSheet(Integer.parseInt(info[1]));
        if(!matchSheets.containsKey(info[0])){
            ArrayList<MatchSheet> sheets;
            sheets = new ArrayList<MatchSheet>();
            matchSheets.put(info[0], sheets);
        }
        ms.setTeamNum(info[0]);
        ms.setStartingPos(info[2]);
        ms.setAutonPointsScored(Double.parseDouble(info[3]));
        ms.setShotsAttemptedPyramid(Double.parseDouble(info[4]));
        ms.setShotsAttemptedHigh(Double.parseDouble(info[5]));
        ms.setShotsAttemptedMiddle(Double.parseDouble(info[6]));
        ms.setShotsAttemptedLow(Double.parseDouble(info[7]));
        ms.setShotsScoredPyramid(Double.parseDouble(info[8]));
        ms.setShotsScoredHigh(Double.parseDouble(info[9]));
        ms.setShotsScoredMiddle(Double.parseDouble(info[10]));
        ms.setShotsScoredLow(Double.parseDouble(info[11]));
        ms.setDefense(Double.parseDouble(info[12]));
        ms.setHangLevel(Double.parseDouble(info[13]));
        ms.setHangTime(Double.parseDouble(info[14]));
        matchSheets.get(info[0]).add(ms);
        
    }

    public TeamSheet lookUpTeamSheet(String teamName) {
        return teamSheets.get(teamName);
    }
    public ArrayList<MatchSheet> lookUpMatchSheets(String teamName) {
        return matchSheets.get(teamName);
    }
}
