package org.code13k.perri.model.config.channel;

public class DiscordInfo extends ChannelInfo {
    private String incomingWebhookUrl;

    @Override
    public boolean equals(Object object) {
        if (super.equals(object) == true) {
            if (object instanceof DiscordInfo) {
                if (this.getIncomingWebhookUrl().equals(((DiscordInfo) object).getIncomingWebhookUrl())) {
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
