package org.code13k.perri.business.message.sender;

import io.vertx.ext.web.client.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.code13k.perri.model.Message;
import org.code13k.perri.model.MessageOperation;
import org.code13k.perri.model.config.channel.TelegramInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.function.Consumer;

public class TelegramSender extends BasicSender {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(TelegramSender.class);

    /**
     * Send message to telegram
     */
    @Override
    public void send(MessageOperation messageOperation, Consumer<Integer> consumer) {
        // Exception
        if (messageOperation == null) {
            mLogger.error("Parameter messageOperation is null. It's invalid");
            consumer.accept(SendResult.FAILURE);
            return;
        }

        try {
            getWebClient().getAbs(makeTelegramUri(messageOperation)).send(request -> {
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
     * Make telegram uri
     */
    private String makeTelegramUri(MessageOperation messageOperation) {
        try {
            TelegramInfo telegramInfo = (TelegramInfo) messageOperation.getChannelInfo();
            String text = buildDisplayMessage(messageOperation, 4000);
            String urlEncodedText = URLEncoder.encode(text, "UTF-8");

            // Make URL
            String bot_id = telegramInfo.getBotId();
            String chat_id = telegramInfo.getChatId();
            String uriFormat = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            String uri = String.format(uriFormat, bot_id, chat_id, urlEncodedText);

            // End
            mLogger.debug("TelegramSender URI : " + uri);
            return uri;
        } catch (Exception e) {
            mLogger.error("Failed to make telegram uri", e);
        }
        return null;
    }
}
