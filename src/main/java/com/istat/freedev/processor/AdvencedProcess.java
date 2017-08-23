package com.istat.freedev.processor;

import com.istat.freedev.processor.Process;

/**
 * Created by istat on 25/05/17.
 */

public abstract class AdvencedProcess<Result, Error extends Throwable> extends Process<Result, Error> {

    @Override
    public boolean isRunning() {
        return !isCanceled() && (getResult() == null || getError() == null);
    }

    @Override
    public boolean isCompleted() {
        return !isCanceled() && (getResult() != null || getError() != null);
    }
}
