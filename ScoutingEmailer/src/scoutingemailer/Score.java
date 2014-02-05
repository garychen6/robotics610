package scoutingemailer;

import java.util.List;

public class Score {
    private double startGoalie;
    private double startMiddle;
    private double autonLowPercent;
    private double autonHighPercent;
    private double autonHighHotPercent;
    private double autonNoShot;
    private double autonScore;
    private double teleScore;
    private double lowPercent;
    private double highPercent;
    private double avgLowGoals;
    private double avgHighGoals;
    private double autonMobility;
    private int numMatches;
    private double avgAssist;
    private double avgTruss;
    private double avgCatch;
    private int noShows;
    private int teamNum;
    private List<Integer> missedMatches;
    
    int dqCount = 0;
    
    public int lowShots, lowScores;
    public int highShots, highScores;
    public int autonLowShots, autonLowScores;
    public int autonHighShots, autonHighScores;

    public static Score getScoreFromTable(String tableName) {
        Score s = new Score();

        s.teamNum = Integer.parseInt(tableName.split("_")[1]);

        Object[][] table = PhpRequest.getTable(tableName);
        s.numMatches = table.length;

        double highMisses = getAverage(table, 8);
        double lowMisses = getAverage(table, 9);
        double highGoals = getAverage(table, 6);
        double lowGoals = getAverage(table, 7);
        
        s.lowShots = (int)Math.round((lowGoals+lowMisses)*s.numMatches);
        s.lowScores = (int)Math.round((lowGoals)*s.numMatches);
        s.highShots = (int)Math.round((highGoals+highMisses)*s.numMatches);
        s.highScores = (int)Math.round((highGoals)*s.numMatches);

        s.lowPercent = lowGoals / (lowMisses + lowGoals) * 100;
        s.highPercent = highGoals / (highMisses + highGoals) * 100;
        s.avgLowGoals = lowGoals;
        s.avgHighGoals = highGoals;

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
        s.startMiddle = 100 - s.getStartGoalie();

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
        
        Integer[] trussGoals = (Integer[]) getColumn(table, 5);
        
        for(int i : trussGoals){
            if(i >= 1000){
                s.dqCount++;
            }
        }

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

        s.teleScore = s.getAvgCatch() * 10 + s.getAvgTruss() * 10
                + s.getAvgHighGoals() * 10 + s.getAvgLowGoals();

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
        
        s.autonHighShots = (int)Math.round(autonHighGoalTotal);
        s.autonHighScores = (int)Math.round(autonHighGoalScored);
        s.autonLowShots = (int)Math.round(autonLowGoalTotal);
        s.autonLowScores = (int)Math.round(autonLowGoalScored);

        s.autonNoShot = noShots;
        s.autonHighPercent = autonHighGoalScored / autonHighGoalTotal * 100;
        s.autonLowPercent = autonLowGoalScored / autonLowGoalTotal * 100;
        s.autonHighHotPercent = autonGoalHot / autonHighGoalTotal * 100;

        s.autonScore = Math.max(0, (autonHighGoalScored - autonGoalHot) * 15)
                + autonGoalHot * 20 + numMobile * 5 + autonLowGoalScored * 6;
        s.autonScore /= s.getNumMatches();

        s.normalize();

        return s;
    }

    public void normalize() {
        if (Double.isNaN(getLowPercent())) {
            lowPercent = 0;
        }
        if (Double.isInfinite(getLowPercent())) {
            lowPercent = 100;
        }
        if (Double.isNaN(getHighPercent())) {
            highPercent = 0;
        }
        if (Double.isInfinite(getHighPercent())) {
            highPercent = 100;
        }
        if (Double.isNaN(getAutonHighHotPercent())) {
            autonHighHotPercent = 0;
        }
        if (Double.isInfinite(getAutonHighHotPercent())) {
            autonHighHotPercent = 100;
        }
        if (Double.isNaN(getAutonHighPercent())) {
            autonHighPercent = 0;
        }
        if (Double.isInfinite(getAutonHighPercent())) {
            autonHighPercent = 100;
        }
        if (Double.isNaN(getAutonLowPercent())) {
            autonLowPercent = 0;
        }
        if (Double.isInfinite(getAutonLowPercent())) {
            autonLowPercent = 100;
        }
        if (Double.isNaN(getStartGoalie())) {
            startGoalie = 0;
        }
        if (Double.isInfinite(getStartGoalie())) {
            startGoalie = 100;
        }
        if (Double.isNaN(getStartMiddle())) {
            startMiddle = 0;
        }
        if (Double.isInfinite(getStartMiddle())) {
            startMiddle = 100;
        }
        if(Double.isNaN(getAutonMobility())){
            autonMobility = 0;
        }
        if(Double.isInfinite(getAutonMobility())){
            autonMobility = 100;
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

    /**
     * @return the startGoalie
     */
    public double getStartGoalie() {
        return startGoalie;
    }

    /**
     * @return the startMiddle
     */
    public double getStartMiddle() {
        return startMiddle;
    }

    /**
     * @return the autonLowPercent
     */
    public double getAutonLowPercent() {
        return autonLowPercent;
    }

    /**
     * @return the autonHighPercent
     */
    public double getAutonHighPercent() {
        return autonHighPercent;
    }

    /**
     * @return the autonHighHotPercent
     */
    public double getAutonHighHotPercent() {
        return autonHighHotPercent;
    }

    public double getAutonNoShot() {
        return autonNoShot;
    }
    
    public double getAutonScore() {
        return autonScore;
    }

    /**
     * @return the teleScore
     */
    public double getTeleScore() {
        return teleScore;
    }

    /**
     * @return the lowPercent
     */
    public double getLowPercent() {
        return lowPercent;
    }

    /**
     * @return the highPercent
     */
    public double getHighPercent() {
        return highPercent;
    }

    /**
     * @return the avgLowGoals
     */
    public double getAvgLowGoals() {
        return avgLowGoals;
    }

    /**
     * @return the avgHighGoals
     */
    public double getAvgHighGoals() {
        return avgHighGoals;
    }

    /**
     * @return the autonMobility
     */
    public double getAutonMobility() {
        return autonMobility;
    }

    /**
     * @return the numMatches
     */
    public int getNumMatches() {
        return numMatches;
    }

    /**
     * @return the avgAssist
     */
    public double getAvgAssist() {
        return avgAssist;
    }

    /**
     * @return the avgTruss
     */
    public double getAvgTruss() {
        return avgTruss;
    }

    /**
     * @return the avgCatch
     */
    public double getAvgCatch() {
        return avgCatch;
    }

    /**
     * @return the noShows
     */
    public int getNoShows() {
        return noShows;
    }

    /**
     * @return the teamNum
     */
    public int getTeamNum() {
        return teamNum;
    }
}