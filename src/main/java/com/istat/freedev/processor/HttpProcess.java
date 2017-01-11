package com.istat.freedev.processor;


import android.util.Log;

import com.istat.freedev.processor.Process;
import com.istat.freedev.processor.ProcessManager;
import com.istat.freedev.processor.Processor;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQuery;
import istat.android.network.http.HttpQueryError;
import istat.android.network.http.SimpleHttpQuery;
import istat.android.network.http.interfaces.DownloadHandler;
import istat.android.network.utils.StreamOperationTools;

/**
 * Created by istat on 03/11/16.
 */

public class HttpProcess<Result, Error extends Process.ProcessError> extends Process<Result, Error> implements HttpAsyncQuery.HttpQueryCallback, DownloadHandler {
    HttpAsyncQuery asyncQ;
    String executionURL;

    public String getExecutionURL() {
        return executionURL;
    }


    public final boolean execute(Processor processor, String url) {
        HashMap<?, ?> httpVars = null;
        return execute(processor.getProcessManager(), url);
    }

    public final boolean execute(Processor processor, String url, HashMap<?, ?> httpVars) {
        return execute(processor.getProcessManager(), url, httpVars);
    }

    public final boolean execute(Processor processor, String url, HttpQuery http) {
        return execute(processor.getProcessManager(), url, http);
    }

    public final boolean execute(Processor processor, String url, HttpQuery http, HashMap<?, ?> httpVars) {
        return execute(processor.getProcessManager(), url, http, httpVars);
    }

    public final boolean execute(String url) {
        return execute(Processor.getDefaultProcessManager(), url, onCreateHttpQuery(), null);
    }

    public final boolean execute(String url, HashMap<?, ?> httpVars) {
        return execute(Processor.getDefaultProcessManager(), url, onCreateHttpQuery(), httpVars);
    }

    public final boolean execute(String url, HttpQuery http) {
        return execute(Processor.getDefaultProcessManager(), url, http, null);
    }

    public final boolean execute(ProcessManager processManager, String url) {
        HashMap<?, ?> httpVars = null;
        return execute(processManager, url, httpVars);
    }

    public final boolean execute(ProcessManager processManager, String url, HashMap<?, ?> httpVars) {
        return execute(processManager, url, onCreateHttpQuery(), httpVars);
    }

    public final boolean execute(ProcessManager processManager, String url, HttpQuery http) {
        return execute(processManager, url, http, null);
    }

    public final boolean execute(ProcessManager processManager, String url, HttpQuery http, HashMap<?, ?> httpVars) {
        try {
            processManager.execute(this, http, url, httpVars);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected final void onExecute(Object... vars) {
        String url = null;
        HashMap httpVars = null;
        HttpQuery<?> http = null;
        if (vars != null || vars.length > 0) {
            for (int i = 0; i < 4; i++) {
                Object var = vars[i];
                if (var != null) {
                    if (var instanceof HttpQuery) {
                        http = (HttpQuery<?>) var;
                    } else if (var instanceof HashMap) {
                        httpVars = (HashMap) var;
                    } else if (var instanceof String) {
                        url = var.toString();
                    }
                }
                if (i >= vars.length - 1) {
                    break;
                }
            }
        }
        if (http == null) {
            http = onCreateHttpQuery();
        }
        onPreProceed(url, http, httpVars, vars);
        this.asyncQ = onProceed(url, http);
        this.asyncQ.setDownloadHandler(this);
        onPostProceed(url, http, executionVariables);
    }


    protected void onPreProceed(String url, HttpQuery<?> http, HashMap<?, ?> httpVars, Object... vars) {
        if (httpVars != null) {
            http.addParams(httpVars);
        }
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

    protected HttpQuery onCreateHttpQuery() {
        return new SimpleHttpQuery();
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
    public Object onBuildResponseBody(HttpURLConnection connexion, InputStream stream, HttpAsyncQuery query) throws Exception {
        try {
            String incomingData = StreamOperationTools.streamToString(query.executionController, stream);
            return new JSONObject(incomingData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
