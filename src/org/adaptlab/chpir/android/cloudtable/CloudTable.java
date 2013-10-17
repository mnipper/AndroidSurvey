package org.adaptlab.chpir.android.cloudtable;

import java.util.ArrayList;

public class CloudTable {
    private static ArrayList<ReceiveTable> mReceiveTables = new ArrayList<ReceiveTable>();
    
    public static void addReceiveTable(ReceiveTable receiveTable) {
        mReceiveTables.add(receiveTable);
    }
}
