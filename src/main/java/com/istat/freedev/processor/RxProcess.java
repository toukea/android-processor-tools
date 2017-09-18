package com.istat.freedev.processor;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by istat on 06/09/17.
 */

public abstract class RxProcess<Result, Error extends Throwable> extends Process<Result, Error> {
    Subscription mSubscription;

    @Override
    protected void onExecute(final ExecutionVariables executionVariables) {
//        mSubscription = Observable.from(new String[]{})
//                .mapMany(new Func1<String, Observable<Result>>() {
//                    @Override
//                    public Observable<Result> call(String s) {
//                        return onCreateRXObservable(executionVariables);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(this);
    }

    protected abstract Observable<Result> onCreateRXObservable(ExecutionVariables executionVariables);

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
