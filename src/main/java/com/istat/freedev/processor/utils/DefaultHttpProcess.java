package com.istat.freedev.processor.utils;

import com.istat.freedev.processor.HttpProcess;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import istat.android.network.http.AsyncHttp;
import istat.android.network.http.BodyPartHttpQuery;
import istat.android.network.http.HttpQuery;
import istat.android.network.http.SimpleHttpQuery;

/**
 * Created by istat on 08/05/17.
 */

public class DefaultHttpProcess<Result, Error extends Throwable> extends HttpProcess<Result, Error> {
    @Override
    protected AsyncHttp onCreateAsyncHttp(HashMap<String, ?> params, HashMap<String, String> headers, Object... otherVars) {
        if (asyncHttpCreator != null) {
            asyncHttpCreator.onCreateAsyncHttp(params, headers, otherVars);
        }
        return null;
    }

    @Override
    protected final Error onBuildErrorResponseBody(HttpURLConnection httpURLConnection, InputStream inputStream) {
        if (errorBodyCreator != null) {
            return errorBodyCreator.onCreateAsyncHttp(httpURLConnection, inputStream);
        }
        return null;
    }

    @Override
    protected final Result onBuildSuccessResponseBody(HttpURLConnection httpURLConnection, InputStream inputStream) {
        if (errorBodyCreator != null) {
            return successBodyCreator.onCreateAsyncHttp(httpURLConnection, inputStream);
        }
        return null;
    }

    AsyncHttpCreator asyncHttpCreator = DEFAULT_ASYNC_HTTP_CREATOR;
    BodyCreator<Result> successBodyCreator;
    BodyCreator<Error> errorBodyCreator;

    public void setErrorBodyCreator(BodyCreator<Error> errorBodyCreator) {
        this.errorBodyCreator = errorBodyCreator;
    }

    public void setSuccessBodyCreator(BodyCreator<Result> successBodyCreator) {
        this.successBodyCreator = successBodyCreator;
    }

    public void setAsyncHttpCreator(AsyncHttpCreator asyncHttpCreator) {
        this.asyncHttpCreator = asyncHttpCreator;
    }

    public interface AsyncHttpCreator {
        AsyncHttp onCreateAsyncHttp(HashMap<String, ?> params, HashMap<String, String> headers, Object... otherVars);
    }

    public interface BodyCreator<T> {
        T onCreateAsyncHttp(HttpURLConnection httpURLConnection, InputStream inputStream);
    }

    public final static AsyncHttpCreator DEFAULT_ASYNC_HTTP_CREATOR = new AsyncHttpCreator() {

        @Override
        public AsyncHttp onCreateAsyncHttp(HashMap<String, ?> params, HashMap<String, String> headers, Object... otherVars) {
            return AsyncHttp.fromSimpleHttp()
                    .addHttpHeaders(headers)
                    .addHttpParams(params);
        }
    };
    public final static AsyncHttpCreator MULTIPART_ASYNC_HTTP_CREATOR = new AsyncHttpCreator() {

        @Override
        public AsyncHttp onCreateAsyncHttp(HashMap<String, ?> params, HashMap<String, String> headers, Object... otherVars) {
            return AsyncHttp.fromMultipartHttp()
                    .addHttpHeaders(headers)
                    .addHttpParams(params);
        }
    };
    public final static AsyncHttpCreator BODYPART_ASYNC_HTTP_CREATOR = new AsyncHttpCreator() {

        @Override
        public AsyncHttp onCreateAsyncHttp(HashMap<String, ?> params, HashMap<String, String> headers, Object... otherVars) {
            HttpQuery http;
            if (otherVars != null && otherVars.length > 0) {
                http = new BodyPartHttpQuery(otherVars[0]);
            } else if (params != null && !params.isEmpty()) {
                http = new BodyPartHttpQuery(params);
            } else {
                http = new SimpleHttpQuery();
            }
            return AsyncHttp.from(http)
                    .addHttpHeaders(headers)
                    .addHttpParams(params);
        }
    };

    public static class Builder {

    }
}
