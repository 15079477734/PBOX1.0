package com.library.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import java.io.File;

import cn.kuaipan.android.openapi.AuthActivity;
import cn.kuaipan.android.openapi.AuthSession;
import cn.kuaipan.android.openapi.KuaipanAPI;
import cn.kuaipan.android.openapi.session.AccessTokenPair;
import cn.kuaipan.android.openapi.session.AppKeyPair;
import cn.kuaipan.android.sdk.exception.KscException;
import cn.kuaipan.android.sdk.exception.KscRuntimeException;
import cn.kuaipan.android.sdk.model.KuaipanFile;
import cn.kuaipan.android.sdk.oauth.Session;

/**
 * Created by Administrator on 2014/7/25 0025.
 */
public class KssDisk {
    final static String TAG = "AUTH";
    private static final String APP_KEY = "" + "xc8vGOU1bMxGSdxF";
    private static final String APP_SECRET = "" + "YlrtbYeTLSrZJ8V8";
    final static private String ACCOUNT_PREFS_NAME = "prefs";
    final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
    final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    final static private String ACCESS_AUTH_TYPE_NAME = "ACCESS_AUTH_TYPE_NAME";
    final static private String ACCESS_UID_NAME = "ACCESS_UID_NAME";
    private KuaipanAPI mApi;
    private AuthSession mAuthSession;
    private int authType;
    private boolean mLoggedIn;
    private Context mContext;
    private RequestBase request;
    private Activity activity;
    private static final String DOWNLOAD_DIR = Environment.getExternalStorageDirectory().getPath() + "/PlanBox/";

    public KssDisk(Context context) {
        mContext = context;
        mAuthSession = buildSession();
        mApi = new KuaipanAPI(mContext, mAuthSession);
    }

    public KssDisk(Context context, Activity activity) {
        mContext = context;
        mAuthSession = buildSession();
        mApi = new KuaipanAPI(mContext, mAuthSession);
        this.activity = activity;
    }


    public boolean isAuth() {
        return mApi.isAuthorized();
    }


    public void startUploadAsyncTask() {
        request = new RequestBase();
        request.setApi(mApi);
        new UploadAsyTask().execute(request);
    }


