package com.ecocovoit.ecocovoit.communication;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ecocovoit.ecocovoit.communication.request.Request;
import com.ecocovoit.ecocovoit.communication.response.fail.ResponseFail;
import com.ecocovoit.ecocovoit.communication.response.fail.Error;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.NetworkException;
import org.chromium.net.UploadDataProvider;
import org.chromium.net.UploadDataSink;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**One request at a time. So wait until the first request has its response before
 * committing another one.*/
public class ComHandler {

    /**Le context*/
    private Context context;

    /**The server url*/
    private String url;
    /**The response listener. Contains functions which are called
     * on request fail and request success.*/
    private OnResponseListener responseListener;

    /**The request.*/
    private Request currentRequest;

    /**The callback to be give to Cronet the receive data and errors.*/
    private CronetRequestCallBack callBack;

    /**A boolean which is true if the caller want
     * its response callbacks functions to be called in the main thread.*/
    private boolean onMainThread;
    /**The handler which will run code on the main thread.*/
    private Handler handler;
    /**The code for the handler to execute the onSuccess callback function.*/
    private static final int HANDLER_CODE_SUCCESS = 0;
    /**The code for the handler to execute the onFail callback function.*/
    private static final int HANDLER_CODE_FAIL = 1;

    public static final String TAG = "Com Handler";

    public ComHandler(Context context, String url) {
        this.context = context;
        this.url = url;

        // Init the Cronet callbacks
        this.callBack = new CronetRequestCallBack();
    }

