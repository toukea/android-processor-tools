package com.istat.freedev.processor.http;

import com.istat.freedev.processor.HttpProcess;

import java.util.HashMap;

import istat.android.network.http.AsyncHttp;

/**
 * Created by istat on 05/05/17.
 */

public class TestHttpProcess extends HttpProcess<String, TestHttpProcess.HttpError> {

    @Override
    protected AsyncHttp onCreateAsyncHttp(String method, String url, HashMap<String, String> params, HashMap<String, String> headers, Object... otherVars) {
        return null;
    }

    public static class HttpError extends Exception {

    }
}
