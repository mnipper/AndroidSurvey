package org.adaptlab.chpir.android.survey.Models;

import java.util.Date;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

public class EventLog extends Model {
    private final static String TAG = "EVENT";
    
    public static enum EventType {
        SENT_SURVEY
    }
    
    @Column(name = "EventType")
    private EventType mEventType;
    @Column(name = "SurveyIdentifier")
    private String mSurveyIdentifier;
    @Column(name = "Instrument")
    private Instrument mInstrument;
    @Column(name = "Timestamp")
    private Date mTimestamp; 
    
    public EventLog(EventType eventType) {
        super();
        mEventType = eventType;
        mTimestamp = new Date();
    }
    
    public void setInstrument(Instrument instrument) {
        mInstrument = instrument;
    }
    
    public void setSurveyIdentifier(String identifier) {
        mSurveyIdentifier = identifier;
    }
    
    public Instrument getInstrument() {
        return mInstrument;
    }
    
    public String getSurveyIdentifier() {
        return mSurveyIdentifier;
    }
    
    public Date getTimestamp() {
        return mTimestamp;
    }
    
    public String getLogMessage() {
        if (mEventType == EventType.SENT_SURVEY) {
            // TODO: Internationalize
            return "Sent " + mInstrument.getTitle() + " for " + mSurveyIdentifier;
        }
        
        return "";
    }
}