    /**Init the handler for the main thread.*/
    private void initHandlerForMainLoop(final OnResponseListener responseListener) {
        this.handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                /*Code executed on main thread.*/
                if(responseListener != null) {
                    if(msg.what == HANDLER_CODE_SUCCESS) {
                        String response = (String) msg.obj;
                        boolean callbackRes = responseListener.onResponseSuccess(response);
                        handleCallbackResponse(callbackRes);
                    }
                    else if(msg.what == HANDLER_CODE_FAIL) {
                        ResponseFail response = (ResponseFail) msg.obj;
                        responseListener.onResponseFail(response);
                    }
                    else {
                        Log.e(TAG, "handleMessage: ", new RuntimeException("thread handler code inconnu"));
                    }
                }
                super.handleMessage(msg);
            }
        };
    }

    /**Send the Request to the server using Cronet.*/
    protected void send(Request req, boolean onMainThread, OnResponseListener responseListener) {
        this.responseListener = responseListener;
        this.currentRequest = req;
        this.onMainThread = onMainThread;

        // Init the handler if it's needed.
        if(onMainThread) {
            initHandlerForMainLoop(responseListener);
        }

        // Create a CronetEngine.
        CronetEngine.Builder cronetBuilder = new CronetEngine.Builder(this.context);
        cronetBuilder.enableQuic(true);
        CronetEngine cronetEngine = cronetBuilder.build();

        // Create the thread to execute and follow the Cronet request.
        Executor executor = Executors.newSingleThreadExecutor();

        // Build the Cronet request.
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                this.url,
                this.callBack,
                executor
        );
        UrlRequest request = requestBuilder
                .setUploadDataProvider(
                        new Uploader(req.getBody()),
                        executor
                )
                .addHeader("Content-Type", "text/plain")
                .build();

        cronetEngine.createURLStreamHandlerFactory();

        // Issue the request.
        request.start();
    }

    public Context getContext() {
        return context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isOnMainThread() {
        return onMainThread;
    }

    public interface OnResponseListener {
        /**Return true if the server response is well formated.
         * false if not.*/
        boolean onResponseSuccess(String response);
        void onResponseFail(ResponseFail response);
    }

    /**Data uploader.*/
    private static class Uploader extends UploadDataProvider {

        byte[] toSend;

        Uploader(String toSend) {
            this.toSend = toSend.getBytes();
        }

        @Override
        public long getLength() {
            return toSend.length;
        }

        @Override
        public void read(UploadDataSink uploadDataSink, ByteBuffer byteBuffer) {
            byteBuffer.put(toSend);
            uploadDataSink.onReadSucceeded(false);
        }

        @Override
        public void rewind(UploadDataSink uploadDataSink) {
            uploadDataSink.onRewindSucceeded();
        }
    }

    /**Get response fail out of a network exception.*/
    private ResponseFail getResponseFailForNetWorkError(NetworkException e) {
        Error error;
        String message;
        int c = e.getErrorCode();
        if(c == NetworkException.ERROR_INTERNET_DISCONNECTED) {
            error = Error.NOT_CONNECTED;
        }
        else if(c == NetworkException.ERROR_ADDRESS_UNREACHABLE
                || c == NetworkException.ERROR_HOSTNAME_NOT_RESOLVED) {
            error = Error.BAD_ADDRESS;
        }
        else if(
                c == NetworkException.ERROR_CONNECTION_CLOSED
                || c == NetworkException.ERROR_CONNECTION_REFUSED
                || c == NetworkException.ERROR_CONNECTION_RESET
        ) {
            error = Error.CONNEXION_FAIL;
        }
        else {
            error = Error.UNKNOWN_NETWORK_ERROR;
        }

        message = Error.getMessageForError(error, this.context);
        return new ResponseFail(error, message);
    }

    /**Get response fail from server response.*/
    private ResponseFail getResponseFailForServerError(UrlResponseInfo info) {
        int code = info.getHttpStatusCode();
        String presisionMsg = info.getHttpStatusText();
        Error error;

        if(code == Error.BAD_REQUEST.v) {
            error = Error.BAD_REQUEST;
        }
        else if(code == Error.UNAUTHORIZED.v) {
            error = Error.UNAUTHORIZED;
        }
        else if(code == Error.FORBIDDEN.v) {
            error = Error.FORBIDDEN;
        }
        else if(code == Error.NOT_FOUND.v) {
            error = Error.NOT_FOUND;
        }
        else if(code == Error.INTERNAL_SERVER_ERROR.v) {
            error = Error.INTERNAL_SERVER_ERROR;
        }
        else if(code == Error.SERVER_UNAVAILLABLE.v) {
            error = Error.SERVER_UNAVAILLABLE;
        }
        else {
            error = Error.UNKNOWN_SERVER_ERROR;
        }

        String message = Error.getMessageForError(error, this.context);
        return new ResponseFail(error, message, presisionMsg);
    }

    /**Send feedback to server.*/
    private void sendFeedBack(final String feedBack) {
        Request request = new Request() {
            @Override
            public String getBody() {
                return feedBack;
            }
        };
        this.send(request, false, new OnResponseListener() {
            @Override
            public boolean onResponseSuccess(String response) { return true; }
            @Override
            public void onResponseFail(ResponseFail response) { }
        });
    }

    /**This function process the return of the success response callback.
     * It just send a feedback to the server.
     * @param correct indicate that the server response isn't formated as expected.*/
    private void handleCallbackResponse(boolean correct) {
        if(!correct) {
            String req = currentRequest.getBody();
            sendFeedBack("Bad format for response to request : "+req);
        }
    }

    /**The Cronet callback functions.*/
    private class CronetRequestCallBack extends UrlRequest.Callback {

        private static final int BUFFER_SIZE = 128;

        private String body = "";

        @Override
        public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
            // Follow redirection.
            request.followRedirect();
        }

        @Override
        public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
            // Read data from request
            request.read(ByteBuffer.allocateDirect(BUFFER_SIZE));
        }

        @Override
        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
            collectReads(byteBuffer);
            request.read(ByteBuffer.allocateDirect(BUFFER_SIZE));
        }

        @Override
        public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            body = body.trim();

            if(onMainThread) {
                handler.obtainMessage(HANDLER_CODE_SUCCESS, body).sendToTarget();
            }
            else {
                boolean callbackRes = responseListener.onResponseSuccess(body);
                handleCallbackResponse(callbackRes);
            }
        }

        @Override
        public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
            ResponseFail response;
            if(error instanceof NetworkException) {
                NetworkException e = (NetworkException) error;
                response = ComHandler.this.getResponseFailForNetWorkError(e);
            }
            else {
                response = getResponseFailForServerError(info);
            }

            if(onMainThread) {
                handler.obtainMessage(HANDLER_CODE_FAIL, response).sendToTarget();
            }
            else {
                responseListener.onResponseFail(response);
            }
        }

        private void collectReads(ByteBuffer byteBuffer) {
            String chunk = new String(byteBuffer.array(), StandardCharsets.UTF_8);
            body += chunk;
        }
    }
}
