package com.ecocovoit.ecocovoit.communication.request;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class RequestFromConnectedUser extends Request {

    private int token;
    public static final String REQUEST_JSON_KEY_TOKEN = "token";

    private JSONObject jsonObject;

    public RequestFromConnectedUser(int token) throws JSONException {
        this.token = token;

        this.jsonObject = new JSONObject();
        this.jsonObject.put(REQUEST_JSON_KEY_TOKEN, this.token);

        fillJsonObject(this.jsonObject);
    }

    protected abstract void fillJsonObject(JSONObject jsonObject) throws JSONException;

    @Override
    public String getBody() {
        return this.jsonObject.toString();
    }
}
