package com.ecocovoit.ecocovoit.communication.response.success.connexion;

import com.ecocovoit.ecocovoit.communication.response.success.ResponseSuccess;

import org.json.JSONException;

public class DeconnexionResponse extends ResponseSuccess {

    private int token;
    public static final String RESPONSE_JSON_KEY_TOKEN = "token";

    public DeconnexionResponse(String body) throws JSONException {
        super(body);

        this.token = getJSONObject().getInt(RESPONSE_JSON_KEY_TOKEN);
    }

    public int getToken() {
        return token;
    }
}
