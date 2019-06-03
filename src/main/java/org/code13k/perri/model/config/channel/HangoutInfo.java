package org.code13k.perri.model.config.channel;

public class HangoutInfo extends ChannelInfo {
    private String incomingWebhookUrl;

    @Override
    public boolean equals(Object object) {
        if (super.equals(object) == true) {
            if (object instanceof HangoutInfo) {
                if (this.getIncomingWebhookUrl().equals(((HangoutInfo) object).getIncomingWebhookUrl())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getIncomingWebhookUrl() {
        return incomingWebhookUrl;
    }

    public void setIncomingWebhookUrl(String incomingWebhookUrl) {
        this.incomingWebhookUrl = incomingWebhookUrl;
    }
}

