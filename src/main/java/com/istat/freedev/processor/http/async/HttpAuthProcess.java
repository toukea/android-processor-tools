package com.istat.freedev.processor.http.async;

import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpQueryError;

/**
 * Created by Istat Toukea on 23/08/2017.
 */

public class HttpAuthProcess<Result> extends HttpProcess<Result> {
    AuthFailErrorHandler authFailErrorHandler;

    public HttpAuthProcess(AsyncHttp asyncHttp) {
        super(asyncHttp);
    }

    public HttpAuthProcess(AsyncHttp asyncHttp, int method, String url) {
        super(asyncHttp, method, url);
    }

    public HttpAuthProcess(AsyncHttp asyncHttp, AuthFailErrorHandler authFailErrorHandler, int method, String url) {
        super(asyncHttp, method, url);
        this.authFailErrorHandler = authFailErrorHandler;
    }

    public void setAuthFailErrorHandler(AuthFailErrorHandler authFailErrorHandler) {
        this.authFailErrorHandler = authFailErrorHandler;
    }

    protected final void onErrorHappen(HttpQueryError error) {
        if (error.getCode() == 401 || error.getCode() == 403) {
            if (onHandleError(error)) {
                restart(RESTART_MODE_GEOPARDISE);
            } else {
                notifyProcessError(error);
            }
        } else {
            notifyProcessError(error);
        }
    }

    private boolean onHandleError(HttpQueryError e) {
        if (authFailErrorHandler != null) {
            return authFailErrorHandler.onError(e, this.asyncHttp);
        }
        return false;
    }

    public interface AuthFailErrorHandler {
        boolean onError(HttpQueryError e, AsyncHttp http);
    }
}
