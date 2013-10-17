package org.adaptlab.chpir.android.activerecordcloudsync;

import java.util.ArrayList;

public class ActiveRecordCloudSync {
    private static ArrayList<Class<? extends ReceiveTable>> mReceiveTables =
            new ArrayList<Class<? extends ReceiveTable>>();
    private static String mEndPoint;
    
    public static void addReceiveTable(Class<? extends ReceiveTable> receiveTable) {
        mReceiveTables.add(receiveTable);
    }
    
    public static ArrayList<Class<? extends ReceiveTable>> getReceiveTables() {
        return mReceiveTables;
    }
    
    public static void setEndPoint(String endPoint) {
        mEndPoint = endPoint;
    }
    
    public static String getEndPoint() {
        return mEndPoint;
    }
}
