package com.istat.freedev.processor.http.async;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.istat.freedev.processor.http.AbsHttpProcess;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.concurrent.Executor;

import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQuery;
import istat.android.network.http.HttpQueryError;
import istat.android.network.http.HttpQueryResponse;
import istat.android.network.http.HttpQueryResult;
import istat.android.network.http.interfaces.DownloadHandler;
import istat.android.network.http.interfaces.ProgressionListener;
import istat.android.network.utils.StreamOperationTools;

/**
 * Created by Istat Toukea on 23/08/2017.
 */

public class HttpProcess<Result, Error extends Throwable> extends AbsHttpProcess<Result, Error> {
    private HttpAsyncQuery asyncTask;
    protected Executor executor;
    final protected HashMap<DownloadHandler.WHEN, DownloadHandler> downloaderMap = new HashMap<>();

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setUploader(HttpAsyncQuery.HttpUploadHandler uploader) {
        this.uploader = uploader;
    }

    public void setDownloader(final DownloadHandler downloader) {
        DownloadHandler.WHEN when = null;
        setDownloader(downloader, when);
    }

    public void setDownloader(HttpAsyncQuery.HttpDownloadHandler downloader) {
        DownloadHandler.WHEN when = null;
        setDownloader(downloader, when);
    }

    public void setDownloader(final DownloadHandler downloader, final ProgressionListener progressionListener) {
        DownloadHandler.WHEN when = null;
        setDownloader(downloader, when, progressionListener);
    }

    public void setDownloader(final DownloadHandler downloader, DownloadHandler.WHEN when) {
        setDownloader(downloader, when, null);
    }

    public void setDownloader(HttpAsyncQuery.HttpDownloadHandler downloader, DownloadHandler.WHEN when) {
        downloaderMap.put(when, downloader);

    }

    public void setDownloader(final DownloadHandler downloader, DownloadHandler.WHEN when, final ProgressionListener progressionListener) {
        if (downloader == null && progressionListener == null) {
            return;
        }
        setDownloader(new HttpAsyncQuery.HttpDownloadHandler() {
            @Override
            public void onProgress(HttpAsyncQuery query, long... integers) {
                if (progressionListener != null) {
                    progressionListener.onProgress(query, integers);
                }
            }

            @Override
            public Object onBuildResponseBody(HttpURLConnection connexion, InputStream stream) throws Exception {
                if (downloader != null) {
                    return downloader.onBuildResponseBody(connexion, stream);
                } else {
                    try {
                        return StreamOperationTools.streamToString(asyncTask.executionController,
                                stream, bufferSize, encoding);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                }
            }
        }, when);
    }

    protected HttpAsyncQuery onCreateHttpAsyncQuery(HttpQuery http, int method, String url, ExecutionVariables vars) {
        AsyncHttp asyncHttp = AsyncHttp.from(http)
                .setQueryCallback(new HttpAsyncQuery.HttpQueryCallback() {
                    @Override
                    public void onHttpSuccess(HttpQueryResult resp) {
                        onSuccessHappen(resp);
                    }

                    @Override
                    public void onHttpError(HttpQueryError e) {
                        onErrorHappen(e);
                    }

                    @Override
                    public void onHttpFail(Exception e) {
                        onFailingHappen(e);
                    }

                    @Override
                    public void onHttComplete(HttpQueryResponse resp) {

                    }

                    @Override
                    public void onHttpAborted() {
                        onAbortionHappen();
                    }
                });
        if (this.executor != null) {
            asyncHttp.useExecutor(executor);
        }
        if (this.bufferSize > 0) {
            asyncHttp.useBufferSize(bufferSize);
        }
        if (this.uploader != null) {
            asyncHttp.useUploader(uploader);
        }
        DownloadHandler successDownloader = downloaderMap.get(DownloadHandler.WHEN.SUCCESS);
        DownloadHandler errorDownloader = downloaderMap.get(DownloadHandler.WHEN.ERROR);
        if (successDownloader != null) {
            asyncHttp.useDownloader(successDownloader);
        }
        if (errorDownloader != null) {
            asyncHttp.useDownloader(errorDownloader);
        }
        if (!TextUtils.isEmpty(this.encoding)) {
            asyncHttp.useEncoding(encoding);
        }
        onPreExecute(http, HttpAsyncQuery.METHOD_TYPE_NAME_MAP.get(method), url);
        HttpAsyncQuery httpAsyncQuery = asyncHttp.doQuery(method, url);
        onPostExecute(httpAsyncQuery);
        return httpAsyncQuery;
    }


    protected void onPreExecute(HttpQuery http, String method, String url) {

    }

    private void onPostExecute(HttpAsyncQuery httpAsyncQuery) {
    }

    protected void onSuccessHappen(HttpQueryResult result) {
        notifyProcessSuccess((Result) result.getBody());
    }

    protected void onErrorHappen(HttpQueryError error) {
        notifyProcessError((Error) error.getBody());
    }

    protected void onFailingHappen(Throwable error) {
        notifyProcessFailed(error);
    }

    protected void onAbortionHappen() {
        notifyProcessAborted();
    }

    @Override
    protected void onResume() {
        throw new RuntimeException("Not yet supported.");
    }

    @Override
    protected void onPaused() {
        throw new RuntimeException("Not yet supported.");
    }

    @Override
    protected final void onExecute(ExecutionVariables vars) {
        asyncTask = onCreateHttpAsyncQuery(getHttpQuery(), getHttpMethodInteger(), getURL(), vars);
    }

    @Override
    protected final void onStopped() {
        if (asyncTask != null)
            asyncTask.cancel(true);
    }

    @Override
    protected final void onCancel() {
        if (asyncTask != null)
            asyncTask.cancel(true);
    }

    @Override
    public boolean isRunning() {
        if (asyncTask != null)
            return asyncTask.getStatus() == AsyncTask.Status.RUNNING;
        return false;
    }

    @Override
    public boolean isCompleted() {
        if (asyncTask != null)
            return asyncTask.getStatus() == AsyncTask.Status.FINISHED;
        return false;
    }

    @Override
    public boolean isPaused() {
        throw new RuntimeException("Not yet supported.");
    }

    private int getHttpMethodInteger() {
        String method = getHttpMethod();
        return HttpAsyncQuery.METHOD_NAME_TYPE_MAP.get(method);
    }

    protected HttpAsyncQuery getAsyncTask() {
        return asyncTask;
    }
}
