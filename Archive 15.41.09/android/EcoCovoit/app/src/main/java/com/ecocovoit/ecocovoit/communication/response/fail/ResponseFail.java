package com.ecocovoit.ecocovoit.communication.response.fail;

public class ResponseFail {

    private Error type;
    private String message;
    private String precisionMessage;

    public ResponseFail(Error type, String msg, String precisionMessage) {
        this.type = type;
        this.message = msg;
        this.precisionMessage = precisionMessage;
    }

    public ResponseFail(Error type, String msg) {
        this(type, msg, "");
    }

    public Error getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getPrecisionMessage() {
        return precisionMessage;
    }
}
