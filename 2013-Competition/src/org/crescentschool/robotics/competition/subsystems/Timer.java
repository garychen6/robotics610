package org.crescentschool.robotics.competition.subsystems;

/**
 *  A simple Timer to measure execution time on crio.  
 *  Usage:  Timer t = Timer.getTimer("label");   
 *          // do some work; 
 *          t.stop();  // the elapsed time is logged using the Logger
 * 
 */
public class Timer {
    
    String key;
    long startTime, endTime;
    private static Logger logger = Logger.getLogger();
    
    public static Timer getTimer(String key) {
        Timer timer = new Timer();
        timer.key = key;
        timer.startTime = System.currentTimeMillis();
        return timer;
    }
    
    public void stop() {
        endTime = System.currentTimeMillis();
        logger.debug("Timer: " + key + ": " + (endTime -startTime) + " ms.");
    }
    
}
