package org.adaptlab.chpir.android.survey.Models;

import java.util.Date;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.SendModel;
import org.adaptlab.chpir.android.survey.EncryptUtil;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ConsentForms")
public class ConsentForm extends SendModel {
    private static final String TAG = "ConsentForm";
    
    @Column(name = "Name")
    private String mName;
    @Column(name = "Email")
    private String mEmail;
    @Column(name = "Date")
    private Date mDate;

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", getName());
            jsonObject.put("email", getEmail());
            jsonObject.put("date", getDate());

            json.put("consent_form", jsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        
        return json;
    }

    @Override
    public boolean isSent() { return false; }

    @Override
    public boolean readyToSend() { return true; }

    @Override
    public boolean isPersistent() { return true; }

    @Override
    public void setAsSent() {
        this.delete();
    }
    
    @Override
    public String getEndPoint() {
        return ActiveRecordCloudSync.getConsentEndPoint();
    }
    
    public void setName(String name) {
        mName = EncryptUtil.encrypt(name);
    }
    
    public void setEmail(String email) {
        mEmail = EncryptUtil.encrypt(email);
    }

    public void setDate(Date date) {
        mDate = date;
    }
    
    private String getName() {
        return mName;
    }
    
    private String getEmail() {
        return mEmail;
    }
    
    public Date getDate() {
        return mDate;
    }
}
