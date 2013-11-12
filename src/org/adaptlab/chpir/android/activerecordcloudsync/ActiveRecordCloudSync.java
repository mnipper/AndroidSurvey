package org.adaptlab.chpir.android.activerecordcloudsync;

import java.util.LinkedHashMap;
import java.util.Map;

import android.util.Log;

public class ActiveRecordCloudSync {
    private static final String TAG="ActiveRecordCloudSync";
    private static Map<String, Class<? extends ReceiveModel>> mReceiveTables =
            new LinkedHashMap<String, Class<? extends ReceiveModel>>();
    private static Map<String, Class<? extends SendModel>> mSendTables =
            new LinkedHashMap<String, Class<? extends SendModel>>();
    
    // The remote API endpoint url.
    private static String mEndPoint;
    
    /**
     * Add a ReceiveTable.  A ReceiveTable is an active record model class that extends the
     * ReceiveModel abstract class.
     * 
     * @param tableName
     * @param receiveTable
     */
    public static void addReceiveTable(String tableName, Class<? extends ReceiveModel> receiveTable) {
        mReceiveTables.put(tableName, receiveTable);
    }
    
    public static Map<String, Class<? extends ReceiveModel>> getReceiveTables() {
        return mReceiveTables;
    }
    
    public static void addSendTable(String tableName, Class<? extends SendModel> sendTable) {
        mSendTables.put(tableName, sendTable);
    }
    
    public static void setEndPoint(String endPoint) {
        mEndPoint = endPoint;
    }
    
    public static String getEndPoint() {
        return mEndPoint;
    }
    
    public static void syncReceiveTables() {
        for (Map.Entry<String, Class<? extends ReceiveModel>> entry : mReceiveTables.entrySet()) {
            Log.i(TAG, "Syncing " + entry.getValue() + " from remote table " + entry.getKey());
            HttpFetchr httpFetchr = new HttpFetchr(entry.getKey(), entry.getValue());
            httpFetchr.fetch();
        }
    }
    
    public static void syncSendTables() {
        for (Map.Entry<String, Class<? extends SendModel>> entry : mSendTables.entrySet()) {
            Log.i(TAG, "Syncing " + entry.getValue() + " to remote table " + entry.getKey());
            HttpPushr httpPushr = new HttpPushr(entry.getKey(), entry.getValue());
            httpPushr.push();
        }   
    }
}
