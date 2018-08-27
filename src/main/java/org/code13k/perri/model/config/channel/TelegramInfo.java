package org.code13k.perri.model.config.channel;

public class TelegramInfo extends ChannelInfo {
    private String botApiToken;
    private String chatId;

    @Override
    public boolean equals(Object object) {
        if (super.equals(object) == true) {
            if (object instanceof TelegramInfo) {
                if (this.getBotApiToken().equals(((TelegramInfo) object).getBotApiToken())) {
                    if (this.getChatId().equals(((TelegramInfo) object).getChatId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getBotApiToken() {
        return botApiToken;
    }

    public void setBotApiToken(String botApiToken) {
        this.botApiToken = botApiToken;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
