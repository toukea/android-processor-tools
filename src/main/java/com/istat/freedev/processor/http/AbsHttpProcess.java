package com.istat.freedev.processor.http;

import com.istat.freedev.processor.Process;

import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQuery;
import istat.android.network.http.interfaces.UpLoadHandler;

/**
 * Created by istat on 19/09/17.
 */

public abstract class AbsHttpProcess<Result, Error extends Throwable> extends Process<Result, Error> {
    protected String encoding;
    protected UpLoadHandler uploader;
    protected int bufferSize;

    protected String getEncoding() {
        return encoding;
    }

    protected int getBufferSize() {
        return bufferSize;
    }

    protected UpLoadHandler getUploader() {
        return uploader;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setUploader(UpLoadHandler uploader) {
        this.uploader = uploader;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public HttpQuery<?> getHttpQuery() {
        return getExecutionVariable(0);
    }

    public String getHttpMethod() {
        return getExecutionVariable(1);
    }

    public String getURL() {
        return getExecutionVariable(2);
    }
}
