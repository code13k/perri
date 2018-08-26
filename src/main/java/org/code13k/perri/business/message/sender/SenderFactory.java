package org.code13k.perri.business.message.sender;

import org.code13k.perri.config.ChannelConfig;
import org.code13k.perri.model.MessageOperation;

public class SenderFactory {
    private static TelegramSender mTelegramSender = new TelegramSender();
    private static SlackSender mSlackSender = new SlackSender();
    private static WebhookSender mWebhookSender = new WebhookSender();

    public static BasicSender getSender(MessageOperation messageOperation) {
        BasicSender result = null;
        String channelType = messageOperation.getChannelInfo().getType();

        if (channelType.equals(ChannelConfig.ChannelType.TELEGRAM)) {
            result = mTelegramSender;
        } else if (channelType.equals(ChannelConfig.ChannelType.SLACK)) {
            result = mSlackSender;
        } else if (channelType.equals(ChannelConfig.ChannelType.WEBHOOK)) {
            result = mWebhookSender;
        }
        return result;
    }
}
