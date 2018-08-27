package org.code13k.perri.model.config.channel;

public class SlackInfo extends ChannelInfo {
    private String incomingWebhookUrl;

    @Override
    public boolean equals(Object object) {
        if (super.equals(object) == true) {
            if (object instanceof SlackInfo) {
                if (this.getIncomingWebhookUrl().equals(((SlackInfo) object).getIncomingWebhookUrl())) {
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
