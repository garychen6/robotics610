package scoutingcenter;

import java.util.List;

public class Score {

    double startGoalie, startMiddle;
    double autonLowPercent, autonHighPercent, autonHighHotPercent, autonNoShot;
    double autonFieldGoalPercent;
    double autonScore, teleScore;
    double lowPercent, highPercent;
    double fieldGoalPercent;
    double avgLowGoals, avgHighGoals;
    double autonMobility;
    int numMatches;
    double avgAssist, avgTruss, avgCatch;
    int noShows;
    int teamNum;
    List<Integer> missedMatches;
    
    public static Score getScoreFromTable(String tableName) {
        Score s = new Score();

        s.teamNum = Integer.parseInt(tableName.split("_")[1]);

        Object[][] table = PhpRequest.getTable(tableName);
        s.numMatches = table.length;

        double highMisses = getAverage(table, 8);
        double lowMisses = getAverage(table, 9);
        double highGoals = getAverage(table, 6);
        double lowGoals = getAverage(table, 7);

        s.lowPercent = lowGoals / (lowMisses + lowGoals) * 100;
        s.highPercent = highGoals / (highMisses + highGoals) * 100;
        s.avgLowGoals = lowGoals;
        s.avgHighGoals = highGoals;
        s.fieldGoalPercent = (lowGoals+highGoals)/
                (lowMisses + lowGoals+highMisses + highGoals)*100;

        Integer[] startPositions = (Integer[]) getColumn(table, 18);
        double timesGoalie = 0;
        double timesMiddle = 0;
        for (Integer i : startPositions) {
            if (i == 1) {
                timesGoalie++;
            } else {
                timesMiddle++;
            }
        }

        s.startGoalie = timesGoalie / (timesGoalie + timesMiddle) * 100;
        s.startMiddle = 100 - s.startGoalie;

        Integer[] mobility = (Integer[]) getColumn(table, 16);
        int numMobile = 0;
        for (Integer i : mobility) {
            boolean b = i == 1;
            if (b) {
                numMobile++;
                s.autonScore += 5;
            }
        }
        s.autonMobility = (double) numMobile / timesMiddle * 100;

        s.avgAssist = getAverage(table, 3);
        s.avgCatch = getAverage(table, 4);
        s.avgTruss = getAverage(table, 5);

        Integer[] noShows = (Integer[]) getColumn(table, 19);
        Integer[] matches = (Integer[]) getColumn(table, 0);
        int numNoShows = 0;

        for (int i = 0; i < noShows.length; i++) {
            if (noShows[i] == 1) {
                s.missedMatches.add(matches[i]);
                numNoShows++;
            }
        }
        s.noShows = numNoShows;

        s.teleScore = s.avgCatch * 10 + s.avgTruss * 10
                + s.avgHighGoals * 10 + s.avgLowGoals;
        
        Integer[] autonHighGoalAttempts = getColumn(table, 11);
        Integer[] autonHighGoalHot = getColumn(table, 14);
        Integer[] autonLowGoalAttempts = getColumn(table, 12);
        Integer[] autonMisses = getColumn(table, 13);

        double autonHighGoalScored = 0;
        double autonGoalHot = 0;
        double autonLowGoalScored = 0;
        double autonHighGoalTotal = 0;
        double autonLowGoalTotal = 0;
        double noShots = 0;

        for (int i = 0; i < autonMisses.length; i++) {
            if (autonHighGoalAttempts[i] == 1) {
                if (autonMisses[i] == 0) {
                    autonHighGoalScored++;
                    if (autonHighGoalHot[i] == 1) {
                        autonGoalHot++;
                    }
                }
                autonHighGoalTotal++;
            } else if (autonLowGoalAttempts[i] == 1) {
                if (autonMisses[i] == 0) {
                    autonLowGoalScored++;
                }
                autonLowGoalTotal++;
            } else {
                noShots++;
            }
        }

        s.autonNoShot = noShots;
        s.autonHighPercent = autonHighGoalScored / autonHighGoalTotal * 100;
        s.autonLowPercent = autonLowGoalScored / autonLowGoalTotal * 100;
        s.autonHighHotPercent = autonGoalHot / autonHighGoalTotal * 100;
        
        s.autonFieldGoalPercent = (autonHighGoalScored+autonLowGoalScored)/
                (autonHighGoalTotal+autonLowGoalTotal)*100;

        s.autonScore = Math.max(0, (autonHighGoalScored - autonGoalHot) * 15)
                + autonGoalHot * 20 + numMobile * 5 + autonLowGoalScored * 6;
        s.autonScore /= s.numMatches;

        s.normalize();
        
        return s;
    }

    public void normalize() {
        if (Double.isNaN(lowPercent)) {
            lowPercent = 0;
        }
        if (Double.isInfinite(lowPercent)) {
            lowPercent = 100;
        }
        if (Double.isNaN(highPercent)) {
            highPercent = 0;
        }
        if (Double.isInfinite(highPercent)) {
            highPercent = 100;
        }
        if (Double.isNaN(autonHighHotPercent)) {
            autonHighHotPercent = 0;
        }
        if (Double.isInfinite(autonHighHotPercent)) {
            autonHighHotPercent = 100;
        }
        if (Double.isNaN(autonHighPercent)) {
            autonHighPercent = 0;
        }
        if (Double.isInfinite(autonHighPercent)) {
            autonHighPercent = 100;
        }
        if (Double.isNaN(autonLowPercent)) {
            autonLowPercent = 0;
        }
        if (Double.isInfinite(autonLowPercent)) {
            autonLowPercent = 100;
        }
        if (Double.isNaN(startGoalie)) {
            startGoalie = 0;
        }
        if (Double.isInfinite(startGoalie)) {
            startGoalie = 100;
        }
        if (Double.isNaN(startMiddle)) {
            startMiddle = 0;
        }
        if (Double.isInfinite(startMiddle)) {
            startMiddle = 100;
        }
        if(Double.isNaN(autonMobility)){
            autonMobility = 0;
        }
        if(Double.isInfinite(autonMobility)){
            autonMobility = 100;
        }
        if(Double.isNaN(autonFieldGoalPercent)){
            autonFieldGoalPercent = 0;
        }
        if(Double.isInfinite(autonFieldGoalPercent)){
            autonFieldGoalPercent = 100;
        }
        if(Double.isNaN(fieldGoalPercent)){
            fieldGoalPercent = 0;
        }
        if(Double.isInfinite(fieldGoalPercent)){
            fieldGoalPercent = 100;
        }
    }

    public static Integer[] getColumn(Object[][] table, int col) {
        Integer[] colArray = new Integer[table.length];
        for (int i = 0; i < table.length; i++) {
            colArray[i] = Integer.parseInt((String) table[i][col]);
        }
        return colArray;
    }

    public static double getAverage(Object[][] table, int col) {
        double sum = 0;
        for (int i = 0; i < table.length; i++) {
            sum += Integer.parseInt((String) table[i][col]) % 1000;
        }
        return sum / table.length;
    }
}