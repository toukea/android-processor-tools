package com.istat.freedev.processor.tools;

import com.istat.freedev.processor.Process;

import java.util.concurrent.Callable;

/**
 * Created by istat on 07/02/17.
 */

public abstract class AbsThreadRunnableProcess<Result, Error extends Throwable> extends AbsThreadProcess<Result, Error> {

    @Override
    protected Thread onCreateThread(final Process<Result, Error>.ExecutionVariables executionVariables) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Result result = AbsThreadRunnableProcess.this.run(executionVariables);
                    notifySucceed(result);
                } catch (final ErrorThrowingException e) {
                    //do notthingn if this happend, it is because lib-user has called notifyErrorAndThrow(Error), the system will now dispatch Error state.
                } catch (final Exception e) {
                    notifyFailed(e);
                }
            }
        });
    }

    protected final void notifyErrorAndThrow(Error error) throws RuntimeException {
        notifyError(error);
        throw new ErrorThrowingException();
    }

    protected abstract Result run(Process<Result, Error>.ExecutionVariables executionVariables) throws Exception;


    public final static AbsThreadRunnableProcess newOne(final Runnable runnable) {
        return new AbsThreadRunnableProcess() {
            @Override
            protected Runnable run(Process.ExecutionVariables executionVariables) {
                return runnable;
            }
        };
    }

    public final static <Result2, Error2 extends Throwable> AbsThreadRunnableProcess<Result2, Error2> newOne(final Callable<Result2> callable) {
        return new AbsThreadRunnableProcess<Result2, Error2>() {
            @Override
            protected Result2 run(Process<Result2, Error2>.ExecutionVariables executionVariables) throws Exception {
                return callable.call();
            }
        };
    }

    static class ErrorThrowingException extends RuntimeException {

    }
}
