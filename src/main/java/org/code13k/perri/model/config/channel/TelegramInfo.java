package org.code13k.perri.model.config.channel;

public class TelegramInfo extends ChannelInfo {
    private String botId;
    private String chatId;

    @Override
    public boolean equals(Object object) {
        if (super.equals(object) == true) {
            if (object instanceof TelegramInfo) {
                if (this.getBotId().equals(((TelegramInfo) object).getBotId())) {
                    if (this.getChatId().equals(((TelegramInfo) object).getChatId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getBotId() {
        return botId;
    }

    public void setBotId(String botId) {
        this.botId = botId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
