package com.istat.freedev.processor.abs;

import android.os.Handler;
import android.os.Looper;

import com.istat.freedev.processor.Process;

import java.util.concurrent.Callable;

/**
 * Created by istat on 07/02/17.
 */

public abstract class AbsThreadRunnableProcess<Result, Error extends Throwable> extends AbsThreadProcess<Result, Error> {
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected Thread onCreateThread(final Process<Result, Error>.ExecutionVariables executionVariables) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Result result = AbsThreadRunnableProcess.this.run(executionVariables);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifySucceed(result);
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyFailed(e);
                        }
                    });
                }
            }
        });
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

    public final static <Result2, Error2 extends Throwable> AbsThreadRunnableProcess<Result2, Error2> newOne(final Callable<Result2> callable, Class<Result2> resultClass, Class<Error2> errorClass) {
        return new AbsThreadRunnableProcess<Result2, Error2>() {
            @Override
            protected Result2 run(Process<Result2, Error2>.ExecutionVariables executionVariables) throws Exception {
                return callable.call();
            }
        };
    }
}
