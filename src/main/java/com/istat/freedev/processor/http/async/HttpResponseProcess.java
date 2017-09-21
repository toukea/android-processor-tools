package com.istat.freedev.processor.http.async;

import istat.android.network.http.HttpQueryError;
import istat.android.network.http.HttpQueryResult;

/**
 * Created by istat on 20/09/17.
 */

public class HttpResponseProcess extends HttpAuthProcess<HttpQueryResult, HttpQueryError> {
    @Override
    protected void onSuccessHappen(HttpQueryResult result) {
        notifyProcessSuccess(result);
    }

    @Override
    protected void onUnHandledErrorHappen(HttpQueryError error) {
        notifyProcessError(error);
    }
}
