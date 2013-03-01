
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksiserver;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PDFCreator {

    private static String matchFile = "C:/temp/Match.pdf";
    private static String teamFile = "C:/temp/Team.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.NORMAL, BaseColor.RED);
    private static Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.NORMAL, BaseColor.BLUE);
    private static Font greenFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.NORMAL, new BaseColor(1, 129, 86));

    PDFCreator(String directory, String teamFileName, String matchFileName) {
        this.matchFile = directory + "/" + matchFileName + ".pdf";
        this.teamFile = directory + "/" + teamFileName + ".pdf";
    }

    public static void createMatchPDF(TeamSheet blue1, TeamSheet blue2, TeamSheet blue3, TeamSheet red1, TeamSheet red2, TeamSheet red3)
            throws DocumentException {
        //Create a document
        Document document = new Document();
        try {
            //Make the PDF writer
            PdfWriter.getInstance(document, new FileOutputStream(matchFile));
        } catch (FileNotFoundException ex) {
        }

        document.open();
        //Create a paragraph
        Paragraph content = new Paragraph();
        //Create the table
        PdfPTable table = new PdfPTable(4);

        //Add the headers
        PdfPCell c1 = new PdfPCell(new Phrase("Field"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(blue1.getTeamNum()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(blue2.getTeamNum()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(blue3.getTeamNum()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        //Add the cells for number of matches
        table.addCell("# of Matches");
        table.addCell(blue1.getNumMatches() + "");
        table.addCell(blue2.getNumMatches() + "");
        table.addCell(blue3.getNumMatches() + "");

        //Add the cells for the starting positions
        table.addCell("Starts Auto");
        table.addCell(blue1.getAutoStart() + "");
        table.addCell(blue2.getAutoStart() + "");
        table.addCell(blue3.getAutoStart() + "");

        //Add the cells for auto points
        table.addCell("Auto Pts");
        table.addCell(Math.round((blue1.getAutoPoints() ) * 10) / 10.0  + "");
        table.addCell(Math.round((blue2.getAutoPoints() ) * 10) / 10.0 + "");
        table.addCell(Math.round((blue3.getAutoPoints() ) * 10) / 10.0 + "");

        //Add the cells for teleop scoring percentage
        table.addCell("Teleop Scoring %");
        table.addCell(Math.round((blue1.getTeleScoringPercentage() * 100) * 10) / 10.0 + "");
        table.addCell(Math.round((blue2.getTeleScoringPercentage() * 100) * 10) / 10.0 + "");
        table.addCell(Math.round((blue3.getTeleScoringPercentage() * 100) * 10) / 10.0 + "");

        //Add the cells for the average points in teleop
        table.addCell("Teleop Avg Pts");
        table.addCell(Math.round((blue1.getTelePoints() ) * 10) / 10.0 + "");
        table.addCell(Math.round((blue2.getTelePoints() ) * 10) / 10.0 + "");
        table.addCell(Math.round((blue3.getTelePoints() ) * 10) / 10.0 + "");

        //Add the cells for defensive rating
        table.addCell("Defensive Rating");
        table.addCell(Math.round((blue1.getDefenseRating() ) * 10) / 10.0 + "");
        table.addCell(Math.round((blue2.getDefenseRating() ) * 10) / 10.0 + "");
        table.addCell(Math.round((blue3.getDefenseRating() ) * 10) / 10.0 + "");

        //Add the cells for the time required to hang
        table.addCell("Time Hang");
        table.addCell(Math.round((blue1.getHangTime() ) * 10) / 10.0 + "");
        table.addCell(Math.round((blue2.getHangTime() ) * 10) / 10.0 + "");
        table.addCell(Math.round((blue3.getHangTime() ) * 10) / 10.0 + "");

        //Add the cells for the level the robot can hang at
        table.addCell("Hang Level");
        table.addCell(Math.round((blue1.getHangLevel() ) * 10) / 10.0 + "");
        table.addCell(Math.round((blue2.getHangLevel() ) * 10) / 10.0 + "");
        table.addCell(Math.round((blue3.getHangLevel() ) * 10) / 10.0 + "");

        //Add the cells for the average KPR
        table.addCell("Avg KPR");
        table.addCell(Math.round((blue1.getKPR() ) * 10) / 10.0 + "");
        table.addCell(Math.round((blue2.getKPR() ) * 10) / 10.0 + "");
        table.addCell(Math.round((blue3.getKPR() ) * 10) / 10.0 + "");



        PdfPTable table1 = new PdfPTable(4);

        c1 = new PdfPCell(new Phrase("Field"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.addCell(c1);

        c1 = new PdfPCell(new Phrase(red1.getTeamNum()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.addCell(c1);

        c1 = new PdfPCell(new Phrase(red2.getTeamNum()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.addCell(c1);

        c1 = new PdfPCell(new Phrase(red3.getTeamNum()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.addCell(c1);


        //Add the cells for number of matches
        table1.addCell("# of Matches");
        table1.addCell(red1.getNumMatches() + "");
        table1.addCell(red2.getNumMatches() + "");
        table1.addCell(red3.getNumMatches() + "");

        //Add the cells for the starting positions
        table1.addCell("Starts Auto");
        table1.addCell(red1.getAutoStart() + "");
        table1.addCell(red2.getAutoStart() + "");
        table1.addCell(red3.getAutoStart() + "");

        //Add the cells for auto points
        table1.addCell("Auto Pts");
        table1.addCell(Math.round((red1.getAutoPoints() ) * 10) / 10.0  + "");
        table1.addCell(Math.round((red2.getAutoPoints() ) * 10) / 10.0 + "");
        table1.addCell(Math.round((red3.getAutoPoints() ) * 10) / 10.0 + "");

        //Add the cells for teleop scoring percentage
        table1.addCell("Teleop Scoring %");
        table1.addCell(Math.round((red1.getTeleScoringPercentage() * 100) * 10) / 10.0 + "");
        table1.addCell(Math.round((red2.getTeleScoringPercentage() * 100) * 10) / 10.0 + "");
        table1.addCell(Math.round((red3.getTeleScoringPercentage() * 100) * 10) / 10.0 + "");

        //Add the cells for the average points in teleop
        table1.addCell("Teleop Avg Pts");
        table1.addCell(Math.round((red1.getTelePoints() ) * 10) / 10.0 + "");
        table1.addCell(Math.round((red2.getTelePoints() ) * 10) / 10.0 + "");
        table1.addCell(Math.round((red3.getTelePoints() ) * 10) / 10.0 + "");

        //Add the cells for defensive rating
        table1.addCell("Defensive Rating");
        table1.addCell(Math.round((red1.getDefenseRating() ) * 10) / 10.0 + "");
        table1.addCell(Math.round((red2.getDefenseRating() ) * 10) / 10.0 + "");
        table1.addCell(Math.round((red3.getDefenseRating() ) * 10) / 10.0 + "");

        //Add the cells for the time required to hang
        table1.addCell("Time Hang");
        table1.addCell(Math.round((red1.getHangTime() ) * 10) / 10.0 + "");
        table1.addCell(Math.round((red2.getHangTime() ) * 10) / 10.0 + "");
        table1.addCell(Math.round((red3.getHangTime() ) * 10) / 10.0 + "");

        //Add the cells for the level the robot can hang at
        table1.addCell("Hang Level");
        table1.addCell(Math.round((red1.getHangLevel() ) * 10) / 10.0 + "");
        table1.addCell(Math.round((red2.getHangLevel() ) * 10) / 10.0 + "");
        table1.addCell(Math.round((red3.getHangLevel() ) * 10) / 10.0 + "");

        //Add the cells for the average KPR
        table1.addCell("Avg KPR");
        table1.addCell(Math.round((red1.getKPR() ) * 10) / 10.0 + "");
        table1.addCell(Math.round((red2.getKPR() ) * 10) / 10.0 + "");
        table1.addCell(Math.round((red3.getKPR() ) * 10) / 10.0 + "");

        Paragraph paragraph = new Paragraph("Blue Alliance", blueFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        content.add(paragraph);
        addEmptyLine(content, 1);
        content.add(table);
        paragraph = new Paragraph("Red Alliance", redFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        content.add(paragraph);
        addEmptyLine(content, 1);
        content.add(table1);
        document.add(content);

        document.close();
    }

    public static void createTeamPDF(ArrayList<MatchSheet> matches) throws DocumentException {
        Document document = new Document();
        try {
            //Make the PDF writer
            PdfWriter.getInstance(document, new FileOutputStream(teamFile));
        } catch (FileNotFoundException ex) {
        }
        matches = sortMatches(matches);
        document.open();
        Paragraph content = new Paragraph();
        MatchSheet matchsheet = new MatchSheet("Average");
        double autonPointsScored = 0;
        double shotsAttemptedPyramid = 0;
        double shotsAttemptedHigh = 0;
        double shotsAttemptedMiddle = 0;
        double shotsAttemptedLow = 0;
        double shotsScoredPyramid = 0;
        double shotsScoredHigh = 0;
        double shotsScoredMiddle = 0;
        double shotsScoredLow = 0;
        double defense = 0;
        double hangLevel = 0;
        double hangTime = 0;
        int numMatches = matches.size();

        for (MatchSheet match : matches) {
            autonPointsScored += match.getAutonPointsScored();
            shotsAttemptedPyramid += match.getShotsAttemptedPyramid();
            shotsAttemptedHigh += match.getShotsAttemptedHigh();
            shotsAttemptedMiddle += match.getShotsAttemptedMiddle();
            shotsAttemptedLow += match.getShotsAttemptedLow();
            shotsScoredPyramid += match.getShotsScoredPyramid();
            shotsScoredHigh += match.getShotsScoredHigh();
            shotsScoredMiddle += match.getShotsScoredMiddle();
            shotsScoredLow += match.getShotsScoredLow();
            defense += match.getDefense();
            hangLevel += match.getHangLevel();
            hangTime += match.getHangTime();
        }

        matchsheet.setTeamNum(matches.get(0).getTeamNum());
        matchsheet.setAutonPointsScored(autonPointsScored);
        matchsheet.setShotsAttemptedPyramid(shotsAttemptedPyramid);
        matchsheet.setShotsAttemptedHigh(shotsAttemptedHigh);
        matchsheet.setShotsAttemptedMiddle(shotsAttemptedMiddle);
        matchsheet.setShotsAttemptedLow(shotsAttemptedLow);
        matchsheet.setShotsScoredPyramid(shotsScoredPyramid);
        matchsheet.setShotsScoredHigh(shotsScoredHigh);
        matchsheet.setShotsScoredMiddle(shotsScoredMiddle);
        matchsheet.setShotsScoredLow(shotsScoredLow);
        matchsheet.setDefense(defense);
        matchsheet.setHangLevel(hangLevel);
        matchsheet.setHangTime(hangTime);

        matches.add(matchsheet);
        while (matches.size() > 5) {
            ArrayList<MatchSheet> selection = new ArrayList();
            for (int i = 0; i < 5; i++) {
                selection.add(matches.get(0));
                matches.remove(0);
            }

            createNewPage(selection, document, numMatches);
            document.newPage();
        }
        createNewPage(matches, document, numMatches);

        document.close();
    }

    public static void createNewPage(ArrayList<MatchSheet> matches, Document document, int numMatches)
            throws DocumentException {

        Paragraph content = new Paragraph();

        
        PdfPTable table = new PdfPTable(matches.size() + 1);
        
        

        
        PdfPCell c1 = new PdfPCell(new Phrase("Match #"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);

        for (int i = 0; i < matches.size(); i++) {
            c1 = new PdfPCell(new Phrase(matches.get(i).getMatchNum() + ""));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }



        table.addCell("Starting Position");
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getMatchNum().equals("Average")) {
                table.addCell("N/A");
            } else {
                table.addCell(matches.get(i).getStartingPos());
            }

        }


        table.addCell("Auto Points");

        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getMatchNum().equals("Average")) {

                table.addCell(Math.round(matches.get(i).getAutonPointsScored() / numMatches * 10) / 10.0 + "");
            } else {

                table.addCell(matches.get(i).getAutonPointsScored() + "");
            }


        }


        double[] percentage = new double[matches.size()];

        table.addCell("Teleop Low Attempted");
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getMatchNum().equals("Average")) {
                percentage[i] = matches.get(i).getShotsAttemptedLow();

                table.addCell(Math.round(matches.get(i).getShotsAttemptedLow() / numMatches * 10) / 10.0 + "");
            } else {
                percentage[i] = matches.get(i).getShotsAttemptedLow();
                table.addCell(matches.get(i).getShotsAttemptedLow() + "");
            }

        }



        table.addCell("Teleop Low Scored");
        for (int i = 0; i < matches.size(); i++) {
            if (percentage[i] != 0) {
                percentage[i] = matches.get(i).getShotsScoredLow() / percentage[i];
            } else {
                percentage[i] = 0;
            }
            if (matches.get(i).getMatchNum().equals("Average")) {


                table.addCell(Math.round(matches.get(i).getShotsScoredLow() / numMatches * 10) / 10.0 + "");
            } else {

                table.addCell(matches.get(i).getShotsScoredLow() + "");
            }
        }



        table.addCell("Teleop Low Percentage");
        for (int i = 0; i < matches.size(); i++) {


            table.addCell(Math.round((percentage[i] * 100) * 10) / 10.0 + "");
        }


        percentage = new double[matches.size()];
        table.addCell("Teleop Middle Attempted");
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getMatchNum().equals("Average")) {
                percentage[i] = matches.get(i).getShotsAttemptedMiddle();
                table.addCell(Math.round(matches.get(i).getShotsAttemptedMiddle() / numMatches * 10) / 10.0 + "");
            } else {
                percentage[i] = matches.get(i).getShotsAttemptedMiddle();
                table.addCell(matches.get(i).getShotsAttemptedMiddle() + "");
            }

        }



        table.addCell("Teleop Middle Scored");
        for (int i = 0; i < matches.size(); i++) {
            if (percentage[i] != 0) {
                percentage[i] = matches.get(i).getShotsScoredMiddle() / percentage[i];
            } else {
                percentage[i] = 0;
            }
            if (matches.get(i).getMatchNum().equals("Average")) {


                table.addCell(Math.round(matches.get(i).getShotsScoredMiddle() / numMatches * 10) / 10.0 + "");
            } else {

                table.addCell(matches.get(i).getShotsScoredMiddle() + "");
            }

        }


        table.addCell("Teleop Middle Percentage");
        for (int i = 0; i < matches.size(); i++) {

            table.addCell(Math.round((percentage[i] * 100) * 10) / 10.0 + "");
        }


        percentage = new double[matches.size()];
        table.addCell("Teleop High Attempted");
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getMatchNum().equals("Average")) {
                percentage[i] = matches.get(i).getShotsAttemptedHigh();
                table.addCell(Math.round(matches.get(i).getShotsAttemptedHigh() / numMatches * 10) / 10.0 + "");
            } else {
                percentage[i] = matches.get(i).getShotsAttemptedHigh();
                table.addCell(matches.get(i).getShotsAttemptedHigh() + "");
            }
        }


        table.addCell("Teleop High Scored");
        for (int i = 0; i < matches.size(); i++) {
            if (percentage[i] != 0) {
                percentage[i] = matches.get(i).getShotsScoredHigh() / percentage[i];
            } else {
                percentage[i] = 0;
            }
            if (matches.get(i).getMatchNum().equals("Average")) {


                table.addCell(Math.round(matches.get(i).getShotsScoredHigh() / numMatches * 10) / 10.0 + "");
            } else {

                table.addCell(matches.get(i).getShotsScoredHigh() + "");
            }
        }


        table.addCell("Teleop High Percentage");
        for (int i = 0; i < matches.size(); i++) {

            table.addCell(Math.round((percentage[i] * 100) * 10) / 10.0 + "");
        }


        percentage = new double[matches.size()];
        table.addCell("Teleop Pyramid Attempted");
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getMatchNum().equals("Average")) {
                percentage[i] = matches.get(i).getShotsAttemptedPyramid();
                table.addCell(Math.round(matches.get(i).getShotsAttemptedHigh() / numMatches * 10) / 10.0 + "");
            } else {
                percentage[i] = matches.get(i).getShotsAttemptedPyramid();
                table.addCell(matches.get(i).getShotsAttemptedPyramid() + "");
            }
        }


        table.addCell("Teleop Pyramid Scored");
        for (int i = 0; i < matches.size(); i++) {

            if (percentage[i] != 0) {
                percentage[i] = matches.get(i).getShotsScoredPyramid() / percentage[i];
            } else {
                percentage[i] = 0;
            }


            if (matches.get(i).getMatchNum().equals("Average")) {


                table.addCell(Math.round(matches.get(i).getShotsScoredPyramid() / numMatches * 10) / 10.0 + "");
            } else {

                table.addCell(matches.get(i).getShotsScoredPyramid() + "");
            }
        }


        table.addCell("Teleop Pyramid Percentage");
        for (int i = 0; i < matches.size(); i++) {

            table.addCell(Math.round((percentage[i] * 100) * 10) / 10.0 + "");

            //table.addCell(Math.round((percentage[i] * 100) * 10) / 10.0 + "");

        }


        table.addCell("Defense Rating");
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getMatchNum().equals("Average")) {


                table.addCell(Math.round(matches.get(i).getDefense() / numMatches * 10) / 10.0 + "");
            } else {

                table.addCell(matches.get(i).getDefense() + "");
            }
            
        }


        table.addCell("Hang Level");
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getMatchNum().equals("Average")) {


                table.addCell(Math.round(matches.get(i).getHangLevel() / numMatches * 10) / 10.0 + "");
            } else {

                table.addCell(matches.get(i).getHangLevel() + "");
            }
        }


        table.addCell("Hang Time");
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getMatchNum().equals("Average")) {


                table.addCell(Math.round(matches.get(i).getHangTime() / numMatches * 10) / 10.0 + "");
            } else {

                table.addCell(matches.get(i).getHangTime() + "");
            }
        }

        Paragraph paragraph;
        if (matches.get(0).getTeamNum().equals("610")) {
            paragraph = new Paragraph(matches.get(0).getTeamNum(), greenFont);
        } else {
            paragraph = new Paragraph(matches.get(0).getTeamNum(), catFont);
        }
        paragraph.setAlignment(Element.ALIGN_CENTER);
        content.add(paragraph);
        
        addEmptyLine(content, 1);
        
        content.add(table);

        document.add(content);
        

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    private static ArrayList<MatchSheet> sortMatches (ArrayList<MatchSheet> matches) {
        ArrayList<MatchSheet> orderedMatches = new ArrayList();
        orderedMatches.add(matches.get(0));
        for(int i = 1; i<matches.size(); i++){
            for(int j = 0; j<orderedMatches.size(); j++){
                if(Integer.parseInt(matches.get(i).getMatchNum())<Integer.parseInt(orderedMatches.get(j).getMatchNum())){
                    orderedMatches.add(j,matches.get(i));
                    j=orderedMatches.size();
                }
                else if(j==orderedMatches.size()-1){
                    orderedMatches.add(matches.get(i));
                    j = orderedMatches.size();
                }
            }
        }
        for(int i = 0; i<orderedMatches.size();i++){
            System.out.println(orderedMatches.get(i).getMatchNum());
        }
        return orderedMatches;
    }
}
