package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.SendModel;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Responses")
public class Response extends SendModel {
    private static final String TAG = "Response";
    public static final String SKIPPED = "SKIP";
	
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
	
	public Response() {
		super();
		mSent = false;
		mText = "";
		mSpecialResponse = "";
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
            save();
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
            
            json.put("response", jsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        return json;
    }
    
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
    
    @Override
    public boolean isSent() {
        return mSent;
    }
    
    @Override
    public void setAsSent() {
        mSent = true;
        this.delete(); // Delete from device after successful send
        Log.d(TAG, Response.getAll().size() + " responses left on device");
    }
    
    /*
     * Only send if survey is ready to send.
     */
    @Override 
    public boolean readyToSend() {
        return getSurvey().readyToSend();
    }
}
