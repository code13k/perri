package org.code13k.perri.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Status {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(Status.class);

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
}
