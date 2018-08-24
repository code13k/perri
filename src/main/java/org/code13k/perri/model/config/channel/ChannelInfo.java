package org.code13k.perri.model.config.channel;

import org.code13k.perri.model.BasicModel;

public class ChannelInfo extends BasicModel {
    private String type;
    private boolean displayTags;
    private boolean mergeDuplicateMessage;

    @Override
    public boolean equals(Object object) {
        if (object instanceof ChannelInfo) {
            if (this.getType().equals(((ChannelInfo) object).getType())) {
                if (this.isDisplayTags() == ((ChannelInfo) object).isDisplayTags()) {
                    if (this.isMergeDuplicateMessage() == ((ChannelInfo) object).isMergeDuplicateMessage()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDisplayTags() {
        return displayTags;
    }

    public void setDisplayTags(boolean displayTags) {
        this.displayTags = displayTags;
    }

    public boolean isMergeDuplicateMessage() {
        return mergeDuplicateMessage;
    }

    public void setMergeDuplicateMessage(boolean mergeDuplicateMessage) {
        this.mergeDuplicateMessage = mergeDuplicateMessage;
    }
}
