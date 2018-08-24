package org.code13k.perri.business.message.sender;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.code13k.perri.model.Message;
import org.code13k.perri.model.MessageOperation;
import org.code13k.perri.model.config.channel.SlackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.function.Consumer;

public class SlackSender extends BasicSender {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(SlackSender.class);

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
            SlackInfo slackInfo = (SlackInfo)messageOperation.getChannelInfo();
            String requestUri = slackInfo.getWebhookUrl();

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
            Message message = messageOperation.getMessage();
            SlackInfo slackInfo = (SlackInfo) messageOperation.getChannelInfo();

            // Make additional string
            StringBuffer sb1 = new StringBuffer();
            if (slackInfo.isDisplayTags() == true) {
                ArrayList<String> tags = message.getTags();
                if (tags != null) {
                    tags.forEach(tag -> {
                        sb1.append("#");
                        sb1.append(tag);
                        sb1.append(" ");
                    });
                }
            }
            String additionalText = sb1.toString();

            // Make Sending Message Text
            StringBuffer sb2 = new StringBuffer();
            sb2.append(message.getText());
            if(StringUtils.isEmpty(additionalText)==false){
                sb2.append("\n");
                sb2.append(additionalText);
            }
            if (messageOperation.getMessageCount() > 1) {
                sb2.append("\n");
                sb2.append("(Received ");
                sb2.append(messageOperation.getMessageCount());
                sb2.append(" duplicate messages)");
            }
            String text = sb2.toString();

            // Make Body
            JsonObject vertxJsonObject = new JsonObject();
            vertxJsonObject.put("text", text);

            // End
            mLogger.debug("SlackSender Body : " + vertxJsonObject.toString());
            return vertxJsonObject;
        } catch (Exception e) {
            mLogger.error("Failed to make slack body", e);
        }
        return null;
    }
}
