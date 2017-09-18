package com.istat.freedev.processor;

import android.os.AsyncTask;
import android.text.TextUtils;


import com.istat.freedev.processor.abs.AbsAsyncTaskProcess;

import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQueryError;

/**
 * Created by Istat Toukea on 23/08/2017.
 */

public class AsyncHttpProcess<Result> extends AbsAsyncTaskProcess<Result, HttpQueryError> {
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
                    onSuccessHappen(resp, (Result) resp.optBody());
                } else if (resp.isAccepted() &&  resp.getError() instanceof HttpQueryError) {
                    onErrorHappen(resp, (HttpQueryError) resp.getError());
                } else if (query.isAborted()) {
                    onAbortionHappen();
                } else {
                    onFailingHappen(resp.getError());
                }
            }
        }, HttpAsyncQuery.WHEN_ANYWAY);
        return httpAsyncQuery;
    }

    protected void onSuccessHappen(HttpAsyncQuery.HttpQueryResponse resp, Result result) {
        notifyProcessSuccess(result);
    }

    protected void onErrorHappen(HttpAsyncQuery.HttpQueryResponse resp, HttpQueryError error) {
        notifyProcessError(error);
    }

    protected void onFailingHappen(Exception error) {
        notifyProcessFailed(error);
    }

    protected void onAbortionHappen() {
        notifyProcessAborted();
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
