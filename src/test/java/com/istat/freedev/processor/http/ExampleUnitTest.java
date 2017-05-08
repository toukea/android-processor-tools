package com.istat.freedev.processor.http;

import com.istat.freedev.processor.ProcessManager;
import com.istat.freedev.processor.Processor;

import org.junit.Test;

import java.util.HashMap;

import istat.android.data.access.sqlite.SQLite;
import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQueryError;

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
        TestHttpProcess process = new TestHttpProcess();
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        Processor.from("machine").execute(process, "GET", "http://www.google.com", headers, params);
    }


    interface Pop<T> {
        void onPop(T intence);
    }

    abstract class absPop implements Pop {

    }

    class Poper extends absPop implements HttpAsyncQuery.HttpQueryCallback {

        @Override
        public void onPop(Object intence) {
            AsyncHttp.fromSimpleHttp().
                    setQueryCallback(this).
                    doGet("http://");
        }

        @Override
        public void onHttpSuccess(HttpAsyncQuery.HttpQueryResponse result) {
            try {
                SQLite.SQL sql = SQLite.fromConnection("dbnAME");
                sql.select(this.getClass());
                sql.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onHttpError(HttpAsyncQuery.HttpQueryResponse result, HttpQueryError e) {

        }

        @Override
        public void onHttpFail(Exception e) {

        }

        @Override
        public void onHttComplete(HttpAsyncQuery.HttpQueryResponse result) {

        }

        @Override
        public void onHttpAborted() {

        }
    }
}