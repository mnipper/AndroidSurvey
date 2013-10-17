package org.adaptlab.chpir.android.cloudtable;

import java.util.ArrayList;

public class CloudTable {
    private static ArrayList<Class<?>> mReceiveTables = new ArrayList<Class<?>>();
    
    public static void addReceiveTable(Class<?> receiveTable) {
        mReceiveTables.add(receiveTable);
    }
    
    public static ArrayList<Class<?>> getReceiveTables() {
        return mReceiveTables;
    }
}
