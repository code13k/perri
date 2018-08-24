package org.code13k.perri.model.config.channel;

public class WebhookInfo extends ChannelInfo {
    private String webhookUrl;

    @Override
    public boolean equals(Object object) {
        if (super.equals(object) == true) {
            if (object instanceof WebhookInfo) {
                if (this.getWebhookUrl().equals(((WebhookInfo) object).getWebhookUrl())) {
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
