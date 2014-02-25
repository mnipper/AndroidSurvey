package org.adaptlab.chpir.android.survey.Models;

import android.util.Log;

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
    @Column(name = "ApiUrl")
    private String mApiUrl;
    @Column(name = "CustomLocaleCode")
    private String mCustomLocaleCode;
    @Column(name = "ShowSurveys")
    private boolean mShowSurveys;
    @Column(name = "ShowSkip")
    private boolean mShowSkip;
    @Column(name = "ShowNA")   
    private boolean mShowNA;
    @Column(name = "ShowRF")    
    private boolean mShowRF;
    @Column(name = "ShowDK")    
    private boolean mShowDK;
    
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
        	Log.i(TAG, "Creating new admin settings instance");
            adminSettings = new AdminSettings();
            adminSettings.save();
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
        mShowSurveys = false;
    }
    
    public void setDeviceIdentifier(String id) {
        Log.i(TAG, "Setting device identifier: " + id);
        mDeviceIdentifier = id;
        save();
    }
    
    public String getDeviceIdentifier() {
        return mDeviceIdentifier;
    }
    
    /**
     * Millisecond sync interval
     */
    public int getSyncInterval() {
        return mSyncInterval;
    }

    /**
     * Second sync interval
     */
    public int getSyncIntervalInMinutes() {
        return mSyncInterval / (60 * 1000);
    }
    
    /**
     * Set the interval in minutes, it is converted to milliseconds
     */
    public void setSyncInterval(int interval) {
        Log.i(TAG, "Setting set interval: " + (interval * 1000 * 60));
        mSyncInterval = interval * 1000 * 60;
        save();
    }
    
    public void setApiUrl(String apiUrl) {
        Log.i(TAG, "Setting api endpoint: " + apiUrl);
        mApiUrl = apiUrl;
        save();
    }
    
    public String getApiUrl() {
        return mApiUrl;
    }
    
    public void setCustomLocaleCode(String code) {
        mCustomLocaleCode = code;
        save();
    }
    
    public String getCustomLocaleCode() {
        return mCustomLocaleCode;
    }
    
    public void setShowSurveys(boolean showSurveys) {
        mShowSurveys = showSurveys;
        save();
    }
    
    public boolean getShowSurveys() {
        return mShowSurveys;
    }
    
    public void setShowSkip(boolean showSkip) {
        mShowSkip = showSkip;
        save();
    }
    
    /*
     * Show SKIP special response for each question if true
     */
    public boolean getShowSkip() {
        return mShowSkip;
    }
    
    public void setShowNA(boolean showNA) {
        mShowNA = showNA;
        save();
    }

    /*
     * Show NOT APPLICABLE special response for each question if true
     */
    public boolean getShowNA() {
        return mShowNA;
    }
    
    public void setShowRF(boolean showRF) {
        mShowRF = showRF;
        save();
    }
    
    /*
     * Show REFUSED special response for each question if true
     */
    public boolean getShowRF() {
        return mShowRF;
    }
    
    public void setShowDK(boolean showDK) {
        mShowDK = showDK;
        save();
    }
    
    /*
     * Show DONT KNOW special response for each question if true
     */
    public boolean getShowDK() {
        return mShowDK;
    }
}
