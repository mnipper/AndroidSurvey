package org.adaptlab.chpir.android.survey.Models;

import org.adaptlab.chpir.android.activerecordcloudsync.SendTable;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Responses")
public class Response extends Model implements SendTable {
	
	@Column(name = "Question")
	private Question mQuestion;
	@Column(name = "Response")
	private String mResponse;
	@Column(name = "Survey")
	private Survey mSurvey;
	@Column(name = "Other_Response")
	private String mOtherResponse;
	
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
        // TODO Auto-generated method stub
        return null;
    }
}
