package org.code13k.perri.model.config.channel;

public class SlackInfo extends ChannelInfo {
    private String webhookUrl;

    @Override
    public boolean equals(Object object) {
        if (super.equals(object) == true) {
            if (object instanceof SlackInfo) {
                if (this.getWebhookUrl().equals(((SlackInfo) object).getWebhookUrl())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
}
