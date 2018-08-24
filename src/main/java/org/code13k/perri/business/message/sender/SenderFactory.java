package org.code13k.perri.business.message.sender;

import org.code13k.perri.config.ChannelConfig;
import org.code13k.perri.model.MessageOperation;

public class SenderFactory {
    public static BasicSender getSender(MessageOperation messageOperation) {
        BasicSender result = null;
        String channelType = messageOperation.getChannelInfo().getType();

        if (channelType.equals(ChannelConfig.ChannelType.TELEGRAM)) {
            result = new TelegramSender();
        } else if (channelType.equals(ChannelConfig.ChannelType.SLACK)) {
            result = new SlackSender();
        } else if (channelType.equals(ChannelConfig.ChannelType.WEBHOOK)) {
            result = new WebhookSender();
        }
        return result;
    }
}
