package com.istat.freedev.processor.http.async;

import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQueryError;

/**
 * Created by Istat Toukea on 23/08/2017.
 */

public class HttpAuthProcess<Result, Error extends Throwable> extends HttpProcess<Result, Error> {
    AuthFailErrorHandler authFailErrorHandler;

    public HttpAuthProcess() {
        super();
    }

    public HttpAuthProcess(AuthFailErrorHandler authFailErrorHandler) {
        super();
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
                onUnHandledErrorHappen(error);
            }
        } else {
            onUnHandledErrorHappen(error);
        }
    }

    protected void onUnHandledErrorHappen(HttpQueryError error) {
        super.onErrorHappen(error);
    }

    private boolean onHandleError(HttpQueryError e) {
        if (authFailErrorHandler != null) {
            return authFailErrorHandler.onError(e, getAsyncTask());
        }
        return false;
    }

    public String getSubmittedUserName() {
        return getExecutionVariable(3);
    }

    public String getSubmittedPassword() {
        return getExecutionVariable(4);
    }

    public interface AuthFailErrorHandler {
        boolean onError(HttpQueryError e, HttpAsyncQuery http);
    }

    public Authentication getAuthentication() {
        String username = getSubmittedUserName(), password = getSubmittedPassword();
        return new Authentication(username, password);
    }

    public static class Authentication {
        public final String userName;
        public final String password;

        public Authentication(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }
    }
}
