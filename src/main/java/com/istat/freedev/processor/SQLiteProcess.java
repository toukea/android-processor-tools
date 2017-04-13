package com.istat.freedev.processor;

import istat.android.data.access.sqlite.SQLiteDelete;
import istat.android.data.access.sqlite.SQLiteInsert;
import istat.android.data.access.sqlite.SQLiteMerge;
import istat.android.data.access.sqlite.SQLiteSelect;
import istat.android.data.access.sqlite.SQLiteUpdate;

/**
 * Created by istat on 03/01/17.
 */

public class SQLiteProcess<Result, Error extends Throwable> extends Process<Result, Error> {
    Thread thread;

    public SQLiteProcess() {

    }

    @Override
    protected void onExecute(final Object... vars) {
        thread = new Thread() {
            @Override
            public void run() {
                for (Object sql : vars) {
                    if (sql instanceof SQLiteSelect) {
                    } else if (sql instanceof SQLiteSelect) {

                    } else if (sql instanceof SQLiteUpdate) {

                    } else if (sql instanceof SQLiteDelete) {

                    } else if (sql instanceof SQLiteInsert) {

                    } else if (sql instanceof SQLiteMerge) {

                    }
                }

            }
        };
        thread.start();
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
        return thread != null && thread.isAlive();
    }

    @Override
    public boolean isCompleted() {
        return thread != null && !thread.isAlive();
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    public class SQLiteSelectProcess<T> extends SQLiteProcess<T, Error> {
        final void execute(ProcessManager manager, SQLiteSelect... vars) {
            super.execute(manager, (Object[]) vars);
        }

    }
}
