
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

    private static String FILE = "C:/Users/Warfa/Desktop/DATABASE/ScoutingSheet.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.NORMAL, BaseColor.RED);
    private static Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.NORMAL, BaseColor.BLUE);
    
    HelloPDF(String FILE) {
        this.FILE = FILE;
    }
    
    
    public static void main(String[] args) {
        try {


            TeamSheet sheet = new TeamSheet("610");
            createMatchPDF(sheet, sheet, sheet, sheet, sheet, sheet);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createMatchPDF(TeamSheet blue1, TeamSheet blue2, TeamSheet blue3, TeamSheet red1, TeamSheet red2, TeamSheet red3)
            throws DocumentException {
        //Create a document
        Document document = new Document();
        try {
            //Make the PDF writer
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
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

        c1 = new PdfPCell(new Phrase(blue1.teamName));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(blue2.teamName));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(blue3.teamName));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        //Add the cells for number of matches
        table.addCell("# of Matches");
        table.addCell(blue1.get + "");
        table.addCell(blue2.get + "");
        table.addCell(blue3.get + "");
        
        //Add the cells for the starting positions
        table.addCell("Starts Auto");
        table.addCell(blue1.get +"");
        table.addCell(blue2.get +"");
        table.addCell(blue3.get +"");
        
        //Add the cells for auto points
        table.addCell("Auto Pts");
        table.addCell(blue1.get +"");
        table.addCell(blue2.get +"");
        table.addCell(blue3.get +"");

        //Add the cells for teleop scoring percentage
        table.addCell("Teleop Scoring %");
        table.addCell(blue1.get +"");
        table.addCell(blue2.get +"");
        table.addCell(blue3.get +"");

        //Add the cells for the average points in teleop
        table.addCell("Teleop Avg Pts");
        table.addCell(blue1.get +"");
        table.addCell(blue2.get +"");
        table.addCell(blue3.get +"");

        //Add the cells for defensive rating
        table.addCell("Defensive Rating");
        table.addCell(blue1.get +"");
        table.addCell(blue2.get +"");
        table.addCell(blue3.get +"");

        //Add the cells for the time required to hang
        table.addCell("Time Hang");
        table.addCell(blue1.get +"");
        table.addCell(blue2.get +"");
        table.addCell(blue3.get +"");

        //Add the cells for the level the robot can hang at
        table.addCell("Hang Level");
        table.addCell(blue1.get +"");
        table.addCell(blue2.get +"");
        table.addCell(blue3.get +"");

        //Add the cells for the average KPR
        table.addCell("Avg KPR");
        table.addCell(blue1.get +"");
        table.addCell(blue2.get +"");
        table.addCell(blue3.get +"");



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


        //Add cells for the number of matches played
        table1.addCell("# of Matches");
        table1.addCell(blue1.get +"");
        table1.addCell(blue2.get +"");
        table1.addCell(blue3.get +"");
        
        //Add cells for the starting location
        table1.addCell("Starts Auto");
        table1.addCell(blue1.get +"");
        table1.addCell(blue2.get +"");
        table1.addCell(blue3.get +"");
        
        //Add cells for the average auto points
        table1.addCell("Auto Pts");
        table1.addCell(blue1.get +"");
        table1.addCell(blue2.get +"");
        table1.addCell(blue3.get +"");

        //Add cells for the teleop scoring percentage
        table1.addCell("Teleop Scoring %");
        table1.addCell(blue1.get +"");
        table1.addCell(blue2.get +"");
        table1.addCell(blue3.get +"");

        //Add cells for the teleop average points
        table1.addCell("Teleop Avg Pts");
        table1.addCell(blue1.get +"");
        table1.addCell(blue2.get +"");
        table1.addCell(blue3.get +"");

        //Add cells for the defensive rating
        table1.addCell("Defensive Rating");
        table1.addCell(blue1.get +"");
        table1.addCell(blue2.get +"");
        table1.addCell(blue3.get +"");

        //Add cells for the time required to hang
        table1.addCell("Time Hang");
        table1.addCell(blue1.get +"");
        table1.addCell(blue2.get +"");
        table1.addCell(blue3.get +"");

        //Add cells for the highest level of hang
        table1.addCell("Hang Level");
        table1.addCell(blue1.get +"");
        table1.addCell(blue2.get +"");
        table1.addCell(blue3.get +"");

        //Add cells for average KPR.
        table1.addCell("Avg KPR");
        table1.addCell(blue1.get +"");
        table1.addCell(blue2.get +"");
        table1.addCell(blue3.get +"");

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

    public static void createTeamPDF(MatchSheet[] matches)
            throws DocumentException {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
        } catch (FileNotFoundException ex) {
        }
        document.open();

        Paragraph content = new Paragraph();
        
        
        PdfPTable table = new PdfPTable(matches.length + 1);
        
        PdfPCell c1 = new PdfPCell(new Phrase("Match #"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        table.addCell(c1);
        
        for (int i = 0; i < matches.length; i++) {
            c1 = new PdfPCell(new Phrase(matches[i].matchNum));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        
        table.addCell("Starting Position");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].getStart);
        }
        
        table.addCell("Auto Points");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        table.addCell("Teleop Low Attempted");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        table.addCell("Teleop Low Scored");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        table.addCell("Teleop Middle Attempted");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        table.addCell("Teleop Middle Scored");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        table.addCell("Teleop High Attempted");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        table.addCell("Teleop High Scored");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        table.addCell("Teleop Pyramid Attempted");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        table.addCell("Teleop Pyramid Scored");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        table.addCell("Defense Rating");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        table.addCell("Hang Level");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        table.addCell("Hang Time");
        for (int i = 0; i < matches.length; i++) {
            table.addCell(matches[i].get);
        }
        
        Paragraph paragraph = new Paragraph(matches.teamName, catFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        content.add(paragraph);
        addEmptyLine(content, 1);
        content.add(table);

        document.add(content);
        document.close();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
