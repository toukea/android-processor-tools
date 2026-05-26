package com.istat.freedev.processor.tools;

import com.istat.freedev.processor.Process;

import java.util.concurrent.Callable;

/**
 * Created by istat on 07/02/17.
 */

public abstract class ThreadRunnableProcess<Result, Error extends Throwable> extends ThreadProcess<Result, Error> {

    @Override
    protected Thread onCreateThread(final Process<Result, Error>.ExecutionVariables executionVariables) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Result result = ThreadRunnableProcess.this.run(executionVariables);
                    notifySucceed(result);
                } catch (final ErrorThrowingException e) {
                    //do notthingn if this happend, it is because lib-user has called notifyErrorAndThrow(Error), the system will now dispatch Error state.
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                    notifyAborted();
                } catch (final Exception e) {
                    if (Thread.currentThread().isInterrupted()) {
                        notifyAborted();
                        return;
                    }
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

    public static <Error2 extends Throwable> ThreadRunnableProcess<Void, Error2> newRun(final ThrowingRunnable runnable) {
        return new ThreadRunnableProcess<>() {

            @Override
            protected Void run(Process<Void, Error2>.ExecutionVariables executionVariables) throws Exception {
                runnable.run();
                return null;
            }
        };
    }

    public static <Error2 extends Throwable> ThreadRunnableProcess<Void, Error2> newRun(final Runnable runnable) {
        return new ThreadRunnableProcess<>() {

            @Override
            protected Void run(Process<Void, Error2>.ExecutionVariables executionVariables) throws Exception {
                runnable.run();
                return null;
            }
        };
    }

    public static <Result2, Error2 extends Throwable> ThreadRunnableProcess<Result2, Error2> newCall(final Callable<Result2> callable) {
        return new ThreadRunnableProcess<Result2, Error2>() {
            @Override
            protected Result2 run(Process<Result2, Error2>.ExecutionVariables executionVariables) throws Exception {
                return callable.call();
            }
        };
    }

    static class ErrorThrowingException extends RuntimeException {

    }
    
    public interface ThrowingRunnable {
        void run() throws Exception;
    }
}
