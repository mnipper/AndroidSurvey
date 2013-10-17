package org.adaptlab.chpir.android.activerecordcloudsync;

import java.util.ArrayList;

public class ActiveRecordCloudSync {
    private static ArrayList<Class<?>> mReceiveTables = new ArrayList<Class<?>>();
    private static String mEndPoint;
    
    public static void addReceiveTable(Class<?> receiveTable) {
        mReceiveTables.add(receiveTable);
    }
    
    public static ArrayList<Class<?>> getReceiveTables() {
        return mReceiveTables;
    }
    
    public static void setEndPoint(String endPoint) {
        mEndPoint = endPoint;
    }
    
    public static String getEndPoint() {
        return mEndPoint;
    }
}
