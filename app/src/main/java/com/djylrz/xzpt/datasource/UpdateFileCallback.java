package com.djylrz.xzpt.datasource;

public interface UpdateFileCallback {

    void onFileUpdated(String fileName, boolean isSuccess);

    void onFileUpdated(String fileName);
}
