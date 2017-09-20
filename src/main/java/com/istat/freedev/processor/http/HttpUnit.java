package com.istat.freedev.processor.http;

import com.istat.freedev.processor.Process;
import com.istat.freedev.processor.ProcessManager;
import com.istat.freedev.processor.utils.ProcessUnit;

import istat.android.network.http.HttpQuery;

/**
 * Created by istat on 19/09/17.
 */

public class HttpUnit extends ProcessUnit {

    public <R, E extends Throwable> Process<R, E> execute(String PID, Process<R, E> process, HttpQuery http, String method, String url) throws ProcessManager.ProcessException {
        return super.execute(PID, process, http, method, url);
    }

    public <R, E extends Throwable> Process<R, E> execute(Process<R, E> process, HttpQuery http, String method, String url) {
        return super.execute(process, http, method, url);
    }
}
