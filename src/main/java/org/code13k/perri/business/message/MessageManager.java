package org.code13k.perri.business.message;

import org.code13k.perri.config.ChannelConfig;
import org.code13k.perri.model.Message;
import org.code13k.perri.model.MessageOperation;
import org.code13k.perri.model.config.channel.ChannelInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class MessageManager {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(MessageManager.class);

    // Data
    private MessageOperator mMessageOperator = new MessageOperator();

    /**
     * Singleton
     */
    private static class SingletonHolder {
        static final MessageManager INSTANCE = new MessageManager();
    }

    public static MessageManager getInstance() {
        return MessageManager.SingletonHolder.INSTANCE;
    }

    /**
     * Constructor
     */
    private MessageManager() {
        mLogger.trace("MessageManager()");
    }

    /**
     * Send message
     */
    public void send(Message message) {
        ArrayList<ChannelInfo> channelList = ChannelConfig.getInstance().getChannelListByKey(message.getChannel());
        channelList.forEach(channel -> {
            mLogger.debug("channel = " + channel);
            MessageOperation messageOperation = new MessageOperation();
            messageOperation.setChannelInfo(channel);
            messageOperation.setMessage(message);
            mMessageOperator.add(messageOperation);
        });
    }

    /**
     * Number of ready message count
     */
    public int getReadyMessageCount() {
        return mMessageOperator.getReadyMessageCount();
    }

    /**
     * Number of sent message count
     */
    public long getSentMessageCount() {
        return mMessageOperator.getSentMessageCount();
    }
}
