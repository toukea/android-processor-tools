package com.istat.freedev.processor;


import android.util.Log;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQueryError;
import istat.android.network.http.interfaces.DownloadHandler;
import istat.android.network.http.interfaces.ProgressionListener;

/**
 * Created by istat on 03/11/16.
 */

public abstract class HttpProcess<Result, Error extends Throwable> extends Process<Result, Error> implements HttpAsyncQuery.HttpQueryCallback, DownloadHandler {

    HttpAsyncQuery asyncQ;
    ProgressionListener<Integer> downloadProgressListener;

    @Override
    protected final void onExecute(Object... vars) {
        String method = vars[0] + "";
        String url = vars[1] + "";
        HashMap<String, String> params = new HashMap<>();
        HashMap<String, String> headers = new HashMap<>();
        int methodInt = HttpAsyncQuery.TYPE_GET;
        if (method.matches("\\d+")) {
            methodInt = Integer.parseInt(method);
        } else {
            method = method.toUpperCase();
            switch ((method)) {
                case "GET":
                    methodInt = HttpAsyncQuery.TYPE_GET;
                    break;
                case "POST":
                    methodInt = HttpAsyncQuery.TYPE_POST;
                    break;
                case "PUT":
                    methodInt = HttpAsyncQuery.TYPE_PUT;
                    break;
                case "HEAD":
                    methodInt = HttpAsyncQuery.TYPE_HEAD;
                    break;
                case "PATCH":
                    methodInt = HttpAsyncQuery.TYPE_PATCH;
                    break;
                case "DELETE":
                    methodInt = HttpAsyncQuery.TYPE_DELETE;
                    break;
            }
        }
        AsyncHttp asyncHttp = onCreateAsyncHttp(params, headers, vars);
        asyncHttp.useDownloader(this, this.downloadProgressListener);
        asyncQ = asyncHttp.doQuery(methodInt, this, url);
    }


    @Override
    protected final void onResume() {
        if (asyncQ != null) {
            asyncQ.resume();
        }
    }

    @Override
    protected final void onPaused() {
        if (asyncQ != null)
            asyncQ.pause();
    }

    @Override
    protected final void onStopped() {
        if (asyncQ != null)
            asyncQ.cancel();
    }

    @Override
    protected final void onCancel() {
        if (asyncQ != null)
            asyncQ.cancel();
    }

    @Override
    public boolean isRunning() {
        if (asyncQ != null)
            return asyncQ.isRunning();
        return false;
    }

    @Override
    public boolean isCompleted() {
        if (asyncQ != null)
            return asyncQ.isCompleted();
        return false;
    }

    @Override
    public boolean isPaused() {
        if (asyncQ != null)
            return asyncQ.isPaused();
        return false;
    }

    @Override
    public void onHttComplete(HttpAsyncQuery.HttpQueryResponse result) {
        Log.d("HttpProcess", "was completed::CODE::" + result.getCode());
    }

    @Override
    public void onHttpSuccess(HttpAsyncQuery.HttpQueryResponse result) {
        Log.d("HttpProcess", "was Succeed::result=" + result.getBodyAsString());
        Result resultP = result.optBody();
        notifyProcessSuccess(resultP);
    }

    @Override
    public void onHttpError(HttpAsyncQuery.HttpQueryResponse result, HttpQueryError e) {
        Log.d("HttpProcess", "was Error::result=" + result.getBodyAsString());
        Error error = result.optBody();
        notifyProcessError(error);
    }

    @Override
    public void onHttpFail(Exception e) {
        Log.d("HttpProcess", "was Failed::exception" + e);
        notifyProcessFailed(e);
    }


    @Override
    public void onHttpAborted() {
        Log.d("HttpProcess", "was Aborted::");
        notifyProcessAborted();
    }

    public void setDownloadProgressListener(ProgressionListener<Integer> downloadProgressListener) {
        this.downloadProgressListener = downloadProgressListener;
    }

    protected abstract AsyncHttp onCreateAsyncHttp(HashMap<String, ?> params, HashMap<String, String> headers, Object... otherVars);

    @Override
    public final Object onBuildResponseBody(HttpURLConnection httpURLConnection, InputStream inputStream) throws Exception {
        if (HttpAsyncQuery.HttpQueryResponse.isSuccessCode(httpURLConnection.getResponseCode())) {
            return onBuildSuccessResponseBody(httpURLConnection, inputStream);
        } else {
            return onBuildErrorResponseBody(httpURLConnection, inputStream);
        }
    }

    protected abstract Error onBuildErrorResponseBody(HttpURLConnection httpURLConnection, InputStream inputStream);

    protected abstract Result onBuildSuccessResponseBody(HttpURLConnection httpURLConnection, InputStream inputStream);
}
