package org.adaptlab.chpir.android.survey;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Responses")
public class Response extends Model {
	
	@Column(name = "Question")
	private Question mQuestion;
	@Column(name = "Response")
	private String mResponse;
	@Column(name = "Survey")
	private Survey mSurvey;
	
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
}
