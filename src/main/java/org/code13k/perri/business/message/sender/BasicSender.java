package org.code13k.perri.business.message.sender;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.apache.commons.lang3.StringUtils;
import org.code13k.perri.app.Env;
import org.code13k.perri.model.Message;
import org.code13k.perri.model.MessageOperation;
import org.code13k.perri.model.config.channel.ChannelInfo;
import org.code13k.perri.model.config.channel.TelegramInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class BasicSender {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(BasicSender.class);

    // Data
    private WebClient mWebClient = null;

    /**
     * Send Result
     */
    public class SendResult {
        public static final int SUCCESS = 1;
        public static final int FAILURE = 2;
        public static final int TEMPORARY_FAILURE = 3;
    }

    /**
     * Constructor
     */
    public BasicSender() {
        mLogger.trace("BasicSender()");

        // Set user-agent
        StringBuffer sb = new StringBuffer();
        sb.append("Code13k-Perri/");
        sb.append(Env.getInstance().getVersionString());
        sb.append(" (");
        sb.append("message sender");
        sb.append(")");
        String userAgent = sb.toString();
        mLogger.trace("User-Agent = " + userAgent);

        // WebClientOptions
        WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setUserAgent(userAgent);
        webClientOptions.setTrustAll(true);
        webClientOptions.setSsl(true);
        webClientOptions.setTryUseCompression(true);
        webClientOptions.setConnectTimeout(10 * 1000); // 10 Seconds
        webClientOptions.setIdleTimeout(20 * 1000); // 20 Seconds

        // Init VertxOptions
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setEventLoopPoolSize(1);

        // Create WebClient
        mWebClient = WebClient.create(Vertx.vertx(vertxOptions), webClientOptions);
    }

    /**
     * Get web client object for using http
     */
    protected WebClient getWebClient() {
        return mWebClient;
    }

    /**
     * Build message for display
     */
    public static String buildDisplayMessage(MessageOperation messageOperation, int maximumNumberOfCharacters) {
        Message message = messageOperation.getMessage();
        ChannelInfo channelInfo = messageOperation.getChannelInfo();

        // Build tags string
        StringBuffer sbTags = new StringBuffer();
        if (channelInfo.isDisplayTags() == true) {
            ArrayList<String> tags = message.getTags();
            if (tags != null) {
                tags.forEach(tag -> {
                    sbTags.append("#");
                    sbTags.append(tag);
                    sbTags.append(" ");
                });
            }
        }
        String tagsString = sbTags.toString();

        // Build duplicate string
        StringBuffer sbDuplicate = new StringBuffer();
        if (messageOperation.getMessageCount() > 1) {
            sbDuplicate.append("(Received ");
            sbDuplicate.append(messageOperation.getMessageCount());
            sbDuplicate.append(" duplicate messages)");
        }
        String duplicateString = sbDuplicate.toString();

        // Build message string
        String messageString = message.getText();

        // Calculate
        int etcLength = 4;
        int totalLength = tagsString.length() + duplicateString.length() + messageString.length() + etcLength;
        if (maximumNumberOfCharacters < totalLength) {
            String ellipsisString = " ...";
            int ellipsisLength = ellipsisString.length() + 1;
            int overLength = (totalLength - maximumNumberOfCharacters) + ellipsisLength;
            messageString = messageString.substring(0, messageString.length() - overLength);
            messageString = messageString + ellipsisString;
        }

        // Build result string
        StringBuffer sbResult = new StringBuffer();
        sbResult.append(messageString);
        if(StringUtils.isEmpty(tagsString)==false) {
            sbResult.append("\n");
            sbResult.append(tagsString);
        }
        if(StringUtils.isEmpty(duplicateString)==false){
            sbResult.append("\n");
            sbResult.append(duplicateString);
        }

        // End
        return sbResult.toString();
    }

    /**
     * Send message
     */
    public abstract void send(MessageOperation messageOperation, Consumer<Integer> consumer);
}






