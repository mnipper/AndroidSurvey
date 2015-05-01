package org.adaptlab.chpir.android.survey.Models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.adaptlab.chpir.android.activerecordcloudsync.SendModel;
import org.adaptlab.chpir.android.survey.AuthUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Responses")
public class Response extends SendModel {
    private static final String TAG = "Response";
    public static final String SKIP = "SKIP";
    public static final String RF = "RF";
    public static final String NA = "NA";
    public static final String DK = "DK";
    public static final String LOGICAL_SKIP = "LOGICAL_SKIP";
	
	@Column(name = "Question")
	private Question mQuestion;
	@Column(name = "Text")
	private String mText;
	@Column(name = "Survey")
	private Survey mSurvey;
	@Column(name = "Other_Response")
	private String mOtherResponse;
	@Column(name = "SpecialResponse")
	private String mSpecialResponse;
	@Column(name = "SentToRemote")
	private boolean mSent;
	@Column(name = "TimeStarted")
	private Date mTimeStarted;
	@Column(name = "TimeEnded")
	private Date mTimeEnded;
	@Column(name = "UUID")
	private String mUUID;
	@Column(name = "DeviceUser")
	private DeviceUser mDeviceUser;
	@Column(name = "QuestionVersion")
	private int mQuestionVersion;
	
	public Response() {
		super();
		mSent = false;
		mText = "";
		mSpecialResponse = "";
		mUUID = UUID.randomUUID().toString();
		setDeviceUser(AuthUtils.getCurrentUser());
	}
	
	public String getUUID() {
		return mUUID;
	}
	    
	/*
	 * Return true if this response matches the regular expression
	 * in its question.  If the regular expression is the empty string,
	 * declare it a match and return true.
	 */
    public boolean isValid() {
        if (mQuestion.getRegExValidation() == null) {
            return true;
        }
        return getText().matches(mQuestion.getRegExValidation());
    }
    
    /*
     * Only save if this response is valid.  If valid, return
     * true.  If not, return false.
     */
    public boolean saveWithValidation() {
        if (isValid()) {
            setQuestionVersion(getQuestion().getQuestionVersion());
            save();
            mSurvey.setLastUpdated(new Date());
            mSurvey.save();
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("survey_uuid", getSurvey().getUUID());
            jsonObject.put("question_id", getQuestion().getRemoteId());
            jsonObject.put("text", getText());
            jsonObject.put("other_response", getOtherResponse());
            jsonObject.put("special_response", getSpecialResponse());
            jsonObject.put("time_started", getTimeStarted());
            jsonObject.put("time_ended", getTimeEnded());
            jsonObject.put("question_identifier", getQuestion().getQuestionIdentifier());
            jsonObject.put("uuid", getUUID());
            jsonObject.put("question_version", getQuestionVersion());
            if (getDeviceUser() != null) {
                jsonObject.put("device_user_id", getDeviceUser().getRemoteId());
            }
            
            json.put("response", jsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        return json;
    }
    
    @Override
    public boolean isPersistent() { return true; }
    
    /*
     * Finders
     */
    public static List<Response> getAll() {
        return new Select().from(Response.class).orderBy("Id ASC").execute();
    }
    
    /*
     * Getters/Setters
     */
	public Question getQuestion() {
		return mQuestion;
	}

	public void setQuestion(Question question) {
		mQuestion = question;
	}

	public String getText() {
		return mText;
	}

	public void setResponse(String text) {
		mText = text;
	}
	
	public void setSurvey(Survey survey) {
		mSurvey = survey;
	}
	
	public Survey getSurvey() {
		return mSurvey;
	}
	
	public void setOtherResponse(String otherResponse) {
	    mOtherResponse = otherResponse;
	}
	
	public String getOtherResponse() {
	    return mOtherResponse;
	}
	
	public void setSpecialResponse(String specialResponse) {
		mSpecialResponse = specialResponse;
	}
	
	public String getSpecialResponse() {
		return mSpecialResponse;
	}
	
	public void setTimeStarted(Date time) {
		mTimeStarted = time;
	}
	
	public Date getTimeStarted() {
		return mTimeStarted;
	}
	
	public void setTimeEnded(Date time) {
		mTimeEnded = time;
	}
	
	public Date getTimeEnded() {
		return mTimeEnded;
	}
	
	public ResponsePhoto getResponsePhoto() {
		return new Select().from(ResponsePhoto.class).where("Response = ?", getId()).executeSingle();
	}
    
    @Override
    public boolean isSent() {
        return mSent;
    }

    @Override
    public void setAsSent(Context context) {
    	mSent = true;
    	this.save();

    	if (getResponsePhoto() == null) {	
    		this.delete();
    	}

    	mSurvey.deleteIfComplete();
    }
    
    /*
     * Only send if survey is ready to send.
     */
    @Override 
    public boolean readyToSend() {
        return getSurvey().readyToSend();
    }
    
    public boolean hasSpecialResponse() {
        return mSpecialResponse.equals(SKIP) || mSpecialResponse.equals(RF) ||
                mSpecialResponse.equals(NA) || mSpecialResponse.equals(DK);
    }
    
    public DeviceUser getDeviceUser() {
        return mDeviceUser;
    }
    
    public void setDeviceUser(DeviceUser deviceUser) {
        mDeviceUser = deviceUser;
    }  
    
    private void setQuestionVersion(int version) {
        mQuestionVersion = version;
    }
    
    private int getQuestionVersion() {
        return mQuestionVersion;
    }
}
