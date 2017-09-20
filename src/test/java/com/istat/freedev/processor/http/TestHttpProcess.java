package com.istat.freedev.processor.http;


import com.istat.freedev.processor.http.async.HttpProcess;

import istat.android.network.http.AsyncHttp;

/**
 * Created by istat on 05/05/17.
 */

public class TestHttpProcess extends HttpProcess<String> {


    public TestHttpProcess(AsyncHttp asyncHttp) {
        super(asyncHttp);
    }

    public TestHttpProcess(AsyncHttp asyncHttp, int method, String url) {
        super(asyncHttp, method, url);
    }

//    @Override
//    protected AsyncHttp onCreateAsyncHttp(HashMap<String, String> headers, HashMap<String, ?> params, Object... otherVars) {
//        return null;
//    }
//
//    @Override
//    protected HttpError onHandleError(HttpURLConnection httpURLConnection, InputStream inputStream) throws Exception {
//        return null;
//    }
//
//    @Override
//    protected String onHandleResult(HttpURLConnection httpURLConnection, InputStream inputStream) throws Exception {
//        return null;
//    }

    public static class HttpError extends Exception {

    }
}
