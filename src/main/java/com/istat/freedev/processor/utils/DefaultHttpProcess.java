package com.istat.freedev.processor.utils;


import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;

import istat.android.network.http.interfaces.DownloadHandler;
import istat.android.network.utils.ToolKits;

/**
 * Created by istat on 08/05/17.
 */

public class DefaultHttpProcess<Result, Error extends Throwable> extends SimpleHttpProcess<Result, Error> implements DownloadHandler<Result> {
    Class<Result> resultClass;
    Class<Error> errorClass;

    public DefaultHttpProcess(Class<Result> resultCLass, Class<Error> errorClass) {
        this(DEFAULT_ASYNC_HTTP_CREATOR, resultCLass, errorClass);
    }

    public DefaultHttpProcess(AsyncHttpCreator creator, Class<Result> resultCLass, Class<Error> errorClass) {
        this.resultClass = resultCLass;
        this.errorClass = errorClass;
        setErrorDownloader(getErrorDownloader());
        setSuccessDownloader(this);
        setAsyncHttpCreator(creator);
    }


    public DownloadHandler<Error> getErrorDownloader() {
        return new DownloadHandler<Error>() {
            @Override
            public Error onBuildResponseBody(HttpURLConnection connexion, InputStream stream) throws Exception {
                String jsonString = ToolKits.Stream.streamToString(stream);
                Gson gson = new Gson();
                Error error = gson.fromJson(jsonString, errorClass);
                return error;
            }
        };
    }

    @Override
    public Result onBuildResponseBody(HttpURLConnection connexion, InputStream stream) throws Exception {
        String jsonString = ToolKits.Stream.streamToString(stream);
        Gson gson = new Gson();
        Result error = gson.fromJson(jsonString, resultClass);
        return error;
    }
}
