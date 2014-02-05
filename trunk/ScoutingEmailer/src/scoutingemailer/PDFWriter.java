package scoutingemailer;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDFWriter {

    static final Font TITLE = new Font(Font.FontFamily.HELVETICA, 30);
    static final Font SUB_TITLE = new Font(Font.FontFamily.HELVETICA, 16);

    public static void createPDF(String teamNumber, Score team) throws DocumentException {
        File folder = new File("PDF");
        if(!folder.exists()){
            folder.mkdirs();
        }
        String fileDirectory = "PDF/"+teamNumber + ".pdf";
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(fileDirectory));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDFWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc.open();

        Paragraph mainContent = new Paragraph();
        PdfPTable autonTable = new PdfPTable(2);
        PdfPTable teleTable = new PdfPTable(2);
        PdfPTable generalTable = new PdfPTable(2);

        Paragraph title = new Paragraph(teamNumber + "\n", TITLE);
        title.setAlignment(Element.ALIGN_CENTER);

        PdfPCell cell = new PdfPCell(new Phrase("Goalie Zone Start Percentage"));
        cell.setPadding(6);

        autonTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", team.getStartGoalie()) + "%"));
        autonTable.addCell(cell);

        cell.setPhrase(new Phrase("Middle Zone Start Percentage"));
        autonTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", team.getStartMiddle()) + "%"));
        autonTable.addCell(cell);

        cell.setPhrase(new Phrase("Auton Low Score Percentage"));
        autonTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", team.getAutonLowPercent()) + "% (" + team.autonLowScores + "/" + team.autonLowShots + ")"));
        autonTable.addCell(cell);

        cell.setPhrase(new Phrase("Auton High Score Percentage"));
        autonTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", team.getAutonHighPercent()) + "% (" + team.autonHighScores + "/" + team.autonHighShots + ")"));
        autonTable.addCell(cell);

        cell.setPhrase(new Phrase("Auton High Hot Score Percentage"));
        autonTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", team.getAutonHighHotPercent()) + "%"));
        autonTable.addCell(cell);

        cell.setPhrase(new Phrase("Auton No-Shots"));
        autonTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", team.getAutonNoShot())));
        autonTable.addCell(cell);

        cell.setPhrase(new Phrase("Auton Mobility"));
        autonTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", team.getAutonMobility()) + "%"));
        autonTable.addCell(cell);

        cell.setPhrase(new Phrase("Average Auton Score"));
        autonTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.1f", team.getAutonScore())));
        autonTable.addCell(cell);

        cell.setPhrase(new Phrase("Low Goal Score Percentage"));
        teleTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", team.getLowPercent()) + "% (" + team.lowScores + "/" + team.lowShots + ")"));
        teleTable.addCell(cell);

        cell.setPhrase(new Phrase("High Goal Score Percentage"));
        teleTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", team.getHighPercent()) + "% (" + team.highScores + "/" + team.highShots + ")"));
        teleTable.addCell(cell);

        cell.setPhrase(new Phrase("Average Low Goals Scored"));
        teleTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.1f", team.getAvgLowGoals())));
        teleTable.addCell(cell);

        cell.setPhrase(new Phrase("Average High Goals Scored"));
        teleTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.1f", team.getAvgHighGoals())));
        teleTable.addCell(cell);

        cell.setPhrase(new Phrase("Average Assists per Game"));
        teleTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.1f", team.getAvgAssist())));
        teleTable.addCell(cell);

        cell.setPhrase(new Phrase("Average Truss Goals per Game"));
        teleTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.1f", team.getAvgTruss())));
        teleTable.addCell(cell);

        cell.setPhrase(new Phrase("Average Catches per Game"));
        teleTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.1f", team.getAvgCatch())));
        teleTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Matches"));
        generalTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", (double)team.getNumMatches())));
        generalTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Disqualifications"));
        generalTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", (double)team.dqCount)));
        generalTable.addCell(cell);
        
        cell.setPhrase(new Phrase("No Shows"));
        generalTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.0f", (double)team.getNoShows())));
        generalTable.addCell(cell);
        
        cell.setPhrase(new Phrase("Average Points Per Game"));
        generalTable.addCell(cell);
        cell.setPhrase(new Phrase(String.format("%.1f", (team.getTeleScore()+team.getAutonScore()))));
        generalTable.addCell(cell);

        mainContent.add(title);
        mainContent.add(new Paragraph(" "));

        PdfPTable tableTitle = new PdfPTable(1);
        PdfPCell c1 = new PdfPCell(new Phrase("Autonomous", SUB_TITLE));
        c1.setPadding(10f);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableTitle.addCell(c1);
        mainContent.add(tableTitle);
        mainContent.add(autonTable);

        tableTitle = new PdfPTable(1);
        c1 = new PdfPCell(new Phrase("Tele-Operated", SUB_TITLE));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(10f);
        tableTitle.addCell(c1);
        mainContent.add(tableTitle);
        mainContent.add(teleTable);
        
        tableTitle = new PdfPTable(1);
        c1 = new PdfPCell(new Phrase("General", SUB_TITLE));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(10f);
        tableTitle.addCell(c1);
        mainContent.add(tableTitle);
        mainContent.add(generalTable);

        tableTitle = new PdfPTable(1);
        mainContent.add(tableTitle);
        doc.add(mainContent);
        doc.close();
    }
}