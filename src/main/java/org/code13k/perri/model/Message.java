package org.code13k.perri.model;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;

public class Message extends BasicModel {
    private String channel;
    private String text;
    private String textMD5;
    private ArrayList<String> tags;

    @Override
    public boolean equals(Object object) {
        if (object instanceof Message) {
            if (this.getTextMD5().equals(((Message) object).getTextMD5())) {
                if (this.getTags().equals(((Message) object).getTags())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.textMD5 = DigestUtils.md5Hex(text);
    }

    public String getTextMD5() {
        return textMD5;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getCommaSeparatedTags(){
        StringBuffer sb = new StringBuffer();
        if (tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                sb.append(tags.get(i));
                if (i < tags.size() - 1) {
                    sb.append(",");
                }
            }
            return sb.toString();
        }
        return "";
    }
}
