package org.code13k.perri.config;

import org.code13k.perri.model.config.app.PortInfo;
import org.code13k.perri.model.config.app.WebhookInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;

public class AppConfig extends BasicConfig {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(AppConfig.class);

    // Data
    private PortInfo mPortInfo = new PortInfo();
    private WebhookInfo mWebhookInfo = new WebhookInfo();

    /**
     * Singleton
     */
    private static class SingletonHolder {
        static final AppConfig INSTANCE = new AppConfig();
    }

    public static AppConfig getInstance() {
        return AppConfig.SingletonHolder.INSTANCE;
    }

    /**
     * Constructor
     */
    private AppConfig() {
        mLogger.trace("AppConfig()");
    }

    @Override
    protected String getDefaultConfigFilename() {
        return "default_app_config.yaml";
    }

    @Override
    protected String getConfigFilename() {
        return "app_config.yaml";
    }

    @Override
    protected boolean loadConfig(final String content, final String filePath) {
        try {
            Yaml yaml = new Yaml();
            LinkedHashMap yamlObject = yaml.load(content);
            mLogger.trace("yamlObject class name = " + yamlObject.getClass().getName());
            mLogger.trace("yamlObject = " + yamlObject);

            // PortInfo
            LinkedHashMap portObject = (LinkedHashMap) yamlObject.get("port");
            mLogger.trace("portObject class name = " + portObject.getClass().getName());
            mLogger.trace("portObject = " + portObject);
            Integer portMain = (Integer) portObject.get("main");
            if (portMain == null) {
                mLogger.error("Invalid main port");
                return false;
            }
            Integer portApiHttp = (Integer) portObject.get("api_http");
            if (portApiHttp == null) {
                mLogger.error("Invalid api_http port");
                return false;
            }
            if (portMain == portApiHttp) {
                mLogger.error("Duplicated port bind : main=" + portMain + ", api_http=" + portApiHttp);
                return false;
            }
            mPortInfo.setMain(portMain);
            mPortInfo.setApiHttp(portApiHttp);

            // Webhook for alarm
            LinkedHashMap webhookObject = (LinkedHashMap) yamlObject.get("webhook");
            if (webhookObject != null) {
                mLogger.trace("webhookObject class name = " + portObject.getClass().getName());
                LinkedHashMap webhookAlarmObject = (LinkedHashMap) webhookObject.get("alarm");
                if (webhookAlarmObject != null) {
                    mLogger.trace("webhookAlarmObject class name = " + portObject.getClass().getName());
                    mLogger.debug("webhookAlarmObject = " + webhookAlarmObject);
                    mWebhookInfo.setAlarmDebug((String) webhookAlarmObject.get("debug"));
                    mWebhookInfo.setAlarmInfo((String) webhookAlarmObject.get("info"));
                    mWebhookInfo.setAlarmError((String) webhookAlarmObject.get("error"));
                }
            }
        } catch (Exception e) {
            mLogger.error("Failed to load config file", e);
            return false;
        }
        return true;
    }

    @Override
    public void logging() {
        // Begin
        mLogger.info("------------------------------------------------------------------------");
        mLogger.info("Application Configuration");
        mLogger.info("------------------------------------------------------------------------");

        // Config File Path
        mLogger.info("Config file path = " + getConfigFilename());

        // PortInfo
        mLogger.info("PortInfo of main = " + mPortInfo.getMain());
        mLogger.info("PortInfo of api_http = " + mPortInfo.getApiHttp());

        // Webhook for log
        mLogger.info("Webhook for alarm (debug) = " + mWebhookInfo.getAlarmDebug());
        mLogger.info("Webhook for alarm (info) = " + mWebhookInfo.getAlarmInfo());
        mLogger.info("Webhook for alarm (error) = " + mWebhookInfo.getAlarmError());

        // End
        mLogger.info("------------------------------------------------------------------------");
    }

    /**
     * Get port
     */
    public PortInfo getPort() {
        return mPortInfo;
    }

    /**
     * Get webhook for log
     */
    public WebhookInfo getWebhookLog() {
        return mWebhookInfo;
    }
}
