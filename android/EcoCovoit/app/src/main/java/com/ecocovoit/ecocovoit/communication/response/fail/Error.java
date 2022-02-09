package com.ecocovoit.ecocovoit.communication.response.fail;

import android.content.Context;

import com.ecocovoit.ecocovoit.R;

public enum Error {

    /**Erreur inconnue.*/
    UNKNOWN_ERROR(-1),
    /**Pas d'erreur.*/
    NON_ERROR(0),

    // ******************** NETWORK ERROR (1 - 20)
    /**Erreur inconnue sur le reseau.*/
    UNKNOWN_NETWORK_ERROR(1),
    /**Le mobile n'est pas connecte.*/
    NOT_CONNECTED(2),
    /**La connexion au serveur a echouee.*/
    CONNEXION_FAIL(3),
    /**L'addresse est erronee ou introuvable.*/
    BAD_ADDRESS(4),

    // ******************** SERVER ERROR
    /**Erreur inconnue notifiee par le serveur.*/
    UNKNOWN_SERVER_ERROR(21),
    /**Le serveur ne comprend pas la requete.*/
    BAD_REQUEST(400),
    /**Le client n'est pas connecte et il a besoin de l'etre pour
     * effectuer la requete.*/
    UNAUTHORIZED(401),
    /**Le client est connecte mais il n'a pas le droit de faire la requete.*/
    FORBIDDEN(403),
    /**Le serveur n'a pas pu trouver de reponse.*/
    NOT_FOUND(404),
    /**Une erreur interne est survenie du cote serveur.*/
    INTERNAL_SERVER_ERROR(500),
    /**Le serveur n'est pas pret a traiter la requete.*/
    SERVER_UNAVAILLABLE(503)
    ;

    public final int v;
    Error(int v) {
        this.v = v;
    }

    public static String getMessageForError(Error error, Context context) {
        String message;
        if(error == UNKNOWN_ERROR) {
            message = context.getString(R.string.error_unknown_error);
        }
        else if(error == NON_ERROR) {
            message = context.getString(R.string.error_non_error);
        }
        else if(error == UNKNOWN_NETWORK_ERROR) {
            message = context.getString(R.string.error_unknown_network_error);
        }
        else if(error == UNKNOWN_SERVER_ERROR) {
            message = context.getString(R.string.error_unknown_server_error);
        }
        else if(error == NOT_CONNECTED) {
            message = context.getString(R.string.error_not_connected);
        }
        else if(error == CONNEXION_FAIL) {
            message = context.getString(R.string.error_connexion_fail);
        }
        else if(error == BAD_ADDRESS) {
            message = context.getString(R.string.error_bad_address);
        }
        else if(error == BAD_REQUEST) {
            message = context.getString(R.string.error_bad_request);
        }
        else if(error == UNAUTHORIZED) {
            message = context.getString(R.string.error_unauthorized);
        }
        else if(error == FORBIDDEN) {
            message = context.getString(R.string.error_forbiden);
        }
        else if(error == NOT_FOUND) {
            message = context.getString(R.string.error_not_found);
        }
        else if(error == INTERNAL_SERVER_ERROR) {
            message = context.getString(R.string.error_internal_server_error);
        }
        else if(error == SERVER_UNAVAILLABLE) {
            message = context.getString(R.string.error_server_unavaillable);
        }
        else {
            message = context.getString(R.string.error_without_code);
        }

        return message;
    }
}
