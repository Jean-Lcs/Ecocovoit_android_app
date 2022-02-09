package com.ecocovoit.ecocovoit.communication.response.success;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseSuccess{

    private String body;

    private boolean completed;

    private JSONObject jsonObject;

    private String error;
    public static final String RESPONSE_JSON_KEY_ERROR = "error";

    public ResponseSuccess(String body) throws JSONException {
        this.body = body;

        jsonObject = new JSONObject(body);
        if(jsonObject.has(RESPONSE_JSON_KEY_ERROR)) {
            this.error = jsonObject.getString(RESPONSE_JSON_KEY_ERROR);
            this.completed = false;
        }
        else {
            this.error = "";
            this.completed = true;
        }
    }

    public String getBody() {
        return this.body;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public String getError() {
        return error;
    }

    protected JSONObject getJSONObject() {
        return this.jsonObject;
    }
}
