package com.istat.freedev.processor.http;

import android.content.Context;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.istat.freedev.processor.Process;
import com.istat.freedev.processor.ProcessManager;
import com.istat.freedev.processor.Processor;
import com.istat.freedev.processor.abs.AbsAsyncTaskProcess;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;

import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQueryError;
import istat.android.network.http.HttpQueryResponse;
import istat.android.network.http.HttpQueryResult;
import istat.android.network.http.SimpleHttpQuery;
import istat.android.network.http.interfaces.DownloadHandler;
import istat.android.network.utils.ToolKits;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.istat.freedev.processor.http.test", appContext.getPackageName());
    }

    @Test
    public void testTask() throws Exception {
        // Context of the app under test.
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        GoogleTaskProcess process = new GoogleTaskProcess();
        Processor.getDefault().execute(process);
        process
                .then(new Process.PromiseCallback<String>() {
                    @Override
                    public void onPromise(String data) {
                        System.out.println("task success");
                    }
                })
                .failed(new Process.PromiseCallback<Throwable>() {
                    @Override
                    public void onPromise(Throwable data) {
                        data.printStackTrace();
                        System.out.println("task failed");
                    }
                })
                .error(new Process.PromiseCallback<Exception>() {
                    @Override
                    public void onPromise(Exception data) {
                        if (data != null) {
                            data.printStackTrace();
                        }
                    }
                })
                .catchException(new Process.PromiseCallback<Throwable>() {
                    @Override
                    public void onPromise(Throwable data) {
                        data.printStackTrace();
                    }
                })
                .finish(new Process.PromiseCallback<Process>() {
                    @Override
                    public void onPromise(Process data) {
                        countDownLatch.countDown();
                    }
                });
        countDownLatch.await();
    }


    class GoogleTaskProcess extends AbsAsyncTaskProcess<String, Exception> implements HttpAsyncQuery.Callback {

        @Override
        protected AsyncTask onCreateAsyncTask(ExecutionVariables vars) {
            SimpleHttpQuery http = new SimpleHttpQuery();
            http.setDownloadHandler(new DownloadHandler() {
                @Override
                public Object onBuildResponseBody(HttpURLConnection connexion, InputStream stream) throws Exception {
                    String content = ToolKits.Stream.streamToString(stream);
                    throw new Exception("Error bas download");
                }
            });
            return AsyncHttp.from(http).doQuery(HttpAsyncQuery.TYPE_GET, "http://www.google.com", this);
        }

        @Override
        protected void onResume() {

        }

        @Override
        protected void onPaused() {

        }

        @Override
        public void onHttpSuccess(HttpQueryResult resp) {
            notifySucceed(resp.getBodyAsString());
        }

        @Override
        public void onHttpError(HttpQueryError e) {
            notifyError(e);
        }

        @Override
        public void onHttpFailure(Exception e) {
            notifyFailed(e);
        }

        @Override
        public void onHttComplete(HttpQueryResponse resp) {

        }

        @Override
        public void onHttpAborted() {

        }
    }
}
