package org.code13k.perri;

import io.vertx.core.Vertx;
import org.code13k.perri.config.AppConfig;
import org.code13k.perri.config.LogConfig;
import org.code13k.perri.config.ChannelConfig;
import org.code13k.perri.app.Env;
import org.code13k.perri.app.Status;
import org.code13k.perri.service.api.ApiHttpServer;
import org.code13k.perri.service.main.MainHttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    /**
     * This is a exceptional code for logging.
     * It depends on LogConfig class.
     * If you modified it, you must modify LogConfig class.
     *
     * @see org.code13k.perri.config.LogConfig
     */
    static {
        System.setProperty("logback.configurationFile", "config/logback.xml");
    }

    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(Main.class);

    /**
     * Main
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        // Logs
        mLogger.trace("This is TRACE Log!");
        mLogger.debug("This is DEBUG Log!");
        mLogger.info("This is INFO Log!");
        mLogger.warn("This is WARN Log!");
        mLogger.error("This is ERROR Log!");

        // Arguments
        if (args != null) {
            int argsLength = args.length;
            if (argsLength > 0) {
                mLogger.info("------------------------------------------------------------------------");
                mLogger.info("Arguments");
                mLogger.info("------------------------------------------------------------------------");
                for (int i = 0; i < argsLength; i++) {
                    mLogger.info("Args " + i + " = " + args[i]);
                }
                mLogger.info("------------------------------------------------------------------------");

            }
        }

        // System Properties
        mLogger.info("------------------------------------------------------------------------");
        mLogger.info("System Property");
        mLogger.info("------------------------------------------------------------------------");
        System.getProperties().forEach((key, value) -> {
            mLogger.info(key + " = " + value);
        });
        mLogger.info("------------------------------------------------------------------------");

        // Initialize
        try {
            LogConfig.getInstance().init();
            AppConfig.getInstance().init();
            ChannelConfig.getInstance().init();
            Env.getInstance().init();
            Status.getInstance().init();
        } catch (Exception e) {
            mLogger.error("Failed to initialize", e);
            return;
        }

        // Run
        Vertx.vertx().deployVerticle(MainHttpServer.class.getName());
        Vertx.vertx().deployVerticle(ApiHttpServer.class.getName());
        mLogger.info("Running application is successful.");
    }
}
