package com.ecocovoit.ecocovoit.communication.response.success.connexion;

import com.ecocovoit.ecocovoit.communication.response.success.ResponseSuccess;
import com.ecocovoit.ecocovoit.entities.parties.User;

import org.json.JSONException;

public class ConnexionResponse extends ResponseSuccess {

    private int token;
    public static final String RESPONSE_JSON_KEY_TOKEN = "token";

    private User user;
    public static final String RESPONSE_JSON_KEY_USER = "user";

    public static final String CONNEXION_ERROR_UNKNOWN_LOGIN = "unknown_login";
    public static final String CONNEXION_ERROR_WRONG_PASSWORD = "wrong_password";

    public ConnexionResponse(String body) throws JSONException {
        super(body);

        this.token = getJSONObject().getInt(RESPONSE_JSON_KEY_TOKEN);
        this.user = new User(getJSONObject().getJSONObject(RESPONSE_JSON_KEY_USER));
    }

    public int getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
