package org.code13k.perri.business.message.sender;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import org.code13k.perri.model.MessageOperation;
import org.code13k.perri.model.config.channel.DiscordInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class DiscordSender extends BasicSender {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(DiscordSender.class);

    @Override
    public void send(MessageOperation messageOperation, Consumer<Integer> consumer) {
        // Exception
        if (messageOperation == null) {
            mLogger.error("Parameter messageOperation is null. It's invalid");
            consumer.accept(SendResult.FAILURE);
            return;
        }

        try {
            // Get request uri
            DiscordInfo discordInfo = (DiscordInfo)messageOperation.getChannelInfo();
            String requestUri = discordInfo.getIncomingWebhookUrl();

            // Request
            getWebClient().postAbs(requestUri).sendJsonObject(makeBody(messageOperation), request -> {
                // The request has succeeded.
                if (request.succeeded()) {
                    final HttpResponse response = request.result();
                    final int responseStatusCode = response.statusCode();
                    final String responseStatusMessage = response.statusMessage();
                    final String responseBody = response.bodyAsString();
                    mLogger.debug("Response status code = " + responseStatusCode);
                    mLogger.debug("Response status message = " + responseStatusMessage);
                    mLogger.debug("Response body = " + responseBody);

                    // 200 OK
                    if (responseStatusCode == 200) {
                        consumer.accept(SendResult.SUCCESS);
                    }

                    // Not supported
                    else {
                        mLogger.error("(Not supported) Response status code = " + responseStatusCode);
                        mLogger.error("(Not supported) Response status message = " + responseStatusMessage);
                        mLogger.error("(Not supported) Response body = " + responseBody);
                        consumer.accept(SendResult.FAILURE);
                    }
                }

                // The request has failed.
                else {
                    mLogger.error("The request has failed", request.cause());
                    consumer.accept(SendResult.TEMPORARY_FAILURE);
                }

            });
        } catch (Exception e) {
            mLogger.error("Failed to send message", e);
            consumer.accept(SendResult.FAILURE);
        }
    }

    /**
     * Make body
     */
    private JsonObject makeBody(MessageOperation messageOperation) {
        try {
            String text = buildDisplayMessage(messageOperation, 2000);

            // Make Body
            JsonObject vertxJsonObject = new JsonObject();
            vertxJsonObject.put("content", text);

            // End
            mLogger.debug("DiscordSender Body : " + vertxJsonObject.toString());
            return vertxJsonObject;
        } catch (Exception e) {
            mLogger.error("Failed to make discord body", e);
        }
        return null;
    }
}
