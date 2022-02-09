package com.ecocovoit.ecocovoit.communication.request.connection;

import com.ecocovoit.ecocovoit.communication.request.RequestFromConnectedUser;
import com.ecocovoit.ecocovoit.entities.parties.User;

import org.json.JSONException;
import org.json.JSONObject;

public class DeconnexionRequest extends RequestFromConnectedUser {

    private User user;
    public static final String REQUEST_JSON_KEY_USER = "user";

    public DeconnexionRequest(int token, User user) throws JSONException {
        super(token);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    protected void fillJsonObject(JSONObject jsonObject) throws JSONException {
        jsonObject.put(REQUEST_JSON_KEY_USER, this.user.toJson());
    }
}
