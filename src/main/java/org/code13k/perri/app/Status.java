package org.code13k.perri.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Status {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(Status.class);

    // Const
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"; // RFC3339

    // Data
    private final Date mAppStartedDate = new Date();

    /**
     * Singleton
     */
    private static class SingletonHolder {
        static final Status INSTANCE = new Status();
    }

    public static Status getInstance() {
        return Status.SingletonHolder.INSTANCE;
    }

    /**
     * Constructor
     */
    private Status() {
        mLogger.trace("Status()");
    }

    /**
     * Initialize
     */
    public void init() {
        // Timer
        Timer timer = new Timer("StatusLogging");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    logging();
                } catch (Exception e) {
                    // Nothing
                }
            }
        }, 1000, 1000);
    }

    /**
     * Logging
     */
    public void logging() {
        /*
        mLogger.trace("This is TRACE Log!");
        mLogger.debug("This is DEBUG Log!");
        mLogger.info("This is INFO Log!");
        mLogger.warn("This is WARN Log!");
        mLogger.error("This is ERROR Log!");
        */
    }

    /**
     * Get application started time
     */
    public Date getAppStartedDate() {
        return mAppStartedDate;
    }

    /**
     * Get application started time string
     */
    public String getAppStartedDateString(){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String formattedDate = sdf.format(Status.getInstance().getAppStartedDate());
        return formattedDate;
    }

    /**
     * Get current time string
     */
    public String getCurrentDateString(){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String formattedDate = sdf.format(new Date());
        return formattedDate;
    }

    /**
     * Get application running time (hour)
     */
    public int getAppRunningTimeHour(){
        long createdTimestamp = Status.getInstance().getAppStartedDate().getTime();
        long runningTimestamp = System.currentTimeMillis() - createdTimestamp;
        int runningTimeSec = (int) (runningTimestamp / 1000);
        int runningTimeMin = runningTimeSec / 60;
        int runningTimeHour = runningTimeMin / 60;
        return runningTimeHour;
    }




}
