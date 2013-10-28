package org.adaptlab.chpir.android.survey.Models;

import org.adaptlab.chpir.android.activerecordcloudsync.SendModel;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Responses")
public class Response extends SendModel {
    private static final String TAG = "Response";
	
	@Column(name = "Question")
	private Question mQuestion;
	@Column(name = "Text")
	private String mText;
	@Column(name = "Survey")
	private Survey mSurvey;
	@Column(name = "Other_Response")
	private String mOtherResponse;
	@Column(name = "SentToRemote")
	private boolean mSent;
	
	public Response() {
		super();
		mSent = false;
	}

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

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("survey_uuid", getSurvey().getUUID());
            jsonObject.put("device_id", AdminSettings.getDeviceId());
            jsonObject.put("question_id", getQuestion().getRemoteId());
            jsonObject.put("text", getText());
            jsonObject.put("other_response", getOtherResponse());
            
            json.put("response", jsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        return json;
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
