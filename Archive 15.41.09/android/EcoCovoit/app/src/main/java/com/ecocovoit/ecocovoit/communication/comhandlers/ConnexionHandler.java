package com.ecocovoit.ecocovoit.communication.comhandlers;

import android.content.Context;
import android.util.Log;

import com.ecocovoit.ecocovoit.R;
import com.ecocovoit.ecocovoit.communication.ComHandler;
import com.ecocovoit.ecocovoit.communication.request.Request;
import com.ecocovoit.ecocovoit.communication.request.connection.ConnexionRequest;
import com.ecocovoit.ecocovoit.communication.request.connection.DeconnexionRequest;
import com.ecocovoit.ecocovoit.communication.request.connection.DesinscriptionRequest;
import com.ecocovoit.ecocovoit.communication.request.connection.InscriptionRequest;
import com.ecocovoit.ecocovoit.communication.response.fail.ResponseFail;
import com.ecocovoit.ecocovoit.communication.response.success.ResponseSuccess;
import com.ecocovoit.ecocovoit.communication.response.success.connexion.ConnexionResponse;
import com.ecocovoit.ecocovoit.communication.response.success.connexion.DeconnexionResponse;
import com.ecocovoit.ecocovoit.communication.response.success.connexion.InscriptionResponse;
import com.ecocovoit.ecocovoit.entities.parties.User;

import org.json.JSONException;

public class ConnexionHandler extends ComHandler {

    private int token;
    private User user;

    private static final int NO_TOKEN = 0;

    private static final String TAG = "connexion handler";
    private static final String msg = "Ne croit pas au toi en qui je crois, " +
            "ne crois pas au toi en qui tu crois," +
            "mais crois au toi qui crois en toi";

    public ConnexionHandler(Context context, String url) {
        super(context, url);

        this.token = NO_TOKEN;
    }

    public boolean isConnected() {
        return this.token != NO_TOKEN;
    }

    public void connect(String login, String password, boolean onMainThread, final ConnexionOperationListener listener) {
        ConnexionRequest request;
        try {
            request = new ConnexionRequest(login, password);
        } catch (JSONException e) {
            listener.onFail(msg);
            return;
        }

        this.send(request, onMainThread, new OnResponseListener() {
            @Override
            public boolean onResponseSuccess(String response) {
                ConnexionResponse resp = null;
                try {
                    resp = new ConnexionResponse(response);
                } catch (JSONException e) {
                    listener.onBadResponseFromServer();
                    return false;
                }

                if(resp.isCompleted()) {
                    token = resp.getToken();
                    user = resp.getUser();
                    listener.onSuccess();
                }
                else {
                    if(resp.getError().equals(ConnexionResponse.CONNEXION_ERROR_UNKNOWN_LOGIN)) {
                        listener.onUnknownLogin();
                    }
                    else if(resp.getError().equals(ConnexionResponse.CONNEXION_ERROR_WRONG_PASSWORD)) {
                        listener.onWrongPassword();
                    }
                    else {
                        listener.onFail(resp.getError());
                    }
                }
                return true;
            }

            @Override
            public void onResponseFail(ResponseFail response) {
                Log.i(TAG, "Connexion onResponseFail: "+response.getPrecisionMessage());
                listener.onFail(response.getMessage());
            }
        });
    }

    public void disconnect(boolean onMainThread, final OperationListener listener) {
        DeconnexionRequest request;
        try {
            request = new DeconnexionRequest(this.token, this.user);
        } catch (JSONException e) {
            Log.e(TAG, "disconnect: ", e);
            listener.onFail(msg);
            return;
        }

        this.send(request, onMainThread, new OnResponseListener() {
            @Override
            public boolean onResponseSuccess(String response) {
                DeconnexionResponse resp = null;
                try {
                    resp = new DeconnexionResponse(response);
                } catch (JSONException e) {
                    listener.onBadResponseFromServer();
                    return false;
                }

                if(resp.isCompleted()) {
                    token = NO_TOKEN;
                    listener.onSuccess();
                }
                else {
                    String message = getContext().getString(R.string.msg_cannot_disconnect);
                    listener.onOtherError(message);
                }

                return true;
            }

            @Override
            public void onResponseFail(ResponseFail response) {
                Log.i(TAG, "Connexion onResponseFail: "+response.getPrecisionMessage());
                listener.onFail(response.getMessage());
            }
        });
    }

    public void inscription(String login, String password, String email, boolean onMainThread, final InscriptionOperationListener listener) {
        InscriptionRequest request;
        try {
            request = new InscriptionRequest(login, password, email);
        } catch (JSONException e) {
            Log.e(TAG, "inscription: ", e);
            listener.onFail(msg);
            return;
        }

        this.send(request, onMainThread, new OnResponseListener() {
            @Override
            public boolean onResponseSuccess(String response) {
                ConnexionResponse resp = null;
                try {
                    resp = new ConnexionResponse(response);
                } catch (JSONException e) {
                    listener.onBadResponseFromServer();
                    return false;
                }

                if(resp.isCompleted()) {
                    token = resp.getToken();
                    user = resp.getUser();
                    listener.onSuccess();
                }
                else {
                    if(resp.getError().equals(InscriptionResponse.INSCRIPTION_ERROR_BAD_PASSWORD)) {
                        listener.onBadPassword();
                    }
                    else if(resp.getError().equals(InscriptionResponse.INSCRIPTION_ERROR_EMAIL_EXIST)) {
                        listener.onEmailAlreadyExist();
                    }
                    else if(resp.getError().equals(InscriptionResponse.INSCRIPTION_ERROR_LOGIN_EXIST)) {
                        listener.onLoginAlreadyExist();
                    }
                    else {
                        listener.onFail(resp.getError());
                    }
                }

                return true;
            }

            @Override
            public void onResponseFail(ResponseFail response) {
                Log.i(TAG, "Connexion onResponseFail: "+response.getPrecisionMessage());
                listener.onFail(response.getMessage());
            }
        });
    }

    public void desinscription(boolean onMainThread, final OperationListener listener) {
        DesinscriptionRequest request;
        try {
            request = new DesinscriptionRequest(this.token, this.user);
        } catch (JSONException e) {
            Log.e(TAG, "desinscription: ", e);
            listener.onFail(msg);
            return;
        }

        this.send(request, onMainThread, new OnResponseListener() {
            @Override
            public boolean onResponseSuccess(String response) {
                DeconnexionResponse resp = null;
                try {
                    resp = new DeconnexionResponse(response);
                } catch (JSONException e) {
                    listener.onBadResponseFromServer();
                    return false;
                }

                if(resp.isCompleted()) {
                    token = NO_TOKEN;
                    listener.onSuccess();
                }
                else {
                    String message = getContext().getString(R.string.msg_cannot_inscription);
                    listener.onOtherError(message);
                }

                return true;
            }

            @Override
            public void onResponseFail(ResponseFail response) {
                Log.i(TAG, "Connexion onResponseFail: "+response.getPrecisionMessage());
                listener.onFail(response.getMessage());
            }
        });
    }

    public interface ConnexionOperationListener extends OperationListener {
        void onWrongPassword();
        void onUnknownLogin();
    }

    public interface InscriptionOperationListener extends OperationListener {
        void onLoginAlreadyExist();
        void onBadPassword();
        void onEmailAlreadyExist();
    }
}
