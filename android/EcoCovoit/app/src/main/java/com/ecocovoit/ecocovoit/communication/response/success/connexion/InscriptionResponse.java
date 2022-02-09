package com.ecocovoit.ecocovoit.communication.response.success.connexion;

import com.ecocovoit.ecocovoit.communication.response.success.ResponseSuccess;

import org.json.JSONException;

public class InscriptionResponse extends ResponseSuccess {

    public static final String INSCRIPTION_ERROR_LOGIN_EXIST = "login_already_exist";
    public static final String INSCRIPTION_ERROR_BAD_PASSWORD = "password_not_good";
    public static final String INSCRIPTION_ERROR_EMAIL_EXIST = "email_already_exist";

    public InscriptionResponse(String body) throws JSONException {
        super(body);
    }
}
