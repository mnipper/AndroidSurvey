package org.adaptlab.chpir.android.activerecordcloudsync;

import org.json.JSONObject;

public interface ReceiveTable {
    public Long lastId();
    public void createObjectFromJSON(JSONObject jsonObject);
}
