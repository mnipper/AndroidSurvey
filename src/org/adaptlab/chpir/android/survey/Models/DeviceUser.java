package org.adaptlab.chpir.android.survey.Models;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.adaptlab.chpir.android.survey.AppUtil;
import org.adaptlab.chpir.android.survey.Vendor.BCrypt;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "DeviceUser")
public class DeviceUser extends ReceiveModel {
    private static final String TAG = "DeviceUser";
    
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
    @Column(name = "Name")
    private String mName;
    @Column(name = "UserName", unique = true)
    private String mUserName;
    @Column(name = "PasswordDigest")
    private String mPasswordDigest;
    @Column(name = "Active")
    private boolean mActive;
    
    private void setName(String name) {
        mName = name;
    }
    
    public String getName() {
        return mName;
    }
    
    private void setUserName(String userName) {
        mUserName = userName;
    }
    
    public String getUserName() {
        return mUserName;
    }
    
    public boolean checkPassword(String password) {
        if (!mActive) {
            return false;
        }
        return BCrypt.checkpw(password, mPasswordDigest);
    }
    
    private void setPasswordDigest(String digest) {
        mPasswordDigest = digest;
    }
    
    private void setActive(boolean active) {
        mActive = active;
    }
    
    public boolean getActive() {
        return mActive;
    }
    
    private void setRemoteId(long remoteId) {
        mRemoteId = remoteId;
    }
    
    public long getRemoteId() {
        return mRemoteId;
    }

    @Override
    public void createObjectFromJSON(JSONObject jsonObject) {
        try {
            Long remoteId = jsonObject.getLong("id");
            
            DeviceUser deviceUser = DeviceUser.findByRemoteId(remoteId);
            if (deviceUser == null) {
                deviceUser = this;
            }
            
            if (AppUtil.DEBUG) Log.i(TAG, "Creating object from JSON Object: " + jsonObject);
            
            deviceUser.setRemoteId(remoteId);
            deviceUser.setName(jsonObject.getString("name"));
            deviceUser.setUserName(jsonObject.getString("username"));
            deviceUser.setPasswordDigest(jsonObject.getString("password_digest"));
            
            if (jsonObject.isNull("active")) {
                deviceUser.setActive(false);
            } else {
                deviceUser.setActive(jsonObject.getBoolean("active")); 
            }
            
            deviceUser.save();
            
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }
    }
    
    public static DeviceUser findByRemoteId(Long id) {
        return new Select().from(DeviceUser.class).where("RemoteId = ?", id).executeSingle();
    }
    
    public static DeviceUser findByUserName(String userName) {
        return new Select().from(DeviceUser.class).where("UserName = ?", userName).executeSingle();
    }
}
