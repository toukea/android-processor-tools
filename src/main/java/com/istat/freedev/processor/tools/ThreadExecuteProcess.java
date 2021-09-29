package com.istat.freedev.processor.tools;

public abstract  class ThreadExecuteProcess<Result, Error extends Throwable> extends ThreadProcess<Result,Error> {
    @Override
    protected Thread onCreateThread(final ExecutionVariables executionVariables) {
        return new Thread(){
            @Override
            public void run() {
                onThreadRun(executionVariables);
            }
        };
    }

    protected abstract void onThreadRun(ExecutionVariables executionVariables);
}
