package com.ecocovoit.ecocovoit.communication.request;

public abstract class Request {
    public abstract String getBody();

    /*TODO : Comment diferencier les requetes au niveau du serveur. Il y a deux options :
    * 1) utiliser une url specifique pour chaque requete.
    * 2) tout envoyer sur une unique url et ajouter un champ json qui identifi la requete.*/
}
