package com.istat.freedev.processor;

/**
 * Created by istat on 03/01/17.
 */

public class FileProcess <Result, Error extends Throwable> extends Process<Result, Error>{
    @Override
    protected void onExecute(Object... vars) {

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
