/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

/**
 *
 * @author Warfa
 */
public class MatchSheet {

   
    String teamNum;
    String startingPos;
    String matchNum;
    double autonPointsScored;
    double shotsAttemptedPyramid;
    double shotsAttemptedHigh;
    double shotsAttemptedMiddle;
    double shotsAttemptedLow;
    double shotsScoredPyramid;
    double shotsScoredHigh;
    double shotsScoredMiddle;
    double shotsScoredLow;
    double defense;
    double hangLevel;
    double hangTime;


    MatchSheet(String matchNum) {
        this.matchNum = matchNum;
        teamNum = "";
        startingPos = "";
        autonPointsScored = 0;
        shotsAttemptedPyramid = 0;
        shotsAttemptedHigh = 0;
        shotsAttemptedMiddle = 0;
        shotsAttemptedLow = 0;
        shotsScoredPyramid = 0;
        shotsScoredHigh = 0;
        shotsScoredMiddle = 0;
        shotsScoredLow = 0;
        defense = 0;
        hangLevel = 0;
        hangTime = 0;
    }
     public String getTeamNum() {
        return teamNum;
    }

    public void setTeamNum(String teamNum) {
        this.teamNum = teamNum;
    }
    
    
    public void setStartingPos(String startingPos) {
        this.startingPos = startingPos;
    }

    public void setMatchNum(String matchNum) {
        this.matchNum = matchNum;
    }

    public void setAutonPointsScored(double autonPointsScored) {
        this.autonPointsScored = autonPointsScored;
    }

    public void setShotsAttemptedPyramid(double shotsAttemptedPyramid) {
        this.shotsAttemptedPyramid = shotsAttemptedPyramid;
    }

    public void setShotsAttemptedHigh(double shotsAttemptedHigh) {
        this.shotsAttemptedHigh = shotsAttemptedHigh;
    }

    public void setShotsAttemptedMiddle(double shotsAttemptedMiddle) {
        this.shotsAttemptedMiddle = shotsAttemptedMiddle;
    }

    public void setShotsAttemptedLow(double shotsAttemptedLow) {
        this.shotsAttemptedLow = shotsAttemptedLow;
    }

    public void setShotsScoredPyramid(double shotsScoredPyramid) {
        this.shotsScoredPyramid = shotsScoredPyramid;
    }

    public void setShotsScoredHigh(double shotsScoredHigh) {
        this.shotsScoredHigh = shotsScoredHigh;
    }

    public void setShotsScoredMiddle(double shotsScoredMiddle) {
        this.shotsScoredMiddle = shotsScoredMiddle;
    }

    public void setShotsScoredLow(double shotsScoredLow) {
        this.shotsScoredLow = shotsScoredLow;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public void setHangLevel(double hangLevel) {
        this.hangLevel = hangLevel;
    }

    public void setHangTime(double hangTime) {
        this.hangTime = hangTime;
    }

    public String getStartingPos() {
        return startingPos;
    }

    public String getMatchNum() {
        return matchNum;
    }

    public double getAutonPointsScored() {
        return autonPointsScored;
    }

    public double getShotsAttemptedPyramid() {
        return shotsAttemptedPyramid;
    }

    public double getShotsAttemptedHigh() {
        return shotsAttemptedHigh;
    }

    public double getShotsAttemptedMiddle() {
        return shotsAttemptedMiddle;
    }

    public double getShotsAttemptedLow() {
        return shotsAttemptedLow;
    }

    public double getShotsScoredPyramid() {
        return shotsScoredPyramid;
    }

    public double getShotsScoredHigh() {
        return shotsScoredHigh;
    }

    public double getShotsScoredMiddle() {
        return shotsScoredMiddle;
    }

    public double getShotsScoredLow() {
        return shotsScoredLow;
    }

    public double getDefense() {
        return defense;
    }

    public double getHangLevel() {
        return hangLevel;
    }

    public double getHangTime() {
        return hangTime;
    }
}
