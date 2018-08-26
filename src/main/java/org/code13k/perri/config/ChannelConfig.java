package org.code13k.perri.config;

import org.apache.commons.lang3.StringUtils;
import org.code13k.perri.model.config.channel.ChannelInfo;
import org.code13k.perri.model.config.channel.SlackInfo;
import org.code13k.perri.model.config.channel.TelegramInfo;
import org.code13k.perri.model.config.channel.WebhookInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ChannelConfig extends BasicConfig {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(ChannelConfig.class);

    // Data
    private HashMap<String, ArrayList<ChannelInfo>> mConfigChannelList = new HashMap<>();

    /**
     * Channel Type
     */
    public class ChannelType {
        public static final String TELEGRAM = "telegram";
        public static final String SLACK = "slack";
        public static final String WEBHOOK = "webhook";
    }

    /**
     * Singleton
     */
    private static class SingletonHolder {
        static final ChannelConfig INSTANCE = new ChannelConfig();
    }

    public static ChannelConfig getInstance() {
        return ChannelConfig.SingletonHolder.INSTANCE;
    }

    /**
     * Constructor
     */
    private ChannelConfig() {
        mLogger.trace("ChannelConfig()");
    }

    /**
     * Get channel list by key
     */
    public ArrayList<ChannelInfo> getChannelListByKey(String channelKey) {
        if (StringUtils.isEmpty(channelKey) == false) {
            ArrayList<ChannelInfo> channelList = mConfigChannelList.get(channelKey);
            mLogger.trace("channelList = " + channelList);
            return channelList;
        }
        return null;
    }

    /**
     * Check channel key
     */
    public boolean isAvailableKey(String channelKey) {
        ArrayList<ChannelInfo> channelList = getChannelListByKey(channelKey);
        if (channelList != null && channelList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    protected String getDefaultConfigFilename() {
        return "default_channel_config.yml";
    }

    @Override
    protected String getConfigFilename() {
        return "channel_config.yml";
    }

    @Override
    protected boolean loadConfig(final String content, final String filePath) {
        try {
            Yaml yaml = new Yaml();
            LinkedHashMap<String, ArrayList<LinkedHashMap>> yamlObject = yaml.load(content);
            mLogger.trace("yamlObject class name = " + yamlObject.getClass().getName());
            mLogger.trace("yamlObject = " + yamlObject);

            // Get data
            yamlObject.forEach((key, value) -> {
                mLogger.trace("value class name = " + value.getClass().getName());
                mLogger.trace("[" + key + "]");
                if (false == mConfigChannelList.containsKey(key)) {
                    mConfigChannelList.put(key, new ArrayList<ChannelInfo>());
                }
                final ArrayList<ChannelInfo> channelList = (ArrayList<ChannelInfo>) mConfigChannelList.get(key);
                value.forEach(valueItem -> {
                    mLogger.trace("valueItem = " + valueItem);

                    // Type
                    mLogger.trace("valueItem class name = " + valueItem.getClass().getName());
                    LinkedHashMap valueItemObject = valueItem;
                    String type = (String) valueItemObject.get("type");

                    // Common
                    Boolean displayTags = (Boolean) valueItemObject.getOrDefault("display_tags", true);
                    Boolean mergeDuplicateMessage = (Boolean) valueItemObject.getOrDefault("merge_duplicate_message", true);

                    // Telegram
                    if (type.equalsIgnoreCase(ChannelType.TELEGRAM)) {
                        String botId = (String) valueItemObject.getOrDefault("bot_id", "");
                        String chatId = (String) valueItemObject.getOrDefault("chat_id", "");

                        // Set Telegram
                        TelegramInfo telegramInfo = new TelegramInfo();
                        telegramInfo.setType(ChannelType.TELEGRAM);
                        telegramInfo.setBotId(botId);
                        telegramInfo.setChatId(chatId);
                        telegramInfo.setDisplayTags(displayTags);
                        telegramInfo.setMergeDuplicateMessage(mergeDuplicateMessage);

                        // Check validation
                        if (StringUtils.isEmpty(telegramInfo.getBotId()) == true) {
                            mLogger.error("Invalid telegram channel (bot_id is invalid)");
                        } else if (StringUtils.isEmpty(telegramInfo.getChatId()) == true) {
                            mLogger.error("Invalid telegram channel (chat_id is invalid)");
                        } else {
                            channelList.add(telegramInfo);
                        }
                    }

                    // Slack
                    else if (type.equalsIgnoreCase(ChannelType.SLACK)) {
                        String webhookUrl = (String) valueItemObject.getOrDefault("webhook_url", "");

                        // Set Slack
                        SlackInfo slackInfo = new SlackInfo();
                        slackInfo.setType(ChannelType.SLACK);
                        slackInfo.setWebhookUrl(webhookUrl);
                        slackInfo.setDisplayTags(displayTags);
                        slackInfo.setMergeDuplicateMessage(mergeDuplicateMessage);

                        // Check validation
                        if (StringUtils.isEmpty(slackInfo.getWebhookUrl()) == true) {
                            mLogger.error("Invalid slack channel (webhook_url is invalid)");
                        } else {
                            channelList.add(slackInfo);
                        }
                    }

                    // Webhook
                    else if (type.equalsIgnoreCase(ChannelType.WEBHOOK)) {
                        String webhookUrl = (String) valueItemObject.getOrDefault("webhook_url", "");

                        // Set Webhook
                        WebhookInfo webhookInfo = new WebhookInfo();
                        webhookInfo.setType(ChannelType.WEBHOOK);
                        webhookInfo.setWebhookUrl(webhookUrl);
                        webhookInfo.setDisplayTags(displayTags);
                        webhookInfo.setMergeDuplicateMessage(mergeDuplicateMessage);

                        // Check validation
                        if (StringUtils.isEmpty(webhookInfo.getWebhookUrl()) == true) {
                            mLogger.error("Invalid webhook channel (webhook_url is invalid)");
                        } else {
                            channelList.add(webhookInfo);
                        }
                    }

                    // Not supported
                    else {
                        mLogger.warn("Not supported type : " + type);
                    }
                });
            });
        } catch (Exception e) {
            mLogger.error("Failed to load config file", e);
            return false;
        }
        return true;
    }

    @Override
    protected void logging() {
        // Begin
        mLogger.info("------------------------------------------------------------------------");
        mLogger.info("Channel Configuration");
        mLogger.info("------------------------------------------------------------------------");

        // Config File Path
        mLogger.info("Config file path = " + getConfigFilename());

        // Channel
        mConfigChannelList.forEach((key, value) -> {
            value.forEach(valueItem -> {
                mLogger.info(key + " = " + valueItem.getClass().getSimpleName() + " # " + valueItem);
            });
        });

        // End
        mLogger.info("------------------------------------------------------------------------");
    }
}
