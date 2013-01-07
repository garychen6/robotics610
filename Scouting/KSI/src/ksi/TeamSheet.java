/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 *
 * @author Warfa
 */
public class TeamSheet {

    private String teamNum;
    private double numMatches;
    private String autoStart;
    private double autoPoints;
    private double teleScoringPercentage;
    private double telePoints;
    private double defenseRating;
    private double hangTime;
    private double hangLevel;
    private double kpr;

    TeamSheet(String teamName) {
        this.teamNum = teamName;
        numMatches = 0;
        autoPoints = 0;
        teleScoringPercentage = 0;
        telePoints = 0;
        defenseRating = 0;
        hangTime = 0;
        hangLevel = 0;
        kpr = 0;
    }

    public void incMatches() {
        numMatches++;
    }

    public void setAutoStart(String pos) {
        this.autoStart = pos;
    }

    public void updateAvgAutoPoints(int points) {
        autoPoints *= getNumMatches() - 1;
        autoPoints += points;
        autoPoints /= getNumMatches();
    }

    public void updateScoringPercentage(int totAttempted, int totScored) {
        teleScoringPercentage *= getNumMatches() - 1;
        teleScoringPercentage += ((totScored * 1.0) / (totAttempted * 1.0));
        teleScoringPercentage /= getNumMatches();
    }

    public void updateTelePoints(int scoredPyramid, int scoredHigh, int scoredMiddle, int scoredLow) {
        telePoints *= numMatches - 1;
        telePoints += (scoredPyramid * 5) + (scoredHigh * 3) + (scoredMiddle * 2) + (scoredLow);
        telePoints /= numMatches;
    }

    public void updateDefenseRating(int dRating) {
        defenseRating *= numMatches - 1;
        defenseRating += dRating;
        defenseRating /= numMatches;
    }

    public void updateHangTime(int hangTime) {
        this.hangTime *= numMatches - 1;
        this.hangTime += hangTime;
        this.hangTime /= numMatches;
    }

    public void updateHangLevel(int level) {
        hangLevel = Math.max(hangLevel, level);
    }

    public void updateKPR() {
        kpr *= numMatches - 1;
        kpr += (autoPoints + telePoints) + (hangLevel * 10);
        kpr /= numMatches;
    }

    /**
     * @return the teamNum
     */
    public String getTeamNum() {
        return teamNum;
    }

    /**
     * @return the numMatches
     */
    public double getNumMatches() {
        return numMatches;
    }

    /**
     * @return the autoStart
     */
    public String getAutoStart() {
        return autoStart;
    }

    /**
     * @return the autoPoints
     */
    public double getAutoPoints() {
        return autoPoints;
    }

    /**
     * @return the teleScoringPercentage
     */
    public double getTeleScoringPercentage() {
        return teleScoringPercentage;
    }

    /**
     * @return the telePoints
     */
    public double getTelePoints() {
        return telePoints;
    }

    /**
     * @return the defenseRating
     */
    public double getDefenseRating() {
        return defenseRating;
    }

    /**
     * @return the hangTime
     */
    public double getHangTime() {
        return hangTime;
    }

    /**
     * @return the hangLevel
     */
    public double getHangLevel() {
        return hangLevel;
    }

    /**
     * @return the KPR
     */
    public double getKPR() {
        return kpr;
    }
}
