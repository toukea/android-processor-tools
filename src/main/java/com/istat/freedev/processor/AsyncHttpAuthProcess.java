package com.istat.freedev.processor;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.istat.freedev.processor.abs.AbsAsyncTaskProcess;

import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;

/**
 * Created by Istat Toukea on 23/08/2017.
 */

public class AsyncHttpAuthProcess<Result> extends AsyncHttpProcess<Result> {
    AsyncHttp asyncHttp;
    int method;
    String url;

    public AsyncHttpAuthProcess(AsyncHttp asyncHttp) {
        super(asyncHttp);
    }

    public AsyncHttpAuthProcess(AsyncHttp asyncHttp, int method, String url) {
        super(asyncHttp, method, url);
    }

    protected void onErrorHappen(HttpAsyncQuery.HttpQueryResponse resp, Exception error) {
        if (resp.getCode() == 401) {

        } else {
            notifyProcessError(error);
        }
    }
}
