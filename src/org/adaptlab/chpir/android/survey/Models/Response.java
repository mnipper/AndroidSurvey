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
	@Column(name = "Response")
	private String mResponse;
	@Column(name = "Survey")
	private Survey mSurvey;
	@Column(name = "Other_Response")
	private String mOtherResponse;
	@Column(name = "SentToRemote")
	private boolean mSent;
	
	public Response() {
		super();
	}

	public Question getQuestion() {
		return mQuestion;
	}

	public void setQuestion(Question question) {
		mQuestion = question;
	}

	public String getResponse() {
		return mResponse;
	}

	public void setResponse(String response) {
		mResponse = response;
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("survey_id", getSurvey().getId());
            jsonObject.put("question_id", getQuestion().getId());
            jsonObject.put("response", getResponse());
            jsonObject.put("other_response", getOtherResponse());
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        return jsonObject;
    }
    
    @Override
    public boolean isNotSent() {
        return mSent;
    }
    
    @Override
    public void setAsSent() {
        mSent = true;
    }
}
