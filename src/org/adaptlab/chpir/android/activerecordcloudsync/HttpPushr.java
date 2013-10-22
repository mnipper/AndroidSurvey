package org.adaptlab.chpir.android.activerecordcloudsync;

import android.util.Log;

public class HttpPushr {
    private static final String TAG = "HttpPushr";
    private Class<? extends SendTable> mSendTableClass;
    private String mRemoteTableName;
    
    public HttpPushr(String remoteTableName, Class<? extends SendTable> sendTableClass) {
        mSendTableClass = sendTableClass;
        mRemoteTableName = remoteTableName;
    }
    
    public void push() {
        if (ActiveRecordCloudSync.getEndPoint() == null) {
            Log.i(TAG, "ActiveRecordCloudSync end point is not set!");
            return;
        }
        
        // TODO Push non-pushed entries in mSendTableClass to remote table
    }
}
