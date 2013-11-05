package org.adaptlab.chpir.android.survey.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "AdminSettings")
public class AdminSettings extends Model {
    private static final String TAG = "AdminSettings";
    @Column(name = "DeviceIdentifier")
    private String mDeviceIdentifier;
    @Column(name = "SyncInterval")
    private int mSyncInterval;
    
    private static AdminSettings adminSettings;
    
    /**
     * This maintains a single row in the database for the admin settings, and
     * effectively is a Singleton.  This is done to piggy-back on the
     * ReceiveModel functionality.
     * 
     */
    public static AdminSettings getInstance() {
        adminSettings = new Select().from(AdminSettings.class).orderBy("Id asc").executeSingle();
        if (adminSettings == null) {
            adminSettings = new AdminSettings();
        }
        return adminSettings;
    }
    
    /**
     * Typically a Singleton constructor is private, but in this case the constructor
     * must be public for ActiveAndroid to function properly.  Do not use this
     * constructor, use getInstance() instead.
     * 
     */
    public AdminSettings() {
        super();
    }
    
    public void setDeviceIdentifier(String id) {
        mDeviceIdentifier = id;
        save();
    }
    
    public String getDeviceIdentifier() {
        return mDeviceIdentifier;
    }
    
    public int getSyncInterval() {
        return mSyncInterval;
    }
    
    public void setSyncInterval(int interval) {
        mSyncInterval = interval;
        save();
    }

}
