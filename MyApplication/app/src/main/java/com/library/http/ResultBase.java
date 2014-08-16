
package com.library.http;

import cn.kuaipan.android.openapi.KuaipanAPI;

public class ResultBase {
    private KuaipanAPI api;
    private String remotePath;
    private String filePath;
    private String errorMsg;

    public ResultBase() {

    }

    public ResultBase(KuaipanAPI api, String filePath, String errorMsg) {
        this.api = api;
        this.filePath = filePath;
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ResultBase(KuaipanAPI api, String filePath) {
        this.api = api;
        this.filePath = filePath;
    }

    public KuaipanAPI getApi() {
        return api;
    }

    public void setApi(KuaipanAPI api) {
        this.api = api;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }
}
