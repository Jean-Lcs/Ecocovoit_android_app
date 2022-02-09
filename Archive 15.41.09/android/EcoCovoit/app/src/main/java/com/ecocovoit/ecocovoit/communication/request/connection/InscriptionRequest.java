package com.ecocovoit.ecocovoit.communication.request.connection;

import com.ecocovoit.ecocovoit.communication.request.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class InscriptionRequest extends Request {

    private String login;
    public static final String REQUEST_JSON_KEY_LOGIN = "login";
    private String password;
    public static final String REQUEST_JSON_KEY_PASSWORD = "password";
    private String email;
    public static final String REQUEST_JSON_KEY_EMAIL = "email";

    private String body;

    public InscriptionRequest(String login, String password, String email) throws JSONException {
        this.login = login;
        this.password = password;
        this.email = email;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(REQUEST_JSON_KEY_LOGIN, this.login);
        jsonObject.put(REQUEST_JSON_KEY_EMAIL, this.email);
        jsonObject.put(REQUEST_JSON_KEY_PASSWORD, this.password);

        this.body = jsonObject.toString();
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getBody() {
        return this.body;
    }

}
