package com.istat.freedev.processor;

import istat.android.data.access.sqlite.SQLiteDelete;
import istat.android.data.access.sqlite.SQLiteInsert;
import istat.android.data.access.sqlite.SQLiteMerge;
import istat.android.data.access.sqlite.SQLiteSelect;
import istat.android.data.access.sqlite.SQLiteUpdate;

/**
 * Created by istat on 03/01/17.
 */

public class SQLiteProcess<Result> extends Process<Result, Exception> {
    Thread thread;

    public SQLiteProcess() {

    }

    @Override
    protected void onExecute(final ExecutionVariables vars) {
        for (Object sql : vars.asArray()) {
            if (sql instanceof SQLiteSelect) {
            } else if (sql instanceof SQLiteSelect) {
                thread = ((SQLiteSelect) sql).executeAsync();
            } else if (sql instanceof SQLiteUpdate) {
                thread = ((SQLiteUpdate) sql).where1().executeAsync();
            } else if (sql instanceof SQLiteUpdate.Updater) {
                thread = ((SQLiteUpdate.Updater) sql).executeAsync();
            } else if (sql instanceof SQLiteDelete) {
                thread = ((SQLiteUpdate.Updater) sql).executeAsync();
            } else if (sql instanceof SQLiteInsert) {
                thread = ((SQLiteUpdate.Updater) sql).executeAsync();
            } else if (sql instanceof SQLiteMerge) {
                thread = ((SQLiteUpdate.Updater) sql).executeAsync();
            }
        }
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

    public class SQLiteSelectProcess<T extends SQLiteSelect> extends SQLiteProcess<T> {
        final void execute(ProcessManager manager, SQLiteSelect... vars) {
            super.execute(manager, vars);
        }
    }
}
