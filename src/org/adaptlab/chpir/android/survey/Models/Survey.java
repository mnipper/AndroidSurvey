package org.adaptlab.chpir.android.survey.Models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.adaptlab.chpir.android.activerecordcloudsync.SendModel;
import org.adaptlab.chpir.android.survey.AppUtil;
import org.adaptlab.chpir.android.survey.R;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
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
    @Column(name = "LastUpdated")
    private Date mLastUpdated;
    @Column(name = "LastQuestion")
    private Question mLastQuestion;
    @Column(name = "Metadata")
    private String mMetadata;
    @Column(name = "ProjectId")
    private Long mProjectId;

    public Survey() {
        super();
        mSent = false;
        mComplete = false;
        mUUID = UUID.randomUUID().toString();
        mLastUpdated = new Date();
    }
    
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("instrument_id", getInstrument().getRemoteId());
            jsonObject.put("instrument_version_number", getInstrument().getVersionNumber());
            jsonObject.put("device_uuid", getAdminInstanceDeviceIdentifier());
            jsonObject.put("device_label", AppUtil.getAdminSettingsInstance().getDeviceLabel());
            jsonObject.put("uuid", mUUID);
            jsonObject.put("instrument_title", getInstrument().getTitle());
            jsonObject.put("latitude", mLatitude);
            jsonObject.put("longitude", mLongitude);
            jsonObject.put("metadata", mMetadata);
            
            json.put("survey", jsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        return json;
    }
    
    public String getAdminInstanceDeviceIdentifier() {
    	return AppUtil.getAdminSettingsInstance().getDeviceIdentifier();
    }
    
    @Override
    public boolean isPersistent() { return true; }
    
    /*
     * The identifier to display to the user to identify a Survey.
     * Return Unidentified Survey string if no response for identifier questions.
     */
    public String identifier(Context context) {
    	String surveyLabel = null;
    	String identifier = "";
    	
    	if (!TextUtils.isEmpty(getMetadata())) { surveyLabel = getMetadataLabel(); }    	
    	if (!TextUtils.isEmpty(surveyLabel))  { return surveyLabel; }
    	
    	for (Response response : responses()) {
    		if (response.getQuestion().identifiesSurvey()) {
    			identifier += response.getText() + " ";
    		}
    	}
    	
    	if (identifier.trim().isEmpty())
    		return context.getString(R.string.unidentified_survey) + " " + getId();
    	else
    		return identifier;
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
    
    public static List<Survey> getAll() {
        return new Select().from(Survey.class).orderBy("LastUpdated DESC").execute();
    }

    public static List<Survey> getAllProjectSurveys(Long projectId) {
    	return new Select("Surveys.*")
    			.from(Survey.class)
    			.innerJoin(Instrument.class)
    			.on("Surveys.Instrument=Instruments.Id AND Instruments.Published=" + 1)
    			.where("Surveys.ProjectId = ?", projectId)
    			.orderBy("Surveys.LastUpdated DESC")
    			.execute();
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
    public void setAsSent(Context context) {
        mSent = true;
        this.save();
        
        EventLog eventLog = new EventLog(EventLog.EventType.SENT_SURVEY, context);
        eventLog.setInstrument(mInstrument);
        eventLog.setSurveyIdentifier(identifier(context));
        eventLog.save();
        
        Resources r = context.getResources();

        Notification notification = new NotificationCompat.Builder(context)
            .setTicker(r.getString(R.string.app_name))
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(r.getString(R.string.app_name))
            .setContentText(eventLog.getLogMessage(context))
            .setAutoCancel(true)
            .build();
        
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        notificationManager.notify(eventLog.getLogMessage(context), 1, notification);
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
    
    public Date getLastUpdated() {
        return mLastUpdated;
    }
    
    public void setLastUpdated(Date lastUpdate) {
        mLastUpdated = lastUpdate;
    }
    
    public Question getLastQuestion() {
        return mLastQuestion;
    }
    
    public void setLastQuestion(Question question) {
        mLastQuestion = question;
    }
    
    public void deleteIfComplete() {
		if (this.responses().size() == 0) {
			this.delete();
		}
    }
    
    public void setMetadata(String metadata) {
        mMetadata = metadata;
    }
    
    public String getMetadata() {
        return mMetadata;
    }
    
    public void setProjectId(Long id) {
    	mProjectId = id;
    }
    
    public Long getProjectId() {
    	return mProjectId;
    }
    
    private String getMetadataLabel() {
        try {
            JSONObject metadata = new JSONObject(getMetadata());
            if (metadata.has("survey_label")) {
                return metadata.getString("survey_label");
            }
        } catch (JSONException er) {
            Log.e(TAG, er.getMessage());
        }
        
        return "";
    }
}
