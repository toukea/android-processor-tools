package com.istat.freedev.processor.tools;

public abstract class ThreadExecuteProcess<Result, Error extends Throwable> extends ThreadProcess<Result, Error> {
    @Override
    protected Thread onCreateThread(final ExecutionVariables executionVariables) {
        return new Thread() {
            @Override
            public void run() {
                try {
                    onThreadRun(executionVariables);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    notifyAborted();
                } catch (Exception e) {
                    if (Thread.currentThread().isInterrupted()) {
                        notifyAborted();
                        return;
                    }
                    notifyFailed(e);
                }
            }
        };
    }

    protected abstract void onThreadRun(ExecutionVariables executionVariables) throws Exception;
}
