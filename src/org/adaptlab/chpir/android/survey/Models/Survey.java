package org.adaptlab.chpir.android.survey.Models;

import java.util.List;
import java.util.UUID;

import org.adaptlab.chpir.android.activerecordcloudsync.SendModel;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Surveys")
public class Survey extends SendModel {
    private static final String TAG = "Survey";

    @Column(name = "Instrument")
    private Instrument mInstrument;
    @Column(name = "UUID")
    private String mUUID;
    @Column(name = "SentToRemote")
    private boolean mSent;
    @Column(name = "Complete")
    private boolean mComplete;
    @Column(name = "Latitude")
    private String mLatitude;
    @Column(name= "Longitude")
    private String mLongitude;

    public Survey() {
        super();
        mSent = false;
        mComplete = false;
        mUUID = UUID.randomUUID().toString();
    }
    
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("instrument_id", getInstrument().getRemoteId());
            jsonObject.put("instrument_version_number", getInstrument().getVersionNumber());
            jsonObject.put("device_uuid", getAdminInstanceDeviceIdentifier());
            jsonObject.put("uuid", mUUID);
            jsonObject.put("instrument_title", getInstrument().getTitle());
            jsonObject.put("latitude", mLatitude);
            jsonObject.put("longitude", mLongitude);
            
            json.put("survey", jsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        return json;
    }
    
    public String getAdminInstanceDeviceIdentifier() {
    	return AdminSettings.getInstance().getDeviceIdentifier();
    }
    
    /*
     * Finders
     */   
    public Response getResponseByQuestion(Question question) {
        return new Select().from(Response.class).where(
                "Question = ? AND Survey = ?",
                question.getId(),
                getId()).executeSingle();
    }

    
    /*
     * Relationships
     */
    public List<Response> responses() {
        return getMany(Response.class, "Survey");
    }
 
    
    /*
     * Getters/Setters
     */

    public Instrument getInstrument() {
        return mInstrument;
    }

    public void setInstrument(Instrument instrument) {
        mInstrument = instrument;
    }
    
    public String getUUID() {
        return mUUID;
    }
    
    public void setAsComplete() {
        mComplete = true;
    }
    
    @Override
    public boolean isSent() {
        return mSent;
    }
    
    @Override
    public void setAsSent() {
        mSent = true;
        this.save();
    }
    
    @Override
    public boolean readyToSend() {
        return mComplete;
    }
    
    public void setLatitude(String latitude) {
    	mLatitude = latitude;
    }
    
    public String getLatitude() {
    	return mLatitude;
    }
    
    public void setLongitude(String longitude) {
    	mLongitude = longitude;
    }
    
    public String getLongitude() {
    	return mLongitude;
    }
}
