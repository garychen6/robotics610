package scoutingemailer;

import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import static scoutingemailer.Frame.notificationLabel;

public class Emailer {

    static final String username = "info@team610.com";
    static final String password = "crescentrobotics";
    
    static Session session;
    
    public static void initSession(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public static void sendMail(String to, ArrayList<String> selectedTeams) {
        notificationLabel.setForeground(Frame.WHITE);
        Frame.notificationLabel.setText("Generating PDFs");
        try {
            DateFormat df = new SimpleDateFormat("h:mm a");
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            String timeString = df.format(Calendar.getInstance().getTime());
            message.setSubject("Team Data as of " + timeString);

            Multipart multipart = new MimeMultipart();
            for (String s : selectedTeams) {
                try {
                    PDFWriter.createPDF(s, Frame.scores.get(s));
                } catch (DocumentException ex) {
                    Logger.getLogger(Emailer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            zip(selectedTeams, "PDF/files.zip");
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            String file = "PDF/files.zip";
            String fileName ="files.zip";
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            Frame.notificationLabel.setText("Sending Email");
            Transport.send(message);
            Frame.notificationLabel.setText("Email Sent");
        } catch (MessagingException e) {
            notificationLabel.setForeground(Frame.RED);
            Frame.notificationLabel.setText("Email Send Failed");
            e.printStackTrace();
        }
    }

    public static void zip(ArrayList<String> srcFiles, String zipFile) {
        try {
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (int i = 0; i < srcFiles.size(); i++) {
                File srcFile = new File("PDF/"+srcFiles.get(i) + ".pdf");
                FileInputStream fis = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();

            }
            zos.close();

        } catch (IOException ioe) {
            System.out.println("Error creating zip file: " + ioe);
        }
    }
    
    private static Session getSession(){
        return session;
    }
}
