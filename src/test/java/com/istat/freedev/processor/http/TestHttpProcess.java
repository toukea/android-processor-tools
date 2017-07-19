package com.istat.freedev.processor.http;

import com.istat.freedev.processor.HttpProcess;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import istat.android.network.http.AsyncHttp;

/**
 * Created by istat on 05/05/17.
 */

public class TestHttpProcess extends HttpProcess<String, TestHttpProcess.HttpError> {


    @Override
    protected AsyncHttp onCreateAsyncHttp(HashMap<String, String> headers, HashMap<String, ?> params, Object... otherVars) {
        return null;
    }

    @Override
    protected HttpError onHandleError(HttpURLConnection httpURLConnection, InputStream inputStream) throws Exception {
        return null;
    }

    @Override
    protected String onHandleResult(HttpURLConnection httpURLConnection, InputStream inputStream) throws Exception {
        return null;
    }

    public static class HttpError extends Exception {

    }
}
