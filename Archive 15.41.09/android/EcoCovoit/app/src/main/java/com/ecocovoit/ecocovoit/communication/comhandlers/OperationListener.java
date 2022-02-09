package com.ecocovoit.ecocovoit.communication.comhandlers;

public interface OperationListener {
    void onSuccess();

    /**Other operation errors.*/
    void onOtherError(String message);

    /**Network or server errors (Request fail).*/
    void onFail(String message);

    /**The server response wasn't formated as expected.*/
    void onBadResponseFromServer();
}
