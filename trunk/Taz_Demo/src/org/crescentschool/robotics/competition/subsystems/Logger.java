package org.crescentschool.robotics.competition.subsystems;

import com.sun.squawk.microedition.io.FileConnection;
import javax.microedition.io.Connector;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

/**
 *  This is a basic Logger class that provides multi-level logging to
 *  System.out or a Flat file on the crio.  4=DEBUG, 3=INFO, 2=WARN, 1=ERROR
 *  The flat file can be retrieved using ftp (e.g. ftp://10.6.10.2/log.txt)
 * 
 *  //TODO:  add additional logic to support logging to SmartBoard
 */
public class Logger {

    private final String mode = "File";    // Console or File or SmartBoard
    private final int level = 4;              // 4=DEBUG, 3=INFO, 2=WARN, 1=ERROR
    private final String FILENAME = "log.txt";
    private OutputStreamWriter writer = null;
    private static Logger instance = new Logger();

    private Logger() {
        super();
        if ("File".equalsIgnoreCase(mode)) {
            FileConnection fc;

            try {

                fc = (FileConnection) Connector.open("file:///"+FILENAME, Connector.WRITE);
                writer = new OutputStreamWriter(fc.openOutputStream());

            } catch (Exception e) {
                System.err.println(e);
            }
        } 
    }

    public static Logger getLogger() {
        return instance;
    }

    private String getCurrentDateString() {
        
        Calendar cal = Calendar.getInstance();
        StringBuffer date = new StringBuffer();

        date.append(cal.get(Calendar.YEAR)).append("-");
        date.append(cal.get(Calendar.MONTH) + 1).append('-');
        date.append(cal.get(Calendar.DATE)).append('-');
        date.append(cal.get(Calendar.HOUR_OF_DAY)).append(':');
        date.append(cal.get(Calendar.MINUTE)).append(':');
        date.append(cal.get(Calendar.SECOND)).append('.');
        date.append(cal.get(Calendar.MILLISECOND));
        
        return new String (date);
        
    }

    private void writeToLog(String message) {

        String outputMessage = getCurrentDateString() + " " + message;
        if ("Console".equals(mode)) {
            System.out.println(outputMessage);

        } else if ("File".equalsIgnoreCase(mode)) {
            try {
                writer.write(outputMessage + "\r\n");
            } catch (IOException ioe) {
                System.err.println("unable to log to file " + ioe);
            }
        }
    }

    public void debug(String debugString) {

        if (level >= 4) {
            writeToLog("DEBUG: " + debugString);
        }

    }

    public void info(String infoString) {

        if (level >= 3) {
            writeToLog("INFO: " + infoString);
        }

    }

    public void warn(String warnString) {

        if (level >= 2) {
            writeToLog("WARN: " + warnString);
        }

    }

    public void error(String errorString) {

        if (level >= 1) {
            writeToLog("ERROR: " + errorString);
        }

    }

    public void entry(String methodName) {

        debug("> " + methodName);

    }

    public void exit(String methodName) {

        debug("< " + methodName);

    }

    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
    
    public void flush() {
        try {
            if (writer != null) {
                writer.flush();
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

}
