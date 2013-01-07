
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloPDF {

    private static String FILE = "c:/temp/ScoutingSheet.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.NORMAL, BaseColor.RED);
    private static Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.NORMAL, BaseColor.BLUE);

    public static void main(String[] args) {
        try {

            int[] teamNumbers = new int[3];
            teamNumbers[0] = 610;
            teamNumbers[1] = 611;
            teamNumbers[2] = 612;
            TeamSheet sheet = new TeamSheet("610");
            createScoutingSheet(sheet, sheet, sheet);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createScoutingSheet(TeamSheet blue1, TeamSheet blue2, TeamSheet blue3)
            throws DocumentException {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
        } catch (FileNotFoundException ex) {
        }
        document.open();

        Paragraph content = new Paragraph();



        PdfPTable table = new PdfPTable(4);

        PdfPCell c1 = new PdfPCell(new Phrase("Field"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(blue1.teamName));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(blue2.teamName));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(blue3.teamName));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);




        table.addCell("# of Matches");
        table.addCell(blue1.numMatches + "");
        table.addCell(blue2.numMatches + "");
        table.addCell(blue3.numMatches + "");
        table.addCell("Starts Auto");
        table.addCell("Auton Location");
        table.addCell("Auton Location");
        table.addCell("Auton Location");
        table.addCell("Auto Pts");
        table.addCell("Average Auton ");
        table.addCell("Average Auton ");
        table.addCell("Average Auton ");

        table.addCell("Auto Scoring %");
        table.addCell("Scoring ");
        table.addCell("Scoring ");
        table.addCell("Scoring ");

        table.addCell("Teleop Avg Pts");
        table.addCell("Average Points ");
        table.addCell("Average Points ");
        table.addCell("Average Points ");

        table.addCell("Defensive Rating");
        table.addCell("Defense Rating");
        table.addCell("Defense Rating");
        table.addCell("Defense Rating");

        table.addCell("Time Hang");
        table.addCell("Time");
        table.addCell("Time");
        table.addCell("Time");

        table.addCell("Hang Level");
        table.addCell("Level");
        table.addCell("Level");
        table.addCell("Level");

        table.addCell("KPR");
        table.addCell("99");
        table.addCell("100");
        table.addCell(">9000");







        PdfPTable table1 = new PdfPTable(4);

        c1 = new PdfPCell(new Phrase("Field"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.addCell(c1);

        c1 = new PdfPCell(new Phrase(blue1.teamName));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.addCell(c1);

        c1 = new PdfPCell(new Phrase(blue2.teamName));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.addCell(c1);

        c1 = new PdfPCell(new Phrase(blue3.teamName));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.addCell(c1);



        table1.addCell("# of Matches");
        table1.addCell(blue1.numMatches + "");
        table1.addCell(blue2.numMatches + "");
        table1.addCell(blue3.numMatches + "");
        table1.addCell("Starts Auto");
        table1.addCell("Auton Location");
        table1.addCell("Auton Location");
        table1.addCell("Auton Location");
        table1.addCell("Auto Pts");
        table1.addCell("Average Auton ");
        table1.addCell("Average Auton ");
        table1.addCell("Average Auton ");

        table1.addCell("Auto Scoring %");
        table1.addCell("Scoring ");
        table1.addCell("Scoring ");
        table1.addCell("Scoring ");

        table1.addCell("Teleop Avg Pts");
        table1.addCell("Average Points ");
        table1.addCell("Average Points ");
        table1.addCell("Average Points ");

        table1.addCell("Defensive Rating");
        table1.addCell("Defense Rating");
        table1.addCell("Defense Rating");
        table1.addCell("Defense Rating");

        table1.addCell("Time Hang");
        table1.addCell("Time");
        table1.addCell("Time");
        table1.addCell("Time");

        table1.addCell("Hang Level");
        table1.addCell("Level");
        table1.addCell("Level");
        table1.addCell("Level");

        table1.addCell("KPR");
        table1.addCell("99");
        table1.addCell("100");
        table1.addCell(">9000");

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

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}