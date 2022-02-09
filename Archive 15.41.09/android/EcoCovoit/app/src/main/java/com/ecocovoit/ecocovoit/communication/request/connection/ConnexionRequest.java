package com.ecocovoit.ecocovoit.communication.request.connection;

import com.ecocovoit.ecocovoit.communication.request.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnexionRequest extends Request {

    private String login;
    private static final String REQUEST_JSON_KEY_LOGIN = "login";

    private String password;
    public static final String REQUEST_JSON_KEY_PASSWORD = "password";

    private String body;

    public ConnexionRequest(String login, String password) throws JSONException {
        this.login = login;
        this.password = password;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(REQUEST_JSON_KEY_LOGIN, this.login);
        jsonObject.put(REQUEST_JSON_KEY_PASSWORD, this.password);

        this.body = jsonObject.toString();
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String getBody() {
        return this.body;
    }
}
