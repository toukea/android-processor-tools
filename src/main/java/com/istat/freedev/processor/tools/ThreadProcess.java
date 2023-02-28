package com.istat.freedev.processor.tools;

import com.istat.freedev.processor.Process;

/**
 * Created by istat on 07/02/17.
 */

public abstract class ThreadProcess<Result, Error extends Throwable> extends Process<Result, Error> {
    Thread thread;

    @Override
    protected final void onExecute(ExecutionVariables executionVariables) {
        thread = onCreateThread(executionVariables);
        onThreadCreated(thread, executionVariables);
        if (thread != null && !thread.isAlive()) {
            thread.start();
        }
    }

    protected void onThreadCreated(Thread thread, ExecutionVariables executionVariables){

    }

    protected abstract Thread onCreateThread(ExecutionVariables executionVariables);

    @Override
    protected void onResume() {
        throw new RuntimeException("Not yet supported.");
    }

    @Override
    protected void onPaused() {
        throw new RuntimeException("Not yet supported.");
    }

    @Override
    protected void onStopped() {
        throw new RuntimeException("Not yet supported.");
    }

    @Override
    protected void onCancel() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public boolean isRunning() {
        return super.isRunning() && (thread != null && thread.isAlive());
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted() || (thread != null &&
                !thread.isInterrupted() &&
                !thread.isAlive() &&
                thread.getState() == Thread.State.TERMINATED);
    }

    @Override
    public boolean isPaused() {
        return thread != null &&
                thread.getState() == Thread.State.BLOCKED;
    }

    public Thread getThread() {
        return thread;
    }

    public final static ThreadProcess newOne(final Thread thread) {
        return new ThreadProcess() {
            @Override
            protected Thread onCreateThread(ExecutionVariables executionVariables) {
                return thread;
            }
        };
    }

    public static ThreadProcess newOne(final Runnable runnable) {
        return new ThreadProcess() {
            @Override
            protected Thread onCreateThread(ExecutionVariables executionVariables) {
                return new Thread(runnable);
            }
        };
    }
}
