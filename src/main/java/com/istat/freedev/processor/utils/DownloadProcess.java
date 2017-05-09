package com.istat.freedev.processor.utils;


/**
 * Created by istat on 09/01/17.
 */

public class DownloadProcess<Result, Error extends Throwable> extends DefaultHttpProcess<Result, Error> {


    public DownloadProcess(Class<Result> resultCLass, Class<Error> errorClass) {
        super(resultCLass, errorClass);
    }

    public DownloadProcess(AsyncHttpCreator creator, Class<Result> resultCLass, Class<Error> errorClass) {
        super(creator, resultCLass, errorClass);
    }
}
