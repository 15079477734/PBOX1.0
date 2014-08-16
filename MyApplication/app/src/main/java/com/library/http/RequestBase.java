
package com.library.http;

import cn.kuaipan.android.openapi.KuaipanAPI;

import android.net.Uri;

public class RequestBase {
    private KuaipanAPI api;
    private Uri fileUri;
    private String filePath;
    private String remotePath;

    public RequestBase() {

    }

    public RequestBase(KuaipanAPI api, Uri filePath) {
        this.api = api;
        this.fileUri = filePath;
    }

    public KuaipanAPI getApi() {
        return api;
    }

    public void setApi(KuaipanAPI api) {
        this.api = api;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri filePath) {
        this.fileUri = filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

}
