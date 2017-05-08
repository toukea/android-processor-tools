package com.istat.freedev.processor.utils;

import com.istat.freedev.processor.HttpProcess;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import istat.android.network.http.AsyncHttp;

/**
 * Created by istat on 08/05/17.
 */

public class DefaultHttpProcess<Result, Error extends Throwable> extends HttpProcess<Result, Error> {
    @Override
    protected AsyncHttp onCreateAsyncHttp(String method, String url, HashMap<String, ?> params, HashMap<String, String> headers, Object... otherVars) {
        if (asyncHttpCreator != null) {
            asyncHttpCreator.onCreateAsyncHttp(method, url, params, headers, otherVars);
        }
        return null;
    }

    @Override
    protected Result onBuildErrorResponseBody(HttpURLConnection httpURLConnection, InputStream inputStream) {
        return null;
    }

    @Override
    protected Result onBuildSuccessResponseBody(HttpURLConnection httpURLConnection, InputStream inputStream) {
        return null;
    }

    AsyncHttpCreator asyncHttpCreator;
    ResponseCreator<Result, Error> responseCreator;

    public void setResponseCreator(ResponseCreator<Result, Error> responseCreator) {
        this.responseCreator = responseCreator;
    }

    public void setAsyncHttpCreator(AsyncHttpCreator asyncHttpCreator) {
        this.asyncHttpCreator = asyncHttpCreator;
    }

    public interface AsyncHttpCreator {
        AsyncHttp onCreateAsyncHttp(String method, String url, HashMap<String, ?> params, HashMap<String, String> headers, Object... otherVars);
    }

    public interface ResponseCreator<Result, Error> {
        Result onCreateResult(HttpURLConnection httpURLConnection, InputStream inputStream);

        Error onCreateError(HttpURLConnection httpURLConnection, InputStream inputStream);
    }
}
