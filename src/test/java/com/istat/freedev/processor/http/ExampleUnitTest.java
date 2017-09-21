package com.istat.freedev.processor.http;

import com.istat.freedev.processor.ProcessManager;
import com.istat.freedev.processor.Processor;

import org.junit.Test;

import java.util.HashMap;

import istat.android.data.access.sqlite.SQLite;
import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQueryError;
import istat.android.network.http.HttpQueryResponse;
import istat.android.network.http.HttpQueryResult;
import istat.android.network.http.SimpleHttpQuery;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    private void testHttpProcess() {
        AsyncHttp http = AsyncHttp.fromSimpleHttp();
        TestHttpProcess process = new TestHttpProcess(http);
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        Processor.from("machine").execute(process, "GET", "http://www.google.com", headers, params);
    }
}