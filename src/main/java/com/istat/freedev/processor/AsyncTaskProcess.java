package com.istat.freedev.processor;


import android.os.AsyncTask;

/**
 * Created by istat on 03/11/16.
 */

public abstract class AsyncTaskProcess<Result, Error extends Throwable> extends Process<Result, Error> {
    AsyncTask asyncTask;

    @Override
    protected final void onExecute(ExecutionVariables vars) {
        asyncTask = onCreateAsyncTask(vars);
    }

    protected abstract AsyncTask onCreateAsyncTask(ExecutionVariables vars);

    @Override
    protected final void onStopped() {
        if (asyncTask != null)
            asyncTask.cancel(true);
    }

    @Override
    protected final void onCancel() {
        if (asyncTask != null)
            asyncTask.cancel(true);
    }

    @Override
    public boolean isRunning() {
        if (asyncTask != null)
            return asyncTask.getStatus() == AsyncTask.Status.RUNNING;
        return false;
    }

    @Override
    public boolean isCompleted() {
        if (asyncTask != null)
            return asyncTask.getStatus() == AsyncTask.Status.FINISHED;
        return false;
    }

    @Override
    public boolean isPaused() {

        return false;
    }

}
