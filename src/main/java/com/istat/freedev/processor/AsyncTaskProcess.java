package com.istat.freedev.processor;


import android.os.AsyncTask;
import android.util.Log;

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

public class AsyncTaskProcess<Result, Error extends Throwable> extends Process<Result, Error> {
    AsyncTask asyncTask;

    @Override
    protected final void onExecute(Object... vars) {

        if (asyncTask != null) {
            asyncTask.execute(vars);
        }
    }

    @Override
    protected final void onResume() {

    }

    @Override
    protected final void onPaused() {

    }

    @Override
    protected final void onStopped() {
        if (asyncTask != null)
            asyncTask.cancel(true);
    }

    @Override
    protected final void onCancel() {
        if (asyncTask != null)
            asyncTask.cancel(true);
    }

    @Override
    public boolean isRunning() {
        if (asyncTask != null)
            return asyncTask.getStatus() == AsyncTask.Status.RUNNING;
        return false;
    }

    @Override
    public boolean isCompleted() {
        if (asyncTask != null)
            return asyncTask.getStatus() == AsyncTask.Status.FINISHED;
        return false;
    }

    @Override
    public boolean isPaused() {

        return false;
    }

}
