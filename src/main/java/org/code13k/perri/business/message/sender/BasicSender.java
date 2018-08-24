package org.code13k.perri.business.message.sender;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.code13k.perri.app.Env;
import org.code13k.perri.model.MessageOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        sb.append("Perri/");
        sb.append(Env.getInstance().getVersionString());
        sb.append(" (");
        sb.append("message sender");
        sb.append(")");
        String userAgent = sb.toString();
        mLogger.debug("User-Agent = " + userAgent);

        // Init WebClient
        WebClientOptions options = new WebClientOptions();
        options.setUserAgent(userAgent);
        options.setTrustAll(true);
        options.setSsl(true);
        options.setTryUseCompression(true);
        options.setConnectTimeout(10 * 1000); // 10 Seconds
        options.setIdleTimeout(20 * 1000); // 20 Seconds
        mWebClient = WebClient.create(Vertx.vertx(), options);
    }

    /**
     * Get web client object for using http
     */
    protected WebClient getWebClient() {
        return mWebClient;
    }

    /**
     * Send message
     */
    public abstract void send(MessageOperation messageOperation, Consumer<Integer> consumer);
}
