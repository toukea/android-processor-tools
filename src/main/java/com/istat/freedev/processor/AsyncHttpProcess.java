package com.istat.freedev.processor;

import android.os.AsyncTask;
import android.text.TextUtils;


import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;

/**
 * Created by Istat Toukea on 23/08/2017.
 */

public class AsyncHttpProcess<Result> extends AsyncTaskProcess<Result, Exception> {
    AsyncHttp asyncHttp;
    int method;
    String url;

    public AsyncHttpProcess(AsyncHttp asyncHttp) {
        this.asyncHttp = asyncHttp;
    }

    public AsyncHttpProcess(AsyncHttp asyncHttp, int method, String url) {
        this.asyncHttp = asyncHttp;
        this.method = method;
        this.url = url;
    }

    @Override
    protected AsyncTask onCreateAsyncTask(ExecutionVariables vars) {
        if (method == 0 || TextUtils.isEmpty(url) && vars.length() >= 2) {
            method = vars.getVariable(0);
            url = vars.getVariable(1);
        }
        HttpAsyncQuery httpAsyncQuery = this.asyncHttp.doQuery(method, url);
        httpAsyncQuery.runWhen(new HttpAsyncQuery.WhenCallback() {
            @Override
            public void onWhen(HttpAsyncQuery.HttpQueryResponse resp, HttpAsyncQuery query, int when) {
                if (resp.isSuccess()) {
                    notifyProcessSuccess((Result) resp.optBody());
                } else if (resp.isAccepted()) {
                    notifyProcessError(resp.getError());
                } else if (query.isAborted()) {
                    notifyProcessAborted();
                } else {
                    notifyProcessFailed(resp.getError());
                }
            }
        }, HttpAsyncQuery.WHEN_ANYWAY);
        return httpAsyncQuery;
    }

    @Override
    protected void onResume() {
        throw new RuntimeException("Not yet supported.");
    }

    @Override
    protected void onPaused() {
        throw new RuntimeException("Not yet supported.");
    }

}
