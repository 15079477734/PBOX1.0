
package com.library.http;

import cn.kuaipan.android.http.KscSpeedManager;
import cn.kuaipan.android.http.KscSpeedMonitor;
import cn.kuaipan.android.http.IKscTransferListener.KscTransferListener;

import android.util.Log;

public class TransportListener extends KscTransferListener {
    private static final String TAG = "TransportListener";

    public static final int OPERATION_UPLOAD = 0;
    public static final int OPERATION_DOWNLOAD = 1;

    private static final int SPEED_DUR = 5;

    private final KscSpeedMonitor mMonitor;

    private int mOperationType;

    private long mLatest = -1;

    private static final KscSpeedManager mSpeeder = new KscSpeedManager(
            SPEED_DUR + 1);

    public TransportListener(int operation, String tag) {
        mOperationType = operation;
        mMonitor = mSpeeder.getMoniter("Localhost:" + tag);
    }

    @Override
    public void onDataSended(long pos, long total) {
        if (mOperationType == OPERATION_UPLOAD) {
            onTransData(pos, total);
            Log.v(TAG, "upload data:" + pos + " " + total);
        }
    }

    @Override
    public void onDataReceived(long pos, long total) {
        if (mOperationType == OPERATION_DOWNLOAD) {
            onTransData(pos, total);
            Log.v(TAG, "receive data:" + pos + " " + total);
        }
    }

    private void onTransData(long pos, long total) {
        if (pos > mLatest && mLatest >= 0) {
            mMonitor.recode(pos - mLatest);
            int speed = mSpeeder.getAverageSpeed(mMonitor.getHost(), SPEED_DUR);
            Log.v(TAG, "pos:" + pos + " ,total" + pos + " ,Current Speed:"
                    + speed);
            mLatest = pos;
        }
    }
}
