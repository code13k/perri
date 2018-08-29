package org.code13k.perri.model;

import org.code13k.perri.model.config.channel.ChannelInfo;

public class MessageOperation extends BasicModel {
    private ChannelInfo channelInfo;
    private Message message;
    private int messageCount = 1;
    private int retryCount = 0;

    @Override
    public boolean equals(Object object) {
        if (object instanceof MessageOperation) {
            if (this.getMessage().equals(((MessageOperation) object).getMessage())) {
                if (this.getChannelInfo().equals(((MessageOperation) object).getChannelInfo())) {
                    return true;
                }
            }
        }
        return false;
    }

    public ChannelInfo getChannelInfo() {
        return channelInfo;
    }

    public void setChannelInfo(ChannelInfo channelInfo) {
        this.channelInfo = channelInfo;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void increaseMessageCount() {
        this.messageCount++;
    }

    public int getRetryCount(){
        return retryCount;
    }

    public void increaseRetryCount(){
        this.retryCount++;
    }
}
