package org.adaptlab.chpir.android.activerecordcloudsync;

import java.util.HashMap;
import java.util.Map;
import android.util.Log;

public class ActiveRecordCloudSync {
    private static final String TAG="ActiveRecordCloudSync";
    private static Map<String, Class<? extends ReceiveTable>> mReceiveTables =
            new HashMap<String, Class<? extends ReceiveTable>>();
    private static String mEndPoint;
    
    public static void addReceiveTable(String tableName, Class<? extends ReceiveTable> receiveTable) {
        mReceiveTables.put(tableName, receiveTable);
    }
    
    public static Map<String, Class<? extends ReceiveTable>> getReceiveTables() {
        return mReceiveTables;
    }
    
    public static void setEndPoint(String endPoint) {
        mEndPoint = endPoint;
    }
    
    public static String getEndPoint() {
        return mEndPoint;
    }
    
    public static void syncReceiveTables() {
        for (Map.Entry<String, Class<? extends ReceiveTable>> entry : mReceiveTables.entrySet()) {
            Log.i(TAG, "Syncing " + entry.getValue() + " from remote table " + entry.getKey());
            HttpFetchr httpFetchr = new HttpFetchr(entry.getKey(), entry.getValue());
            httpFetchr.fetch();
        }
    }
}
