package se.harbil.policeslackplugin.model;

import java.util.List;

public class ResponseToSlack {

    private String response_type;
    private String text;
    private List<ResponseAttachment> attachments;

    public ResponseToSlack() {
    }

    public String getResponse_type() {
        return response_type;
    }

    public void setResponse_type(String response_type) {
        this.response_type = response_type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ResponseAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ResponseAttachment> attachments) {
        this.attachments = attachments;
    }

}
