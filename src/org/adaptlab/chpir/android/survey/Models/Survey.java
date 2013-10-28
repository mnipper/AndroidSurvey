package org.adaptlab.chpir.android.survey.Models;

import java.util.List;
import java.util.UUID;

import org.adaptlab.chpir.android.activerecordcloudsync.SendModel;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Surveys")
public class Survey extends SendModel {
    private static final String TAG = "Survey";

    @Column(name = "Instrument")
    private Instrument mInstrument;
    @Column(name = "UUID")
    private String mUUID;
    @Column(name = "SentToRemote")
    private boolean mSent;

    public Survey() {
        super();
        mSent = false;
        mUUID = UUID.randomUUID().toString();
    }

    public Instrument getInstrument() {
        return mInstrument;
    }

    public void setInstrument(Instrument instrument) {
        mInstrument = instrument;
    }

    public List<Response> responses() {
        return getMany(Response.class, "Survey");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("instrument_id", getInstrument().getRemoteId());
            jsonObject.put("device_id", AdminSettings.getDeviceId());
            jsonObject.put("uuid", mUUID);
            
            json.put("survey", jsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        return json;
    }
    
    public String getUUID() {
        return mUUID;
    }
    
    @Override
    public boolean isSent() {
        return mSent;
    }
    
    @Override
    public void setAsSent() {
        mSent = true;
    }
}
