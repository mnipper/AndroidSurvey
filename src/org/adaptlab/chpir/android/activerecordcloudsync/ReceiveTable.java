package org.adaptlab.chpir.android.activerecordcloudsync;

import org.json.JSONObject;

public interface ReceiveTable {
    public void createObjectFromJSON(JSONObject jsonObject);
    public String remoteTableName();
}
