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

public abstract class HttpProcess<Result, Error extends Throwable> extends Process<Result, Error> implements HttpAsyncQuery.HttpQueryCallback {

    HttpAsyncQuery asyncQ;
    ProgressionListener downloadProgressListener;

    @Override
    protected final void onExecute(ExecutionVariables vars) {
        String method = vars.getVariable(0);
        String url = vars.getVariable(1);
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
        AsyncHttp asyncHttp = onCreateAsyncHttp(headers, params, vars);
        asyncHttp.useDownloader(getInternalDownloader(), this.downloadProgressListener);
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

    public void setDownloadProgressListener(ProgressionListener downloadProgressListener) {
        this.downloadProgressListener = downloadProgressListener;
    }

    protected abstract AsyncHttp onCreateAsyncHttp(HashMap<String, String> headers, HashMap<String, ?> params, Object... otherVars);

    protected abstract Error onHandleError(HttpURLConnection httpURLConnection, InputStream inputStream) throws Exception;

    protected abstract Result onHandleResult(HttpURLConnection httpURLConnection, InputStream inputStream) throws Exception;

    public DownloadHandler getInternalDownloader() {
        return new DownloadHandler() {
            @Override
            public Object onBuildResponseBody(HttpURLConnection connexion, InputStream stream) throws Exception {
                if (HttpAsyncQuery.HttpQueryResponse.isSuccessCode(connexion.getResponseCode())) {
                    return onHandleResult(connexion, stream);
                } else {
                    return onHandleError(connexion, stream);
                }
            }
        };
    }


    public final static class Executor {
        String method;
        String url;
        HashMap<String, String> headers;
        HashMap<?, ?> params;
        Object[] otherParams;
        ProcessManager processManager;

        public Executor(ProcessManager processManager) {
            this.processManager = processManager;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public HashMap<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(HashMap<String, String> headers) {
            this.headers = headers;
        }

        public HashMap<?, ?> getParams() {
            return params;
        }

        public void setParams(HashMap<?, ?> params) {
            this.params = params;
        }

        public Object[] getOtherParams() {
            return otherParams;
        }

        public void setOtherParams(Object[] otherParams) {
            this.otherParams = otherParams;
        }

        public ProcessManager getProcessManager() {
            return processManager;
        }

        public void setProcessManager(ProcessManager processManager) {
            this.processManager = processManager;
        }

        //-------------------------------------
        public <T extends HttpProcess> T execute(T process) {
            return execute(getProcessManager(), process, method, url, headers, params, otherParams);
        }

        public <T extends HttpProcess> T execute(String PID, T process) throws ProcessManager.ProcessException {
            return getProcessManager().execute(PID, process, method, url, params, headers, params, otherParams);
        }
        //----------------------------------------------

        public static <T extends HttpProcess> T execute(ProcessManager pm, T process, String method, String url, Object... otherParams) {
            return execute(pm, process, method, url, null, null, otherParams);
        }

        public static <T extends HttpProcess> T execute(ProcessManager pm, T process, String method, String url, HashMap<String, String> headers, Object... otherParams) {
            return execute(pm, process, method, url, headers, null, otherParams);
        }

        public static <T extends HttpProcess> T execute(ProcessManager pm, T process, String
                method, String url, HashMap<String, String> headers, HashMap<?, ?> params, Object...
                                                                otherParams) {
            return pm.execute(process, method, url, params, headers, otherParams);
        }

        public static <T extends HttpProcess> T execute(ProcessManager pm, String PID, T process, String method, String url, HashMap<String, String> headers, HashMap<?, ?> params, Object... otherParams) throws ProcessManager.ProcessException {
            return pm.execute(PID, process, method, url, params, headers, otherParams);
        }

        public static <T extends HttpProcess> T execute(ProcessManager pm, String PID, T process, String method, String url, HashMap<?, ?> params) throws ProcessManager.ProcessException {
            return pm.execute(PID, process, method, url, params, null, new Object[0]);
        }
    }
}
