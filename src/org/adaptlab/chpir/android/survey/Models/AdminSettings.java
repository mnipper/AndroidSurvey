package org.adaptlab.chpir.android.survey.Models;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.json.JSONObject;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "AdminSettings")
public class AdminSettings extends ReceiveModel {
    @Column(name = "DeviceId")
    private static String sDeviceId;
    
    public static void setDeviceId(String id) {
        sDeviceId = id;
    }
    
    public static String getDeviceId() {
        return sDeviceId;
    }
    
    @Override
    public void createObjectFromJSON(JSONObject jsonObject) {
        // TODO Auto-generated method stub
        
    }

}
