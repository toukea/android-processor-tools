package com.istat.freedev.processor;


import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQuery;
import istat.android.network.http.HttpQueryError;
import istat.android.network.http.interfaces.DownloadHandler;
import istat.android.network.http.interfaces.UpLoadHandler;
import istat.android.network.utils.ToolKits;

/**
 * Created by istat on 03/11/16.
 */

public class HttpProcess<Result, Error extends Throwable> extends Process<Result, Error> implements HttpAsyncQuery.HttpQueryCallback, DownloadHandler {
    HttpAsyncQuery asyncQ;
    AsyncHttp asyncHttp;


    public final boolean execute(ProcessManager processManager, String url, HttpQuery http) {
        try {
            processManager.execute(this, http, url);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected final void onExecute(Object... vars) {
        String url = null;
        HttpQuery<?> http = null;
        if (vars != null || vars.length > 0) {
            for (int i = 0; i < 4; i++) {
                Object var = vars[i];
                if (var != null) {
                    if (var instanceof HttpQuery) {
                        http = (HttpQuery<?>) var;
                    } else if (var instanceof String) {
                        url = var.toString();
                    }
                }
                if (i >= vars.length - 1) {
                    break;
                }
            }
        }
        this.asyncQ = onProceed(url, http);
        this.asyncQ.setDownloadHandler(this);
        onPostProceed(url, http, executionVariables);
    }


    protected HttpAsyncQuery onProceed(String url, HttpQuery<?> http) {
        asyncQ = AsyncHttp.from(http).doGet(this, url);
        return asyncQ;
    }

    protected void onPostProceed(String url, HttpQuery<?> http, Object[] executionVariables) {
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

    @Override
    public Object onBuildResponseBody(HttpURLConnection httpURLConnection, InputStream inputStream) throws Exception {
        try {
            String incomingData = ToolKits.Stream.streamToString(inputStream);
            return new JSONObject(incomingData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    DownloadHandler downloadHandler = new DownloadHandler() {
        @Override
        public Object onBuildResponseBody(HttpURLConnection httpURLConnection, InputStream inputStream) throws Exception {
            try {
                String incomingData = ToolKits.Stream.streamToString(inputStream);
                return new JSONObject(incomingData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
    UpLoadHandler upLoadHandler;

    public static HttpProcess from(AsyncHttp http) {
//        HttpProcess process
        return null;
    }
}
