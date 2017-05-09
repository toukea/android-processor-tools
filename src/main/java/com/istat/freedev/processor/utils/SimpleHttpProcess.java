package com.istat.freedev.processor.utils;

import com.istat.freedev.processor.HttpProcess;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import istat.android.network.http.AsyncHttp;
import istat.android.network.http.BodyPartHttpQuery;
import istat.android.network.http.HttpQuery;
import istat.android.network.http.SimpleHttpQuery;
import istat.android.network.http.interfaces.DownloadHandler;

/**
 * Created by istat on 08/05/17.
 */

public class SimpleHttpProcess<Result, Error extends Throwable> extends HttpProcess<Result, Error> {

    public SimpleHttpProcess() {

    }

    public SimpleHttpProcess(AsyncHttpCreator creator) {
        setAsyncHttpCreator(creator);
    }

    @Override
    protected AsyncHttp onCreateAsyncHttp(HashMap<String, String> headers, HashMap<String, ?> params, Object... otherVars) {
        if (asyncHttpCreator != null) {
            asyncHttpCreator.onCreateAsyncHttp(params, headers, otherVars);
        }
        return null;
    }

    @Override
    protected final Error onHandleError(HttpURLConnection httpURLConnection, InputStream inputStream) throws Exception {
        if (errorDownloader != null) {
            return errorDownloader.onBuildResponseBody(httpURLConnection, inputStream);
        }
        return null;
    }

    @Override
    protected final Result onHandleResult(HttpURLConnection httpURLConnection, InputStream inputStream) throws Exception {
        if (errorDownloader != null) {
            return successDownloader.onBuildResponseBody(httpURLConnection, inputStream);
        }
        return null;
    }

    AsyncHttpCreator asyncHttpCreator = DEFAULT_ASYNC_HTTP_CREATOR;
    DownloadHandler<Result> successDownloader;
    DownloadHandler<Error> errorDownloader;

    public void setErrorDownloader(DownloadHandler<Error> errorDownloader) {
        this.errorDownloader = errorDownloader;
    }

    public void setSuccessDownloader(DownloadHandler<Result> successDownloader) {
        this.successDownloader = successDownloader;
    }

    public void setAsyncHttpCreator(AsyncHttpCreator asyncHttpCreator) {
        if (asyncHttpCreator == null) {
            asyncHttpCreator = DEFAULT_ASYNC_HTTP_CREATOR;
        }
        this.asyncHttpCreator = asyncHttpCreator;
    }

    public interface AsyncHttpCreator {
        AsyncHttp onCreateAsyncHttp(HashMap<String, ?> params, HashMap<String, String> headers, Object... otherVars);
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