    public void startDownloadAsyncTask() {

        final KuaipanFile[] file = {null};
        request = new RequestBase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {

                    file[0] = mApi.metadata("/mydb.db");
                    String remotePath = file[0].path;
                    request.setRemotePath(remotePath);
                    request.setApi(mApi);
                    new DownloadAsyTask().execute(request);

                } catch (KscException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();

    }

    public void login() {
        mApi.startAuthForResult();
    }

    public void logout() {
        mApi.unAuthorize();
        clearKeys();
    }

    private String[] getKeys() {
        SharedPreferences prefs = mContext.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        String uid = prefs.getString(ACCESS_UID_NAME, null);
        String authType = prefs.getString(ACCESS_AUTH_TYPE_NAME, null);
        if (key != null && secret != null) {
            String[] ret = new String[4];
            ret[0] = key;
            ret[1] = secret;
            ret[2] = uid;
            ret[3] = authType;
            return ret;
        } else {
            return null;
        }
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a
     * local store, rather than storing user name & password, and
     * re-authenticating each time (which is not to be done, ever).
     */
    public void storeKeys(String key, String secret, String uid,
                          String authType) {
        // Save the access key for later
        SharedPreferences prefs = mContext.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, key);
        edit.putString(ACCESS_SECRET_NAME, secret);
        edit.putString(ACCESS_UID_NAME, uid);
        edit.putString(ACCESS_AUTH_TYPE_NAME, authType);
        edit.commit();
    }

    private void clearKeys() {
        SharedPreferences prefs = mContext.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    private AuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AuthSession session;

        String[] stored = getKeys();
        if (stored != null) {
            AccessTokenPair accessToken = new AccessTokenPair(stored[0],
                    stored[1]);
            if (TextUtils.isEmpty(stored[2])) {

            }
            authType = !TextUtils.isEmpty(stored[2]) ? Integer
                    .valueOf(stored[2]) : 0;
            session = new AuthSession(appKeyPair, accessToken, Session.Root.KUAIPAN);
            this.authType = Integer.valueOf(stored[3]);
        } else {
            session = new AuthSession(appKeyPair, Session.Root.KUAIPAN);
        }

        return session;
    }

    public String getAuthResultName(int resultCode) {
        if (authType == AuthActivity.CANCELED) {
            return "[CANCELED]";
        } else if (authType == AuthActivity.FAILED) {
            return "[FAILED]";
        } else if (authType == AuthActivity.SUCCESSED) {
            return "[SUCCESSED]";
        }
        return "[UnKnown]";
    }

    public String getAuthName(int authType) {
        if (authType == AuthActivity.WEIBO_AUTH) {
            return "[weibo]";
        } else if (authType == AuthActivity.QQ_AUTH) {
            return "[qq]";
        } else if (authType == AuthActivity.XIAOMI_AUTH) {
            return "[xiaomi]";
        } else if (authType == AuthActivity.KUAIPAN_AUTH) {
            return "[kuaipan]";
        }
        return "[UnKnown]";
    }

    public void setAccessToken(String accessToken, String accessSecret) {
        mApi.setAccessToken(accessToken, accessToken);
    }

    public void onActivityReport(int requestCode, int resultCode,
                                 Intent intent) {
        Log.v(TAG, "requestCode:" + requestCode + " resultCode:" + resultCode);

        if (requestCode == AuthActivity.AUTH_REQUEST_CODE) {

            Log.v(TAG, "resultCode: " + getAuthResultName(resultCode));
            Log.v(TAG, "ResultData:  " + intent);

            if (intent == null) {
                return;
            }

            if (resultCode == AuthActivity.SUCCESSED && intent != null) {
                Bundle values = intent.getExtras();
                final String accessToken = values
                        .getString(AuthActivity.EXTRA_ACCESS_TOKEN);
                final String accessSecret = values
                        .getString(AuthActivity.EXTRA_ACCESS_SECRET);
                final String uid = values.getString(AuthActivity.EXTRA_UID);
                int authType = values.getInt(AuthActivity.EXTRA_AUTH_TYPE);

                Log.v(TAG, "Authorized by " + getAuthName(authType) + " server");

                final String error_code = values
                        .getString(AuthActivity.EXTRA_ERROR_CODE);
                Log.v(TAG, "!!!accessToken=" + accessToken + "\n"
                        + "accessSecret=" + accessSecret + "\n" + "uid=" + uid
                        + "\n" + "error_code" + error_code);

                /**
                 * set validate access token pair, then isAuthorized will return
                 * true
                 */
                setAccessToken(accessToken, accessSecret);

                // store all concern values into preference
                storeKeys(accessToken, accessSecret, uid,
                        String.valueOf(authType));
            }
        }
    }

    protected class UploadAsyTask extends AsyncTask<RequestBase, Void, ResultBase> {
        protected void onPreExecute() {
            Log.v(TAG, "====Upload task onPreExecute====");
            Toast.makeText(mContext, "开始上传", Toast.LENGTH_SHORT).show();
        }


        protected ResultBase doInBackground(RequestBase... params) {
            final RequestBase req = (RequestBase) params[0];
            final Uri selectedImage = req.getFileUri();
            final KuaipanAPI api = req.getApi();

            File file = new File("/data/data/com.PlanBox.planboxone/databases/mydb.db");
            if (!file.exists()) {
                return null;
            }

            ResultBase result = new ResultBase();
            Log.d(TAG, "ready to upload file " + file.getAbsolutePath() + " name:" + file.getName());

            try {
                api.upload(file, "/" + file.getName(),
                        new TransportListener(TransportListener.OPERATION_UPLOAD, "Upload"));
            } catch (KscRuntimeException e) {
                e.printStackTrace();
            } catch (KscException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.v("over", "====Upload task doInBackground end====");
            return result;
        }

        protected void onPostExecute(ResultBase result) {

            if (result == null) {
                Toast.makeText(mContext, "上传失败 error code = 3", Toast.LENGTH_LONG).show();
                return;
            }

            if (result.getErrorMsg() != null) {
                Toast.makeText(mContext, "上传失败 error code = 4 ", Toast.LENGTH_LONG).show();
            } else {
                String str = "Download Task Finish" +
                        "Download file from [" + result.getRemotePath()
                        + "] " + "\n" + "to [" + result.getFilePath() + "] Successed!";

                Log.v(TAG, str);
                Toast.makeText(mContext, "上传成功", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected class DownloadAsyTask extends AsyncTask<RequestBase, Void, ResultBase> {


        protected void onPreExecute() {
            Log.v(TAG, "====Download task onPreExecute====");
            Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
        }


        protected ResultBase doInBackground(RequestBase... params) {
            Log.v(TAG, "====Download task doInBackground====");
            if (params == null || params.length != 1) {
                return null;
            }

            final RequestBase req = (RequestBase) params[0];
            final String remotePath = req.getRemotePath();
            final KuaipanAPI api = req.getApi();

            ResultBase result = new ResultBase();

            String localPath = DOWNLOAD_DIR + new File(remotePath).getName();
            result.setRemotePath(remotePath);
            result.setFilePath(localPath);
            File dir = new File(DOWNLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            try {
                api.download(remotePath, localPath, "", false,
                        new TransportListener(TransportListener.OPERATION_DOWNLOAD, "Download"));
            } catch (KscRuntimeException e) {
                e.printStackTrace();
                result.setErrorMsg(e.toString());
            } catch (KscException e) {
                e.printStackTrace();
                result.setErrorMsg(e.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.v(TAG, "====Download task doInBackground end====");
            return result;
        }

        @Override
        protected void onPostExecute(ResultBase result) {

            if (result == null) {
                Toast.makeText(mContext, "下载失败 error code=1", Toast.LENGTH_LONG).show();
                return;
            }

            if (result.getErrorMsg() != null) {
                Toast.makeText(mContext, "下载失败 error code=2", Toast.LENGTH_LONG).show();
            } else {
                String str = "Download Task Finish" + "Download file from [" + result.getRemotePath()
                        + "] " + "\n" + "to [" + result.getFilePath() + "] Successed!";
                Log.v(TAG, str);
                Toast.makeText(mContext, "下载成功", Toast.LENGTH_LONG).show();
            }
        }


    }
}

