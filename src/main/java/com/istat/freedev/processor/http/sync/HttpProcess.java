package com.istat.freedev.processor.http.sync;

import com.istat.freedev.processor.Process;
import com.istat.freedev.processor.http.AbsHttpProcess;

/**
 * Created by istat on 19/09/17.
 */

public class HttpProcess<Result, Error extends Throwable> extends AbsHttpProcess<Result, Error> {
    @Override
    protected void onExecute(ExecutionVariables executionVariables) {

    }

    @Override
    protected void onResume() {

    }

    @Override
    protected void onPaused() {

    }

    @Override
    protected void onStopped() {

    }

    @Override
    protected void onCancel() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }
}
