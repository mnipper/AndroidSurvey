package org.adaptlab.chpir.android.activerecordcloudsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ActiveRecordCloudSync {
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
            HttpFetchr httpFetchr = new HttpFetchr(entry.getKey(), entry.getValue());
            httpFetchr.fetch();
        }
    }
}
